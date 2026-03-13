package com.duckcraftian.gildedlibrary.api.system.archive.mod;

import com.duckcraftian.gildedlibrary.api.system.archive.ArchiveType;
import com.duckcraftian.gildedlibrary.api.system.archive.GLARecordEntry;
import com.duckcraftian.gildedlibrary.api.system.archive.GLArchiveWriter;
import com.duckcraftian.gildedlibrary.api.system.mods.ModMetadata;
import com.duckcraftian.gildedlibrary.api.system.registries.RecordRegistry;
import com.duckcraftian.gildedlibrary.api.system.registries.RegistryManager;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GLAModWriter extends GLArchiveWriter {

    private final List<GLARecordEntry> recordEntries = new ArrayList<>();
    private ModMetadata metadata;

    public GLAModWriter(RegistryManager registryManager) {
        super(registryManager);
    }

    @Override
    protected Optional<ModMetadata> getMetadata() {
        return Optional.ofNullable(metadata);
    }

    protected void writeHeader(DataOutputStream out) throws IOException {
        super.writeHeader(out, ArchiveType.MOD_ARCHIVE);
        out.writeInt(recordEntries.size());
    }

    public void write(ModMetadata metadata, List<RecordRegistry<?>> registries, Path output) {
        this.metadata = metadata;
        List<byte[]> compressedRecordData = getCompressedRecords(registries, recordEntries);

        int dataOffset = 12 + getMetadataSize(metadata);
        for (GLARecordEntry entry : recordEntries) {
            dataOffset += 2 + entry.id().getBytes().length;
            dataOffset += 2 + entry.recordType().getBytes().length;
            dataOffset += 8 + 4 + 4;
        }

        long currentOffset = dataOffset;
        for (int i = 0; i < recordEntries.size(); i++) {
            GLARecordEntry r = recordEntries.get(i);
            recordEntries.set(i, new GLARecordEntry(r.id(), r.recordType(), currentOffset, r.compressedSize(), r.originalSize()));
            currentOffset += r.compressedSize();
        }

        // Write GLM Archive
        try (DataOutputStream out = new DataOutputStream(Files.newOutputStream(output))) {

            // Write Header
            writeHeader(out);
            writeModMetadata(out, metadata);

            // Write Record Index Table
            for (GLARecordEntry entry : recordEntries)
                writeGLARecordEntry(out, entry);

            // Write Compressed Record Data
            for (byte[] compressedData : compressedRecordData)
                out.write(compressedData);

            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
