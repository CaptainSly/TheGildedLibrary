package com.duckcraftian.gildedlibrary.api.system.archive;

import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.zstd.Zstd;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GLAReader {

    private final Path archivePath;
    private final Map<String, GLAEntry> index;
    private final GLAWriter.ArchiveType archiveType;

    public GLAReader(Path archivePath) {
        this.archivePath = archivePath;
        index = new HashMap<>();
        archiveType = readIndex();
    }

    private GLAWriter.ArchiveType readIndex() {
        try (DataInputStream in = new DataInputStream(new BufferedInputStream(Files.newInputStream(archivePath)))) {
            byte[] magic = in.readNBytes(4); // 4 byte Magic Bytes
            var type = GLAWriter.ArchiveType.typeFromMagic(magic);

            if (type == null) throw new RuntimeException("Invalid Archive Magic Bytes");

            int version = in.readInt();
            int entryCount = in.readInt();

            for (int i = 0; i < entryCount; i++) {
                String path = in.readUTF();
                long offset = in.readLong();
                int compressedSize = in.readInt();
                int originalSize = in.readInt();
                index.put(path, new GLAEntry(path, offset, compressedSize, originalSize));
            }

            return type;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean hasFile(String path) {
        return index.containsKey(path);
    }

    public Optional<InputStream> getFile(String path) {
        if (!hasFile(path)) return Optional.empty();
        GLAEntry entry = index.get(path);

        byte[] compressed = new byte[entry.compressedSize()];
        try (RandomAccessFile raf = new RandomAccessFile(archivePath.toFile(), "r")) {
            raf.seek(entry.offset());
            raf.readFully(compressed);
        } catch (IOException e) {
            return Optional.empty();
        }

        ByteBuffer compressedBuffer = MemoryUtil.memAlloc(compressed.length);
        ByteBuffer dstBuffer = null;
        try {
            compressedBuffer.put(compressed).flip();
            dstBuffer = MemoryUtil.memAlloc(entry.originalSize());

            long decompressedSize = Zstd.ZSTD_decompress(dstBuffer, compressedBuffer);
            byte[] decompressed = new byte[(int) decompressedSize];
            dstBuffer.get(decompressed);

            return Optional.of(new ByteArrayInputStream(decompressed));
        } finally {
            MemoryUtil.memFree(compressedBuffer);
            if (dstBuffer != null) MemoryUtil.memFree(dstBuffer);
        }
    }

    public GLAWriter.ArchiveType getArchiveType() {
        return archiveType;
    }

    public Map<String, GLAEntry> getIndex() {
        return index;
    }

    public Path getArchivePath() {
        return archivePath;
    }
}
