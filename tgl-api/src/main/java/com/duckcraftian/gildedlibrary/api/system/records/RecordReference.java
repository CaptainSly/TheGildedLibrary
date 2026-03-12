package com.duckcraftian.gildedlibrary.api.system.records;

import com.duckcraftian.gildedlibrary.api.system.registries.RecordRegistry;

import java.util.Optional;

public record RecordReference<T extends AbstractRecord>(String id) {

    public Optional<T> resolve(RecordRegistry<T> recordRegistry) {
        return recordRegistry.resolve(id);
    }
}
