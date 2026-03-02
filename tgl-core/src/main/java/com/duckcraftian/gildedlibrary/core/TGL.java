package com.duckcraftian.gildedlibrary.core;

public class TGL {

    public static final String MAJOR = "0";
    public static final String MINOR = "1";
    public static final String BUGFIX = "0";
    public static final String CODENAME = "Damascus";

    public static String getVersion() {
        return String.format("%s.%s.%s-%s", MAJOR, MINOR, BUGFIX, CODENAME);
    }

}
