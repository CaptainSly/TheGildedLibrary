package com.duckcraftian.gildedlibrary.api.system.archive.asset;

import com.duckcraftian.gildedlibrary.api.system.archive.ArchiveType;
import com.duckcraftian.gildedlibrary.api.system.archive.GLArchiveWriter;
import com.duckcraftian.gildedlibrary.api.system.mods.ModMetadata;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GLAAssetWriter extends GLArchiveWriter {

    private final List<GLAAssetEntry> entries = new ArrayList<>();

    public GLAAssetWriter() {
        super(null); // GLAsset Writer Doesn't need a RegistryManager (it only deals with assets)
    }

    protected void writeHeader(DataOutputStream out) throws IOException {
        super.writeHeader(out, ArchiveType.ASSET_ARCHIVE);
        out.writeInt(entries.size());
    }

    public void write(List<Path> files, Path rootDirectory, Path outputPath) {
        List<byte[]> compressedData = getCompressedAssets(files, rootDirectory, entries);

        int dataOffset = 4 + 4 + 4; // Magic + Version + entry Count
        for (GLAAssetEntry entry : entries) {
            dataOffset += 2 + entry.path().getBytes().length; // UTF Length prefix + bytes
            dataOffset += 8 + 4 + 4; // offset + compressedSize + originalSize
        }

        long currentOffset = dataOffset;
        for (int i = 0; i < entries.size(); i++) {
            GLAAssetEntry e = entries.get(i);
            entries.set(i, new GLAAssetEntry(e.path(), currentOffset, e.compressedSize(), e.originalSize()));
            currentOffset += e.compressedSize();
        }

        // Write GLA Archive
        try (DataOutputStream out = new DataOutputStream(Files.newOutputStream(outputPath))) {

            // Write Header
            writeHeader(out);

            // Write Index Table
            for (GLAAssetEntry entry : entries) {
                writeGLAAssetEntry(out, entry);
            }

            // Write Compressed Data
            for (byte[] compressed : compressedData) {
                out.write(compressed);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected Optional<ModMetadata> getMetadata() {
        return Optional.empty();
    }
}
