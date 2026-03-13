package com.duckcraftian.gildedlibrary.api.system.archive.base;

import com.duckcraftian.gildedlibrary.api.system.archive.ArchiveType;
import com.duckcraftian.gildedlibrary.api.system.archive.GLARecordEntry;
import com.duckcraftian.gildedlibrary.api.system.archive.GLArchiveWriter;
import com.duckcraftian.gildedlibrary.api.system.archive.asset.GLAAssetEntry;
import com.duckcraftian.gildedlibrary.api.system.mods.ModMetadata;
import com.duckcraftian.gildedlibrary.api.system.registries.RecordRegistry;
import com.duckcraftian.gildedlibrary.api.system.registries.RegistryManager;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A Base is a type of GLArchive that holds an asset index table as well as a record index table
 */
public class GLABaseWriter extends GLArchiveWriter {

    private final List<GLAAssetEntry> assetEntries = new ArrayList<>();
    private final List<GLARecordEntry> recordEntries = new ArrayList<>();
    private ModMetadata metadata;

    public GLABaseWriter(RegistryManager registryManager) {
        super(registryManager);
    }

    @Override
    protected Optional<ModMetadata> getMetadata() {
        return Optional.ofNullable(metadata);
    }

    protected void writeHeader(DataOutputStream out) throws IOException {
        super.writeHeader(out, ArchiveType.BASE_ARCHIVE);
        out.writeInt(assetEntries.size());
        out.writeInt(recordEntries.size());
    }

    public void write(ModMetadata metadata, List<Path> files, List<RecordRegistry<?>> registries, Path rootDirectory, Path outputPath) {
        this.metadata = metadata;
        List<byte[]> compressedAssetData = getCompressedAssets(files, rootDirectory, assetEntries);
        List<byte[]> compressedRecordData = getCompressedRecords(registries, recordEntries);

        int dataOffset = 16 + getMetadataSize(metadata);
        // Calculate Asset Index Table
        for (GLAAssetEntry entry : assetEntries) {
            dataOffset += 2 + entry.path().getBytes().length;
            dataOffset += 8 + 4 + 4;
        }

        // Calculate Record Index Table
        for (GLARecordEntry entry : recordEntries) {
            dataOffset += 2 + entry.id().getBytes().length;
            dataOffset += 2 + entry.recordType().getBytes(StandardCharsets.UTF_8).length;
            dataOffset += 8 + 4 + 4;
        }

        // Assign Asset Offsets
        long currentOffset = dataOffset;
        for (int i = 0; i < assetEntries.size(); i++) {
            GLAAssetEntry e = assetEntries.get(i);
            assetEntries.set(i, new GLAAssetEntry(e.path(), currentOffset, e.compressedSize(), e.originalSize()));
            currentOffset += e.compressedSize();
        }

        // Assign Record Offsets
        long totalAssetDataSize = assetEntries.stream().mapToLong(GLAAssetEntry::compressedSize).sum();
        currentOffset = dataOffset + totalAssetDataSize;
        for (int i = 0; i < recordEntries.size(); i++) {
            GLARecordEntry r = recordEntries.get(i);
            recordEntries.set(i, new GLARecordEntry(r.id(), r.recordType(), currentOffset, r.compressedSize(), r.originalSize()));
            currentOffset += r.compressedSize();
        }

        // Write GLB Archive
        try (DataOutputStream out = new DataOutputStream(Files.newOutputStream(outputPath))) {

            // Write Header
            writeHeader(out);

            // Write Mod Metadata
            writeModMetadata(out, metadata);

            // Write Asset Index Table
            for (GLAAssetEntry entry : assetEntries)
                writeGLAAssetEntry(out, entry);

            // Write Record Index Table
            for (GLARecordEntry entry : recordEntries)
                writeGLARecordEntry(out, entry);

            // Write Compressed Asset Data
            for (byte[] compressedData : compressedAssetData)
                out.write(compressedData);

            // Write Compressed Record Data
            for (byte[] compressedData : compressedRecordData)
                out.write(compressedData);

            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}
