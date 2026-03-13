package com.duckcraftian.gildedlibrary.api.system.archive.asset;

import com.duckcraftian.gildedlibrary.api.system.archive.ArchiveIndexTable;
import com.duckcraftian.gildedlibrary.api.system.archive.ArchiveType;
import com.duckcraftian.gildedlibrary.api.system.archive.GLArchiveReader;
import com.duckcraftian.gildedlibrary.api.system.archive.GLArchiveWriter;
import com.duckcraftian.gildedlibrary.api.system.records.AbstractRecord;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.zstd.Zstd;
import org.tinylog.Logger;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class GLAAssetReader extends GLArchiveReader {

    public static final String[] ASSET_DIRECTORIES = {
            "models", "scripts", "textures", "audio",
    };

    public GLAAssetReader(Path archivePath) throws IOException {
        super(archivePath, null); // GLAAssetReader doesn't need a RegistryManager
        this.indexTables.put("asset", new ArchiveIndexTable<GLAAssetEntry>());
        archiveType = readIndex();
    }

    protected ArchiveType readIndex() {
        try (DataInputStream in = new DataInputStream(new BufferedInputStream(Files.newInputStream(archivePath)))) {
            byte[] magic = in.readNBytes(4); // 4 byte Magic Bytes
            var type = ArchiveType.typeFromMagic(magic);

            if (type == null) throw new RuntimeException("Invalid Archive Magic Bytes");

            int version = in.readInt();

            if (version != GLArchiveWriter.VERSION)
                Logger.warn("Using an incompatible version of a GLAsset Archive");

            int entryCount = in.readInt();

            for (int i = 0; i < entryCount; i++) {
                String path = in.readUTF();
                long offset = in.readLong();
                int compressedSize = in.readInt();
                int originalSize = in.readInt();

                String[] pathSplit = path.split("/");
                getAssetIndex().addItem(pathSplit[0], path, new GLAAssetEntry(path, offset, compressedSize, originalSize));
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

    @Override
    public boolean hasRecord(String recordId) {
        return false;
    }

    public Optional<InputStream> getFile(String path) {
        if (!hasFile(path)) return Optional.empty();
        String[] entryPath = path.split("/");
        GLAAssetEntry entry = getIndex().getItem(entryPath[0], path).get();

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

    @Override
    public Optional<? extends AbstractRecord> getRecord(String recordId) {
        return Optional.empty();
    }

    public ArchiveIndexTable<GLAAssetEntry> getIndex() {
        return getAssetIndex();
    }

}
