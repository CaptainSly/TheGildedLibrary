package com.duckcraftian.gildedlibrary.api.system.registries;

import com.duckcraftian.gildedlibrary.api.system.AbstractRecord;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Registry<R extends AbstractRecord> {

    private final HashMap<String, R> registry;

    public Registry() {
        registry = new HashMap<>();
    }

    public void addRecord(R record) {
        registry.put(record.getId(), record);
    }

    public void removeRecord(String recordId) {
        registry.remove(recordId);
    }

    public Optional<R> resolve(String recordId) {
        return Optional.ofNullable(registry.get(recordId));
    }

    public Map<String, R> getRegistry() {
        return Collections.unmodifiableMap(registry);
    }

}
