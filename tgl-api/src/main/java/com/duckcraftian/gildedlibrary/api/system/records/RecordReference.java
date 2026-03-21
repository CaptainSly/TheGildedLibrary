package com.duckcraftian.gildedlibrary.api.system.records;

import com.duckcraftian.gildedlibrary.api.system.registries.RecordRegistry;

import java.util.Optional;

public record RecordReference<T extends AbstractRecord>(String id) {

    public Optional<T> resolve(RecordRegistry<? extends T> recordRegistry) {
        return (Optional<T>) recordRegistry.resolve(id);
    }
}
