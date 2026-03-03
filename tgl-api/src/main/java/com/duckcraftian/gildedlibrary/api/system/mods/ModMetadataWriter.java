package com.duckcraftian.gildedlibrary.api.system.mods;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModMetadataWriter {

    public void write(ModMetadata metadata, Path path) {
        try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(Files.newOutputStream(path)))) {
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
            for (int i = 0; i < metadata.getTags().length; i++)
                out.writeUTF(metadata.getTags()[i]);

            out.writeInt(metadata.getDependencies().length);
            for (int i = 0; i < metadata.getDependencies().length; i++)
                out.writeUTF(metadata.getDependencies()[i]);

            out.writeInt(metadata.getOptionalDependencies().length);
            for (int i = 0; i < metadata.getOptionalDependencies().length; i++)
                out.writeUTF(metadata.getOptionalDependencies()[i]);

            out.writeInt(metadata.getConflicts().length);
            for (int i = 0; i < metadata.getConflicts().length; i++)
                out.writeUTF(metadata.getConflicts()[i]);

            out.writeLong(metadata.getCreationDate());
            out.writeLong(metadata.getLastUpdated());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
