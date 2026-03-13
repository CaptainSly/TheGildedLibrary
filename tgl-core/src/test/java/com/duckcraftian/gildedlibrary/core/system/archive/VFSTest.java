package com.duckcraftian.gildedlibrary.core.system.archive;

import com.duckcraftian.gildedlibrary.api.system.archive.asset.GLAAssetReader;
import com.duckcraftian.gildedlibrary.core.system.vfs.ArchiveMount;
import com.duckcraftian.gildedlibrary.core.system.vfs.VirtualFileSystem;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class VFSTest {

    private final Path output = Path.of(System.getProperty("user.dir"), "build", "test_archive.gla");
    private final String[] subDirs = {
            "textures", "models", "shaders", "scripts"
    };

    @Test
    void testVFSFilePresent() {
        VirtualFileSystem vfs = new VirtualFileSystem();
        try {
            vfs.addMount(new ArchiveMount("arcanumoria", "base", 0, new GLAAssetReader(output)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < 5; i++)
            for (String dir : subDirs)
                assertTrue(vfs.exists(dir + "/doc_" + (i + 1) + ".txt"));

        Optional<InputStream> file = vfs.resolve("textures/doc_2.txt");
        assertTrue(file.isPresent());
    }

}
