package com.duckcraftian.gildedlibrary.api.system.archive;

import com.duckcraftian.gildedlibrary.api.system.archive.asset.GLAAssetEntry;
import com.duckcraftian.gildedlibrary.api.system.mods.ModMetadata;
import com.duckcraftian.gildedlibrary.api.system.records.AbstractRecord;
import com.duckcraftian.gildedlibrary.api.system.registries.RecordRegistry;
import com.duckcraftian.gildedlibrary.api.system.registries.RegistryManager;
import com.duckcraftian.gildedlibrary.api.system.serialization.AbstractRecordSerializer;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.zstd.Zstd;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class GLArchiveWriter {

    public static final int VERSION = 1;

    public static final byte[] MAGIC_ASSET = {'T', 'G', 'L', 'A'};
    public static final byte[] MAGIC_MOD = {'T', 'G', 'L', 'M'};
    public static final byte[] MAGIC_BASE = {'T', 'G', 'L', 'B'};


    protected final RegistryManager registryManager;

    public GLArchiveWriter(RegistryManager registryManager) {
        this.registryManager = registryManager;
    }

    protected void writeHeader(DataOutputStream out, ArchiveType archiveType) throws IOException {
        out.write(archiveType.getMagicByte());
        out.writeInt(VERSION);
    }

    protected void writeGLARecordEntry(DataOutputStream out, GLARecordEntry entry) throws IOException {
        out.writeUTF(entry.id());
        out.writeUTF(entry.recordType());
        out.writeLong(entry.offset());
        out.writeInt(entry.compressedSize());
        out.writeInt(entry.originalSize());
    }

    protected void writeGLAAssetEntry(DataOutputStream out, GLAAssetEntry entry) throws IOException {
        out.writeUTF(entry.path());
        out.writeLong(entry.offset());
        out.writeInt(entry.compressedSize());
        out.writeInt(entry.originalSize());
    }

    protected void writeModMetadata(DataOutputStream out, ModMetadata metadata) throws IOException {
        out.writeUTF(metadata.getId());
        out.writeUTF(metadata.getName());
        out.writeUTF(metadata.getVersion());
        out.writeUTF(metadata.getDescription());
        out.writeUTF(metadata.getAuthor());
        out.writeUTF(metadata.getWebsite());
        out.writeUTF(metadata.getEngineVersion());
        out.writeUTF(metadata.getType().name().toLowerCase());
        out.writeInt(metadata.getPriority());

        out.writeInt(metadata.getTags().length);
        for (String tag : metadata.getTags())
            out.writeUTF(tag);

        out.writeInt(metadata.getDependencies().length);
        for (String dependency : metadata.getDependencies())
            out.writeUTF(dependency);

        out.writeInt(metadata.getOptionalDependencies().length);
        for (String optionalDependency : metadata.getOptionalDependencies())
            out.writeUTF(optionalDependency);

        out.writeInt(metadata.getConflicts().length);
        for (String conflict : metadata.getConflicts())
            out.writeUTF(conflict);

        out.writeLong(metadata.getCreationDate());
        out.writeLong(metadata.getLastUpdated());
    }

    protected int getMetadataSize(ModMetadata modMetadata) {
        int dataOffset = 0;
        dataOffset += (8 * 2); // 8 String elements - 2Bytes.
        dataOffset += modMetadata.getId().getBytes(StandardCharsets.UTF_8).length;
        dataOffset += modMetadata.getName().getBytes(StandardCharsets.UTF_8).length;
        dataOffset += modMetadata.getVersion().getBytes(StandardCharsets.UTF_8).length;
        dataOffset += modMetadata.getDescription().getBytes(StandardCharsets.UTF_8).length;
        dataOffset += modMetadata.getAuthor().getBytes(StandardCharsets.UTF_8).length;
        dataOffset += modMetadata.getWebsite().getBytes(StandardCharsets.UTF_8).length;
        dataOffset += modMetadata.getEngineVersion().getBytes(StandardCharsets.UTF_8).length;
        dataOffset += modMetadata.getType().name().getBytes(StandardCharsets.UTF_8).length;
        dataOffset += 4;

        dataOffset += 4;
        for (int i = 0; i < modMetadata.getTags().length; i++) {
            dataOffset += 2;
            dataOffset += modMetadata.getTags()[i].getBytes(StandardCharsets.UTF_8).length;
        }

        dataOffset += 4;
        for (int i = 0; i < modMetadata.getDependencies().length; i++) {
            dataOffset += 2;
            dataOffset += modMetadata.getDependencies()[i].getBytes(StandardCharsets.UTF_8).length;
        }

        dataOffset += 4;
        for (int i = 0; i < modMetadata.getOptionalDependencies().length; i++) {
            dataOffset += 2;
            dataOffset += modMetadata.getOptionalDependencies()[i].getBytes(StandardCharsets.UTF_8).length;
        }

        dataOffset += 4;
        for (int i = 0; i < modMetadata.getConflicts().length; i++) {
            dataOffset += 2;
            dataOffset += modMetadata.getConflicts()[i].getBytes(StandardCharsets.UTF_8).length;
        }

        dataOffset += 8 + 8;
        return dataOffset;
    }

    protected abstract Optional<ModMetadata> getMetadata();

    protected List<byte[]> getCompressedRecords(List<RecordRegistry<?>> registries, List<GLARecordEntry> entries) {
        List<byte[]> compressedData = new ArrayList<>();
        for (RecordRegistry<? extends AbstractRecord> registry : registries) {
            String recordType = registry.getRecordType();
            Optional<AbstractRecordSerializer<?>> recordSerializer = registryManager.getSerializerRegistry(recordType);

            // Get All Records inside the registry
            for (AbstractRecord record : registry.getRegistry().values()) {// Get the Serializer that corresponds to this Registry
                recordSerializer.ifPresentOrElse(serializer -> {
                    byte[] rawBytes = serializeRecord(serializer, record);
                    ByteBuffer srcBuffer = MemoryUtil.memAlloc(rawBytes.length);
                    ByteBuffer dstBuffer = null;

                    try {
                        srcBuffer.put(rawBytes).flip();
                        long maxCompressedSize = Zstd.ZSTD_compressBound(rawBytes.length);
                        dstBuffer = MemoryUtil.memAlloc((int) maxCompressedSize);

                        long compressedSize = Zstd.ZSTD_compress(dstBuffer, srcBuffer, Zstd.ZSTD_defaultCLevel());
                        byte[] compressed = new byte[(int) compressedSize];
                        dstBuffer.get(compressed);
                        entries.add(new GLARecordEntry(record.getId(), record.getRecordType(), 0, compressed.length, rawBytes.length));
                        compressedData.add(compressed);
                    } finally {
                        MemoryUtil.memFree(srcBuffer);
                        if (dstBuffer != null) MemoryUtil.memFree(dstBuffer);
                    }

                }, () -> {
                    throw new RuntimeException("Could not find a Serializer for RecordType: " + recordType);
                });

            }
        }
        return compressedData;
    }

    private <R extends AbstractRecord> byte[] serializeRecord(
            AbstractRecordSerializer<R> serializer,
            AbstractRecord record
    ) {
        @SuppressWarnings("unchecked")
        R typed = (R) record;
        return serializer.write(typed);
    }

    protected List<byte[]> getCompressedAssets(List<Path> files, Path rootDirectory, List<GLAAssetEntry> entries) {
        List<byte[]> compressedData = new ArrayList<>();
        for (Path file : files) {
            try {
                byte[] raw = Files.readAllBytes(file);
                ByteBuffer srcBuffer = MemoryUtil.memAlloc(raw.length);
                ByteBuffer dstBuffer = null;
                try {
                    srcBuffer.put(raw).flip();
                    long maxCompressedSize = Zstd.ZSTD_compressBound(raw.length);
                    dstBuffer = MemoryUtil.memAlloc((int) maxCompressedSize);

                    long compressedSize = Zstd.ZSTD_compress(dstBuffer, srcBuffer, Zstd.ZSTD_defaultCLevel());
                    byte[] compressed = new byte[(int) compressedSize];
                    dstBuffer.get(compressed);

                    String relativePath = rootDirectory.relativize(file).toString();
                    entries.add(new GLAAssetEntry(relativePath, 0, compressed.length, raw.length));
                    compressedData.add(compressed);

                } finally {
                    MemoryUtil.memFree(srcBuffer);
                    if (dstBuffer != null) MemoryUtil.memFree(dstBuffer);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return compressedData;
    }

}
