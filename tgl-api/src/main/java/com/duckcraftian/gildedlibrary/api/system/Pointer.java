package com.duckcraftian.gildedlibrary.api.system;

public record Pointer(long address) {

    public boolean isNull() {
        return address == 0;
    }

}
