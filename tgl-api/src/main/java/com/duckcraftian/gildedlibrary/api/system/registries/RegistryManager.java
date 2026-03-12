package com.duckcraftian.gildedlibrary.api.system.registries;

import com.duckcraftian.gildedlibrary.api.system.serialization.AbstractRecordSerializer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RegistryManager {

    private final Map<String, RecordRegistry<?>> recordRegistries;
    private final Map<String, AbstractRecordSerializer<?>> serializerRegistries;

    public RegistryManager() {
        recordRegistries = new HashMap<>();
        serializerRegistries = new HashMap<>();
    }

    public void addRecordRegistry(String registryType, RecordRegistry<?> recordRegistry) {
        recordRegistries.put(registryType, recordRegistry);
    }

    public void addSerializerRegistry(String registryType, AbstractRecordSerializer<?> serializer) {
        serializerRegistries.put(registryType, serializer);
    }

    public void removeRecordRegistry(String registryType) {
        recordRegistries.remove(registryType);
    }

    public void removeSerializerRegistry(String registryType) {
        serializerRegistries.remove(registryType);
    }

    public boolean containsRecordRegistry(String registryType) {
        return recordRegistries.containsKey(registryType);
    }

    public boolean containsSerializerRegistry(String registryType) {
        return serializerRegistries.containsKey(registryType);
    }

    public Optional<RecordRegistry<?>> getRecordRegistry(String registryType) {
        return Optional.ofNullable(recordRegistries.get(registryType));
    }

    public Optional<AbstractRecordSerializer<?>> getSerializerRegistry(String registryType) {
        return Optional.ofNullable(serializerRegistries.get(registryType));
    }

    public Map<String, RecordRegistry<?>> getRecordRegistries() {
        return Collections.unmodifiableMap(recordRegistries);
    }

    public Map<String, AbstractRecordSerializer<?>> getSerializerRegistries() {
        return Collections.unmodifiableMap(serializerRegistries);
    }

    public Map<String, RecordRegistry<?>> copyRegistries() {
        Map<String, RecordRegistry<?>> copy = new HashMap<>();
        copy.putAll(recordRegistries);
        return copy;
    }

}
