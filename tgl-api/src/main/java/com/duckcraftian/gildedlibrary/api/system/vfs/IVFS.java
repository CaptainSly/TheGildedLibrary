package com.duckcraftian.gildedlibrary.api.system.vfs;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public interface IVFS {

    Optional<InputStream> resolve(String path);
    boolean exists(String path);
    List<String> listFiles(String directory);

}
