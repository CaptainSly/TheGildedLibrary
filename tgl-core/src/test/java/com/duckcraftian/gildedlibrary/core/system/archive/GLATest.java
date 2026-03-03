package com.duckcraftian.gildedlibrary.core.system.archive;

import com.duckcraftian.gildedlibrary.api.system.archive.GLAEntry;
import com.duckcraftian.gildedlibrary.api.system.archive.GLAReader;
import com.duckcraftian.gildedlibrary.api.system.archive.GLAWriter;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GLATest {

    private final Path output = Path.of(System.getProperty("user.dir"), "build", "test_archive.gla");

    @Test
    void testGLAWrite() {
        Path pluginDir = Path.of(System.getProperty("user.dir"), "build");

        List<Path> files = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            File test = new File(pluginDir.toString() + "/file_" + i + ".txt");
            try {
                if (test.createNewFile())
                    files.add(test.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        GLAWriter.write(files, pluginDir, output, GLAWriter.ArchiveType.ASSET_ARCHIVE);
        assertTrue(Files.exists(output));

        GLAReader reader = new GLAReader(output);
        assertTrue(reader.hasFile("file_0.txt"));
        assertTrue(reader.hasFile("file_1.txt"));
        assertTrue(reader.hasFile("file_2.txt"));
        assertTrue(reader.hasFile("file_3.txt"));
        assertTrue(reader.hasFile("file_4.txt"));

    }

}
