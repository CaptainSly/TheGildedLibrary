package com.duckcraftian.gildedlibrary.core;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TGL {

    public static final String MAJOR = "0";
    public static final String MINOR = "1";
    public static final String BUGFIX = "0";
    public static final String CODENAME = "Damascus";

    public static String getVersion() {
        return String.format("%s.%s.%s-%s", MAJOR, MINOR, BUGFIX, CODENAME);
    }

    // File Constants
    public static final String ENGINE_FOLDER = "gildedlibrary/";
    public static final String[] ENGINE_FOLDERS = {
            "data", "config", "saves", "libs", "mods", "plugins"
    };

    public static final Path ENGINE_PATH = Path.of(System.getProperty("user.dir"), ENGINE_FOLDER);

    public static void createDataFolder() {
        File engineFolder = ENGINE_PATH.toFile();
        engineFolder.mkdir();

        for (String dir : ENGINE_FOLDERS)
            getEngineFolder(dir).toFile().mkdirs();
    }

    public static Path getEngineFolder(String path) {
        return Path.of(ENGINE_PATH.toString(), path);
    }

}
