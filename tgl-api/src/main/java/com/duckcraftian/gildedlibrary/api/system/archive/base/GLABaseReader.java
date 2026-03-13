package com.duckcraftian.gildedlibrary.api.system.archive.base;

import com.duckcraftian.gildedlibrary.api.system.archive.*;
import com.duckcraftian.gildedlibrary.api.system.archive.asset.GLAAssetEntry;
import com.duckcraftian.gildedlibrary.api.system.mods.ModMetadata;
import com.duckcraftian.gildedlibrary.api.system.records.AbstractRecord;
import com.duckcraftian.gildedlibrary.api.system.registries.RegistryManager;
import com.duckcraftian.gildedlibrary.api.system.serialization.AbstractRecordSerializer;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.zstd.Zstd;
import org.tinylog.Logger;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class GLABaseReader extends GLArchiveReader {

    public GLABaseReader(Path archivePath, RegistryManager registryManager) throws IOException {
        super(archivePath, registryManager);
        this.indexTables.put("asset", new ArchiveIndexTable<GLAAssetEntry>());
        this.indexTables.put("record", new ArchiveIndexTable<GLARecordEntry>());
        archiveType = readIndex();
    }

    protected ArchiveType readIndex() {
        try (DataInputStream in = new DataInputStream(new BufferedInputStream(Files.newInputStream(archivePath)))) {
            byte[] magic = in.readNBytes(4); // 4 byte Magic Bytes
            var type = ArchiveType.typeFromMagic(magic);

            if (type == null) throw new RuntimeException("Invalid Archive Magic Bytes");

            int version = in.readInt();

            if (version != GLArchiveWriter.VERSION)
                Logger.warn("Using an incompatible version of a Base GLArchive");

            int assetEntryCount = in.readInt();
            int recordEntryCount = in.readInt();

            // Modmetadata Read
            this.metadata = readMetadata(in);

            // Assets Index Table first
            for (int i = 0; i < assetEntryCount; i++) {
                String path = in.readUTF();
                long offset = in.readLong();
                int compressedSize = in.readInt();
                int originalSize = in.readInt();

                String[] pathSplit = path.split("/");
                getAssetIndex().addItem(pathSplit[0], path, new GLAAssetEntry(path, offset, compressedSize, originalSize));
            }

            // Record Index Table Next
            for (int i = 0; i < recordEntryCount; i++) {
                String id = in.readUTF();
                String recordType = in.readUTF();
                long offset = in.readLong();
                int compressedSize = in.readInt();
                int originalSize = in.readInt();
                getRecordIndex().addItem(recordType, id, new GLARecordEntry(id, recordType, offset, compressedSize, originalSize));
            }

            return type;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean hasFile(String path) {
        String[] entryPath = path.split("/");
        return getAssetIndex().getItem(entryPath[0], path).isPresent();
    }

    public boolean hasRecord(String recordId) {
        return getRecordIndex().getIndexTable().values().stream().anyMatch(table -> table.containsKey(recordId));
    }

    public Optional<? extends AbstractRecord> getRecord(String recordId) {
        if (!hasRecord(recordId)) return Optional.empty();
        GLARecordEntry entry = getRecordIndex().getIndexTable().values().stream().filter(table -> table.containsKey(recordId)).findFirst().map(table -> table.get(recordId)).orElse(null);

        ByteBuffer compressedBuffer = ByteBuffer.allocateDirect(entry.compressedSize());
        ByteBuffer dstBuffer = null;
        try {
            var bytesRead = 0;
            while (bytesRead < entry.compressedSize())
                bytesRead += channel.read(compressedBuffer, entry.offset() + bytesRead);

            compressedBuffer.flip();
            dstBuffer = MemoryUtil.memAlloc(entry.originalSize());

            long decompressedSize = Zstd.ZSTD_decompress(dstBuffer, compressedBuffer);
            byte[] decompressed = new byte[(int) decompressedSize];
            dstBuffer.get(decompressed);

            var serializerOpt = registryManager.getSerializerRegistry(entry.recordType());
            if (serializerOpt.isEmpty()) return Optional.empty();

            AbstractRecordSerializer<?> recordSerializer = serializerOpt.get();
            return Optional.of(recordSerializer.read(decompressed));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (dstBuffer != null) MemoryUtil.memFree(dstBuffer);
        }

    }

    public Optional<InputStream> getFile(String path) {
        if (!hasFile(path)) return Optional.empty();
        String[] entryPath = path.split("/");
        GLAAssetEntry entry = getAssetIndex().getItem(entryPath[0], path).get();


        ByteBuffer compressedBuffer = ByteBuffer.allocateDirect(entry.compressedSize());
        ByteBuffer dstBuffer = null;
        try {
            var bytesRead = 0;
            while (bytesRead < entry.compressedSize())
                bytesRead += channel.read(compressedBuffer, entry.offset() + bytesRead);

            compressedBuffer.flip();
            dstBuffer = MemoryUtil.memAlloc(entry.originalSize());

            long decompressedSize = Zstd.ZSTD_decompress(dstBuffer, compressedBuffer);
            byte[] decompressed = new byte[(int) decompressedSize];
            dstBuffer.get(decompressed);

            return Optional.of(new ByteArrayInputStream(decompressed));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (dstBuffer != null) MemoryUtil.memFree(dstBuffer);
        }
    }
}
