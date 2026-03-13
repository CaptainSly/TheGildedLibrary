package com.duckcraftian.gildedlibrary.api.system.archive;

import com.duckcraftian.gildedlibrary.api.system.archive.asset.GLAAssetEntry;
import com.duckcraftian.gildedlibrary.api.system.mods.ModMetadata;
import com.duckcraftian.gildedlibrary.api.system.records.AbstractRecord;
import com.duckcraftian.gildedlibrary.api.system.registries.RecordRegistry;
import com.duckcraftian.gildedlibrary.api.system.registries.RegistryManager;
import com.duckcraftian.gildedlibrary.api.system.serialization.AbstractRecordSerializer;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public abstract class GLArchiveReader implements Closeable {

    protected ArchiveType archiveType;
    protected final FileChannel channel;
    protected final Path archivePath;
    protected final RegistryManager registryManager;

    protected final Map<String, ArchiveIndexTable<?>> indexTables;

    protected ModMetadata metadata = null;

    public GLArchiveReader(Path archivePath, RegistryManager registryManager) throws IOException {
        this.archivePath = archivePath;
        this.registryManager = registryManager;
        this.channel = FileChannel.open(this.archivePath, StandardOpenOption.READ);
        indexTables = new HashMap<>();
    }

    public ArchiveType getArchiveType() {
        return archiveType;
    }

    protected abstract ArchiveType readIndex();

    protected ModMetadata readMetadata(DataInputStream in) throws IOException {
        String id = in.readUTF();
        String name = in.readUTF();
        String version = in.readUTF();
        String description = in.readUTF();
        String author = in.readUTF();
        String website = in.readUTF();
        String engineVersion = in.readUTF();
        ModMetadata.ModType modType = ModMetadata.ModType.getType(in.readUTF());
        int priority = in.readInt();

        int length = in.readInt();
        String[] tags = new String[length];
        for (int i = 0; i < length; i++)
            tags[i] = in.readUTF();

        length = in.readInt();
        String[] dependencies = new String[length];
        for (int i = 0; i < length; i++)
            dependencies[i] = in.readUTF();

        length = in.readInt();
        String[] optionalDependencies = new String[length];
        for (int i = 0; i < length; i++)
            optionalDependencies[i] = in.readUTF();

        length = in.readInt();
        String[] conflicts = new String[length];
        for (int i = 0; i < length; i++)
            conflicts[i] = in.readUTF();

        long creationDate = in.readLong();
        long lastUpdated = in.readLong();

        return new ModMetadata.ModMetadataBuilder()
                .id(id)
                .name(name)
                .version(version)
                .description(description)
                .author(author)
                .website(website)
                .engineVersion(engineVersion)
                .type(modType)
                .priority(priority)
                .tags(tags)
                .dependencies(dependencies)
                .optionalDependencies(optionalDependencies)
                .conflicts(conflicts)
                .creationDate(creationDate)
                .lastUpdated(lastUpdated)
                .build();
    }

    public abstract Optional<InputStream> getFile(String path);

    public abstract Optional<? extends AbstractRecord> getRecord(String recordId);

    public abstract boolean hasFile(String path);

    public abstract boolean hasRecord(String recordId);

    public Path getArchivePath() {
        return archivePath;
    }

    public Map<String, ?> getIndexTable(String tableId) {
        ArchiveIndexTable<?> index = indexTables.get(tableId);
        return index.getIndexTable();
    }

    public ArchiveIndexTable<GLAAssetEntry> getAssetIndex() {
        return (ArchiveIndexTable<GLAAssetEntry>) indexTables.get("asset");
    }

    public ArchiveIndexTable<GLARecordEntry> getRecordIndex() {
        return (ArchiveIndexTable<GLARecordEntry>) indexTables.get("record");
    }

    public Optional<ModMetadata> getMetadata() {
        return Optional.ofNullable(metadata);
    }

    @Override
    public void close() throws IOException {
        this.channel.close();
    }

}
