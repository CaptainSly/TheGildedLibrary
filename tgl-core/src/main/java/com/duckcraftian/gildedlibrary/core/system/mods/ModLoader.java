package com.duckcraftian.gildedlibrary.core.system.mods;

import com.duckcraftian.gildedlibrary.api.system.archive.ArchiveIndexTable;
import com.duckcraftian.gildedlibrary.api.system.archive.GLARecordEntry;
import com.duckcraftian.gildedlibrary.api.system.archive.GLArchiveReader;
import com.duckcraftian.gildedlibrary.api.system.archive.asset.GLAAssetReader;
import com.duckcraftian.gildedlibrary.api.system.archive.base.GLABaseReader;
import com.duckcraftian.gildedlibrary.api.system.archive.mod.GLAModReader;
import com.duckcraftian.gildedlibrary.api.system.mods.*;
import com.duckcraftian.gildedlibrary.api.system.records.AbstractRecord;
import com.duckcraftian.gildedlibrary.api.system.registries.RecordRegistry;
import com.duckcraftian.gildedlibrary.api.system.registries.RegistryManager;
import com.duckcraftian.gildedlibrary.core.system.vfs.ArchiveMount;
import com.duckcraftian.gildedlibrary.core.system.vfs.VirtualFileSystem;
import org.tinylog.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ModLoader {

    private final Path modsDirectory;
    private final RegistryManager registryManager;
    private final VirtualFileSystem vfs;
    private final Map<String, GLAAssetReader> assetReaders;
    private final Map<String, GLArchiveReader> modReaders;
    private final List<String> loadOrder;

    public ModLoader(Path modsDirectory, RegistryManager registryManager, VirtualFileSystem vfs) {
        this.modsDirectory = modsDirectory;
        this.registryManager = registryManager;
        this.vfs = vfs;
        modReaders = new LinkedHashMap<>();
        assetReaders = new LinkedHashMap<>();
        loadOrder = new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    public void loadMods() {

        try {
            for (Path modPath : Files.list(modsDirectory).toList()) {
                String archiveFilename = modPath.getFileName().toString();
                if (archiveFilename.endsWith(".gla")) {
                    String id = archiveFilename.substring(0, archiveFilename.length() - 4);
                    assetReaders.put(id, new GLAAssetReader(modPath));
                } else if (archiveFilename.endsWith(".glb")) {
                    GLABaseReader reader = new GLABaseReader(modPath, registryManager);
                    ModMetadata metadata = reader.getMetadata().get();
                    modReaders.put(metadata.getId(), reader);
                } else if (archiveFilename.endsWith(".glm")) {
                    GLAModReader reader = new GLAModReader(modPath, registryManager);
                    ModMetadata metadata = reader.getMetadata().get();
                    modReaders.put(metadata.getId(), reader);
                }

            }
        } catch (IOException | ModConflictException e) {
            Logger.error("Failed to read mod directory: " + e.getMessage());
        }


        if (modReaders.isEmpty()) {
            // We don't care if the AssetReaders has items
            // if the Mod Readers list is empty, we don't load anything
            Logger.info("No Mods found in: " + modsDirectory);
            return;
        }

        resolveDependencyOrder();

        // Create the Asset ArchiveMounts
        List<String> reversedAssets = new ArrayList<>(assetReaders.keySet().stream().toList());
        Collections.reverse(reversedAssets);
        int priority = 0;
        for (String assetId : reversedAssets) {
            GLAAssetReader reader = assetReaders.get(assetId);
            vfs.addMount(new ArchiveMount(assetId, assetId, priority, reader));
            priority++;
        }

        // Create the Mod ArchiveMounts
        List<String> reversedMods = new ArrayList<>(loadOrder);
        Collections.reverse(reversedMods);
        priority = 0;
        for (String modId : reversedMods) {
            GLArchiveReader reader = modReaders.get(modId);
            ModMetadata metadata = reader.getMetadata().get();
            vfs.addMount(new ArchiveMount(metadata.getId(), metadata.getId(), priority, reader));
            priority++;
        }

        // Iterate Load Order and Pull register the Records
        for (String modId : loadOrder) {
            GLArchiveReader mod = modReaders.get(modId);

            Map<String, Map<String, GLARecordEntry>> indexTable = mod.getRecordIndex().getIndexTable();
            if (indexTable == null)
                throw new RuntimeException("Malformed " + mod.getArchiveType().name() + " Archive.");

            for (String recordType : mod.getRecordIndex().getIndexTable().keySet()) {
                for (String recordId : mod.getRecordIndex().getRegistryTable(recordType).get().keySet()) {
                    var record = mod.getRecord(recordId);
                    if (record.isEmpty()) {
                        Logger.warn("Record: " + recordId + " is null. This is a problem.");
                        continue;
                    }

                    var registry = registryManager.getRecordRegistry(recordType);
                    if (registry.isEmpty())
                        throw new MissingRegistryException("Registry: " + recordType + " does not exist. This is a FATAL PROBLEM");

                    ((RecordRegistry) registry.get()).addRecord(record.get());
                }
            }
        }

    }

    private void resolveDependencyOrder() throws ModConflictException {
        List<String> sorted = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Set<String> inProgress = new HashSet<>();

        // Conflict Check - Fail before doing any real work
        for (String modId : modReaders.keySet()) {
            GLArchiveReader modArchive = modReaders.get(modId);
            ModMetadata meta = modArchive.getMetadata().get();
            for (String conflictId : meta.getConflicts()) {
                if (modReaders.containsKey(conflictId))
                    throw new ModConflictException("Conflict detected: " + modId + " conflicts with: " + conflictId);
            }
        }

        // Topological Sort
        for (String modId : modReaders.keySet()) {
            if (!visited.contains(modId))
                visit(modId, sorted, visited, inProgress, modReaders, assetReaders.keySet());
        }

        loadOrder.addAll(sorted);

        // Rebuild Asset Builders
        Set<String> seen = new HashSet<>();
        Map<String, GLAAssetReader> ordered = new LinkedHashMap<>();
        for (String modId : loadOrder) {
            GLArchiveReader modArchive = modReaders.get(modId);
            ModMetadata meta = modArchive.getMetadata().get();
            for (String dependency : meta.getDependencies()) {
                if (assetReaders.keySet().contains(dependency) && !seen.contains(dependency)) {
                    GLAAssetReader reader = assetReaders.get(dependency);
                    ordered.put(dependency, reader);
                    seen.add(dependency);
                }
            }
        }

        assetReaders.clear();
        assetReaders.putAll(ordered);
    }

    private void visit(String modId, List<String> sorted, Set<String> visited, Set<String> inProgress, Map<String, GLArchiveReader> modMap, Set<String> assetIds) {
        if (inProgress.contains(modId))
            throw new CircularDependencyException("Mod: " + modId + " requires itself");
        if (visited.contains(modId))
            return;

        inProgress.add(modId);

        GLArchiveReader mod = modReaders.get(modId);
        ModMetadata metadata = mod.getMetadata().get();
        for (String dependency : metadata.getDependencies()) {
            if (modMap.containsKey(dependency))
                visit(dependency, sorted, visited, inProgress, modMap, assetIds);
            else if (assetIds.contains(dependency))
                continue;
            else if (registryManager.getLoadedPlugins().contains(dependency))
                continue;
            else
                throw new MissingDependencyException("Missing Dependency: " + dependency);
        }

        for (String dependency : metadata.getOptionalDependencies()) {
            if (modMap.containsKey(dependency))
                visit(dependency, sorted, visited, inProgress, modMap, assetIds);
        }

        inProgress.remove(modId);
        visited.add(modId);
        sorted.add(modId);
    }

    public List<String> getLoadOrder() {
        return loadOrder;
    }

}
