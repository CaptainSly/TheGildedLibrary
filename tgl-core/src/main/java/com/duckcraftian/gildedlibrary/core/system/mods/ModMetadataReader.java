package com.duckcraftian.gildedlibrary.core.system.mods;

import com.duckcraftian.gildedlibrary.api.system.mods.ModMetadata;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModMetadataReader {

    public ModMetadata read(Path path) {

        try (DataInputStream in = new DataInputStream(new BufferedInputStream(Files.newInputStream(path)))) {
            String id = in.readUTF();
            String name = in.readUTF();
            String version = in.readUTF();
            String description = in.readUTF();
            String author = in.readUTF();
            String website = in.readUTF();
            String engineVersion = in.readUTF();
            String type = in.readUTF();

            int priority = in.readInt();

            int listLength = in.readInt();
            String[] tags = new String[listLength];
            for (int i = 0; i < listLength; i++)
                tags[i] = in.readUTF();

            listLength = in.readInt();
            String[] dependencies = new String[listLength];
            for (int i = 0; i < listLength; i++)
                dependencies[i] = in.readUTF();

            listLength = in.readInt();
            String[] optionalDependencies = new String[listLength];
            for (int i = 0; i < listLength; i++)
                optionalDependencies[i] = in.readUTF();

            listLength = in.readInt();
            String[] conflicts = new String[listLength];
            for (int i = 0; i < listLength; i++)
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
                    .type(ModMetadata.ModType.getType(type))
                    .priority(priority)
                    .tags(tags)
                    .dependencies(dependencies)
                    .optionalDependencies(optionalDependencies)
                    .conflicts(conflicts)
                    .creationDate(creationDate)
                    .lastUpdated(lastUpdated)
                    .build();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
