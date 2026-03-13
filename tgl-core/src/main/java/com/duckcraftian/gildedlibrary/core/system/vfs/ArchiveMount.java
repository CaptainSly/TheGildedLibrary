package com.duckcraftian.gildedlibrary.core.system.vfs;

import com.duckcraftian.gildedlibrary.api.system.archive.GLArchiveReader;
import com.duckcraftian.gildedlibrary.api.system.records.AbstractRecord;
import com.duckcraftian.gildedlibrary.api.system.vfs.AbstractVFSMount;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ArchiveMount extends AbstractVFSMount implements Closeable {

    private final GLArchiveReader glArchive;

    public ArchiveMount(String modId, String mountId, int priority, GLArchiveReader glArchive) {
        super(modId, mountId, priority);
        this.glArchive = glArchive;
    }

    @Override
    public List<String> listFiles(String directory) {
        if (glArchive.getAssetIndex() == null) return List.of();
        String prefix = directory + "/";
        return glArchive.getAssetIndex().getIndexTable().values().stream()
                .flatMap(table -> table.keySet().stream())
                .filter(s -> s.startsWith(prefix) && !s.substring(prefix.length()).contains("/"))
                .toList();
    }

    public List<String> listFilesRecursive(String directory) {
        if (glArchive.getAssetIndex() == null) return List.of();
        String prefix = directory + "/";
        return glArchive.getAssetIndex().getIndexTable().values().stream()
                .flatMap(table -> table.keySet().stream())
                .filter(s -> s.startsWith(prefix))
                .toList();
    }

    public List<String> listRecords(String recordType) {
        if (glArchive.getRecordIndex() == null) return List.of();
        return glArchive.getRecordIndex().getRegistryTable(recordType)
                .orElse(Map.of())
                .keySet().stream().toList();
    }


    @Override
    public Optional<InputStream> getFile(String path) {
        return glArchive.getFile(path);
    }

    public Optional<? extends AbstractRecord> getRecord(String recordId) {
        return glArchive.getRecord(recordId);
    }

    public boolean hasRecord(String recordId) {
        return glArchive.hasRecord(recordId);
    }

    @Override
    public boolean hasFile(String path) {
        return glArchive.hasFile(path);
    }

    @Override
    public void close() throws IOException {
        glArchive.close();
    }
}
