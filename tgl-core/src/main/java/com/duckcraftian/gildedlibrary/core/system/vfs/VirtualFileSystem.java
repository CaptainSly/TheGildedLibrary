package com.duckcraftian.gildedlibrary.core.system.vfs;

import com.duckcraftian.gildedlibrary.api.system.vfs.AbstractVFSMount;
import com.duckcraftian.gildedlibrary.api.system.vfs.IVFS;

import java.io.InputStream;
import java.util.*;

public class VirtualFileSystem implements IVFS {

    private final List<LooseFolderMount> looseMounts;
    private final List<ArchiveMount> archiveMounts;

    public VirtualFileSystem() {
        looseMounts = new ArrayList<>();
        archiveMounts = new ArrayList<>();
    }

    @Override
    public Optional<InputStream> resolve(String path) {
        for (LooseFolderMount mount : looseMounts)
            if (mount.hasFile(path))
                return mount.getFile(path);

        for (ArchiveMount mount : archiveMounts)
            if (mount.hasFile(path))
                return mount.getFile(path);

        return Optional.empty();
    }

    @Override
    public boolean exists(String path) {
        for (LooseFolderMount mount : looseMounts)
            if (mount.hasFile(path))
                return true;

        for (ArchiveMount mount : archiveMounts)
            if (mount.hasFile(path))
                return true;

        return false;
    }

    public void addMount(AbstractVFSMount mount) {
        if (mount instanceof LooseFolderMount looseFolder)
            looseMounts.add(looseFolder);
        else if (mount instanceof ArchiveMount archive)
            archiveMounts.add(archive);
    }

    @Override
    public List<String> listFiles(String directory) {
        Set<String> results = new LinkedHashSet<>();

        for (ArchiveMount mount : archiveMounts)
            results.addAll(mount.listFiles(directory));

        for (LooseFolderMount mount : looseMounts)
            results.addAll(mount.listFiles(directory));

        return results.stream().toList();
    }

    @Override
    public List<String> listFilesRecursive(String directory) {
        Set<String> results = new LinkedHashSet<>();

        for (ArchiveMount mount : archiveMounts)
            results.addAll(mount.listFilesRecursive(directory));

        for (LooseFolderMount mount : looseMounts)
            results.addAll(mount.listFilesRecursive(directory));

        return results.stream().toList();
    }

    public List<LooseFolderMount> getLooseMounts() {
        return looseMounts;
    }

    public List<ArchiveMount> getArchiveMounts() {
        return archiveMounts;
    }
}
