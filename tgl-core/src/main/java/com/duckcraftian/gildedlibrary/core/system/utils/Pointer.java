package com.duckcraftian.gildedlibrary.core.system.utils;

public record Pointer(long address) {

    public boolean isNull() {
        return address == 0;
    }

}
