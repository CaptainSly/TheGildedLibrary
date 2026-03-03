package com.duckcraftian.gildedlibrary.api.system.archive;

public record GLAEntry(String path, long offset, int compressedSize, int originalSize) {

}
