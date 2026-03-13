package com.duckcraftian.gildedlibrary.api.system.archive;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ArchiveIndexTable<T> {

    private final Map<String, Map<String, T>> indexTable;

    public ArchiveIndexTable() {
        this.indexTable = new HashMap<>();
    }

    public Optional<Map<String, T>> getRegistryTable(String registryId) {
        return Optional.ofNullable(indexTable.get(registryId));
    }

    public void addItem(String registryId, String itemId, T item) {
        indexTable.computeIfAbsent(registryId, k -> new HashMap<>()).put(itemId, item);
    }

    public Optional<T> getItem(String registryId, String itemId) {
        if (indexTable.containsKey(registryId)) {
            Map<String, T> itemTable = indexTable.get(registryId);
            return Optional.ofNullable(itemTable.get(itemId));
        }

        return Optional.empty();
    }

    public Map<String, Map<String, T>> getIndexTable() {
        return indexTable;
    }

}
