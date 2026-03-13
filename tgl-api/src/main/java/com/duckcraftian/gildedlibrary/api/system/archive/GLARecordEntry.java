package com.duckcraftian.gildedlibrary.api.system.archive;

public record GLARecordEntry(String id, String recordType, long offset, int compressedSize, int originalSize) {
}

