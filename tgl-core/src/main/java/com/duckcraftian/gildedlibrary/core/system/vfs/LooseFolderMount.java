package com.duckcraftian.gildedlibrary.core.system.vfs;

import com.duckcraftian.gildedlibrary.api.system.vfs.AbstractVFSMount;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class LooseFolderMount extends AbstractVFSMount {

    private final Path looseFolder;

    public LooseFolderMount(String modId, String mountId, int priority, Path looseFolder) {
        super(modId, mountId, priority);
        this.looseFolder = looseFolder;
    }

    public Path getLooseFolder() {
        return looseFolder;
    }

    @Override
    public List<String> listFiles(String directory) {
        Path dirPath = looseFolder.resolve(directory);
        try (Stream<Path> files = Files.list(dirPath)) {
            return files.map(p -> looseFolder.relativize(p).toString()).toList();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<String> listFilesRecursive(String directory) {
        Path dirPath = looseFolder.resolve(directory);
        try (Stream<Path> files = Files.walk(dirPath)) {
            return files.map(p -> looseFolder.relativize(p).toString()).toList();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<InputStream> getFile(String path) {
        Path filePath = looseFolder.resolve(path);
        if (!Files.exists(filePath)) return Optional.empty();
        try {
            return Optional.of(Files.newInputStream(filePath));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean hasFile(String path) {
        return Files.exists(looseFolder.resolve(path));
    }
}
