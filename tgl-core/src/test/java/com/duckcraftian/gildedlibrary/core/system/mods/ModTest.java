package com.duckcraftian.gildedlibrary.core.system.mods;

import com.duckcraftian.gildedlibrary.api.system.mods.ModMetadata;
import com.duckcraftian.gildedlibrary.core.TGL;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModTest {


    @Test
    void testMetadataReadAndWrite() {
        ModMetadata metadata = new ModMetadata.ModMetadataBuilder()
                .id("arcanumoria")
                .name("Arcanumoria")
                .version("1.0.0")
                .description("The Master File for all of the games in the Arcanumoria Series")
                .author("Azraein, ArbitersVail")
                .website("reignleif.com")
                .engineVersion(TGL.getVersion())
                .type(ModMetadata.ModType.MASTER)
                .priority(0)
                .tags("weapon", "item", "armor", "clothing", "npc", "script")
                .creationDate(System.currentTimeMillis())
                .lastUpdated(System.currentTimeMillis())
                .build();

        File metadataFile = new File("build/" + metadata.getId() + ".glmetadata");
        ModMetadataWriter writer = new ModMetadataWriter();
        ModMetadataReader reader = new ModMetadataReader();

        writer.write(metadata, metadataFile.toPath());
        ModMetadata readMetadata = reader.read(metadataFile.toPath());

        assertEquals(metadata.getId(), readMetadata.getId());
        assertEquals(metadata.getName(), readMetadata.getName());
        assertEquals(metadata.getVersion(), readMetadata.getVersion());
        assertEquals(metadata.getDescription(), readMetadata.getDescription());
        assertEquals(metadata.getAuthor(), readMetadata.getAuthor());
        assertEquals(metadata.getWebsite(), readMetadata.getWebsite());
        assertEquals(metadata.getEngineVersion(), readMetadata.getEngineVersion());
        assertEquals(metadata.getType(), readMetadata.getType());
        assertEquals(metadata.getPriority(), readMetadata.getPriority());

        for (int i = 0; i < metadata.getTags().length; i++)
            assertEquals(metadata.getTags()[i], readMetadata.getTags()[i]);

        assertEquals(metadata.getCreationDate(), readMetadata.getCreationDate());
        assertEquals(metadata.getLastUpdated(), readMetadata.getLastUpdated());
    }

}
