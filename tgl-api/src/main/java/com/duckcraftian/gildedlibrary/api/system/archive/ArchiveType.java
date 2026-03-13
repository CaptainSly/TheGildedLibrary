package com.duckcraftian.gildedlibrary.api.system.archive;

import java.util.Arrays;

public enum ArchiveType {
    ASSET_ARCHIVE, MOD_ARCHIVE, BASE_ARCHIVE;

    public byte[] getMagicByte() {
        return switch (this) {
            case ASSET_ARCHIVE -> GLArchiveWriter.MAGIC_ASSET;
            case MOD_ARCHIVE -> GLArchiveWriter.MAGIC_MOD;
            case BASE_ARCHIVE -> GLArchiveWriter.MAGIC_BASE;
        };
    }

    public static ArchiveType typeFromMagic(byte[] magicByte) {
        if (Arrays.equals(magicByte, GLArchiveWriter.MAGIC_ASSET)) return ASSET_ARCHIVE;
        else if (Arrays.equals(magicByte, GLArchiveWriter.MAGIC_MOD)) return MOD_ARCHIVE;
        else if (Arrays.equals(magicByte, GLArchiveWriter.MAGIC_BASE)) return BASE_ARCHIVE;

        return null;
    }

}