package com.duckcraftian.gildedlibrary.api.system.vfs;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public interface IVFSMount {

    String getModId();

    List<String> listFiles(String directory);

    List<String> listFilesRecursive(String directory);

    Optional<InputStream> getFile(String path);

    boolean hasFile(String path);
}
