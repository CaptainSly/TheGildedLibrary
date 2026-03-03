package com.duckcraftian.gildedlibrary.api.system.archive;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.zstd.ZSTDAllocFunction;
import org.lwjgl.util.zstd.Zstd;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GLAWriter {

    private static final int VERSION = 1;

    private static final byte[] MAGIC_ASSET = {'T', 'G', 'L', 'A'};
    private static final byte[] MAGIC_MOD = {'T', 'G', 'L', 'P'};
    private static final byte[] MAGIC_MASTER = {'T', 'G', 'L', 'M'};

    public enum ArchiveType {
        ASSET_ARCHIVE, MOD_ARCHIVE, MASTER_ARCHIVE;

        public byte[] getMagicByte() {
            return switch (this) {
                case ASSET_ARCHIVE -> MAGIC_ASSET;
                case MOD_ARCHIVE -> MAGIC_MOD;
                case MASTER_ARCHIVE -> MAGIC_MASTER;
            };
        }

        public static ArchiveType typeFromMagic(byte[] magicByte) {
            if (Arrays.equals(magicByte, MAGIC_ASSET))
                return ASSET_ARCHIVE;
            else if (Arrays.equals(magicByte, MAGIC_MOD))
                return MOD_ARCHIVE;
            else if (Arrays.equals(magicByte, MAGIC_MASTER))
                return MASTER_ARCHIVE;

            return null;
        };

    }

    public static void write(List<Path> files, Path rootDirectory, Path outputPath, ArchiveType archiveType) {
        List<GLAEntry> entries = new ArrayList<>();
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
                    entries.add(new GLAEntry(relativePath, 0, compressed.length, raw.length));
                    compressedData.add(compressed);

                } finally {
                    MemoryUtil.memFree(srcBuffer);
                    if (dstBuffer != null) MemoryUtil.memFree(dstBuffer);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        int dataOffset = 4 + 4 + 4; // Magic + Version + entry Count
        for (GLAEntry entry : entries) {
            dataOffset += 2 + entry.path().getBytes().length; // UTF Length prefix + bytes
            dataOffset += 8 + 4 + 4; // offset + compressedSize + originalSize
        }

        long currentOffset = dataOffset;
        for (int i = 0; i < entries.size(); i++) {
            GLAEntry e = entries.get(i);
            entries.set(i, new GLAEntry(e.path(), currentOffset, e.compressedSize(), e.originalSize()));
            currentOffset += e.compressedSize();
        }

        // Write GLA Archive
        try (DataOutputStream out = new DataOutputStream(Files.newOutputStream(outputPath))) {

            // Write Header
            out.write(archiveType.getMagicByte());
            out.writeInt(VERSION);
            out.writeInt(entries.size());

            // Write Index Table
            for (GLAEntry entry : entries) {
                out.writeUTF(entry.path());
                out.writeLong(entry.offset());
                out.writeInt(entry.compressedSize());
                out.writeInt(entry.originalSize());
            }

            // Write Compressed Data
            for (byte[] compressed : compressedData) {
                out.write(compressed);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
