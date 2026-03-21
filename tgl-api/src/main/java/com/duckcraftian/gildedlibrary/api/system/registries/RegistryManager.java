package com.duckcraftian.gildedlibrary.api.system.registries;

import com.duckcraftian.gildedlibrary.api.system.serialization.AbstractRecordSerializer;

import java.util.*;

public class RegistryManager {

    private final Map<String, RecordRegistry<?>> recordRegistries;
    private final Map<String, AssetRegistry<?>> assetRegistries;
    private final Map<String, AbstractRecordSerializer<?>> serializerRegistries;

    private final List<String> loadedPlugins;
    private final List<String> loadedMods;

    public enum DefaultAssetTypes {
        SCRIPT("script"),
        MESH("mesh"),
        MODEL("model"),
        TEXTURE("texture"),
        LOD("lod"),

        ;

        String id;

        DefaultAssetTypes(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    public enum DefaultRecordTypes {
        ITEMS("items"),
        WEAPONS("weapons"),
        ARMORS("armors"),
        CLOTHING("clothing"),
        RACES("races"),
        NPCS("npcs"),
        CREATURES("creatures"),
        QUESTS("quests"),
        DIALOGUE("dialogue"),
        FACTIONS("factions"),
        SKILLS("skills"),
        ATTRIBUTES("attributes"),
        ABILITIES("abilities"),
        CONTAINERS("containers"),
        ;

        String id;

        DefaultRecordTypes(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }


    public RegistryManager() {
        assetRegistries = new HashMap<>();
        recordRegistries = new HashMap<>();
        serializerRegistries = new HashMap<>();

        loadedPlugins = new ArrayList<>();
        loadedMods = new ArrayList<>();
    }

    public void addRecordRegistry(String registryType, RecordRegistry<?> recordRegistry) {
        recordRegistries.put(registryType, recordRegistry);
    }

    public void addSerializerRegistry(String registryType, AbstractRecordSerializer<?> serializer) {
        serializerRegistries.put(registryType, serializer);
    }

    public void addAssetRegistry(String registryType, AssetRegistry<?> assetRegistry) {
        assetRegistries.put(registryType, assetRegistry);
    }

    public void removeRecordRegistry(String registryType) {
        recordRegistries.remove(registryType);
    }

    public void removeSerializerRegistry(String registryType) {
        serializerRegistries.remove(registryType);
    }

    public void removeAssetRegistry(String registryType) {
        assetRegistries.remove(registryType);
    }

    public boolean containsRecordRegistry(String registryType) {
        return recordRegistries.containsKey(registryType);
    }

    public boolean containsSerializerRegistry(String registryType) {
        return serializerRegistries.containsKey(registryType);
    }

    public boolean containsAssetRegistry(String registryType) {
        return assetRegistries.containsKey(registryType);
    }

    public Optional<RecordRegistry<?>> getRecordRegistry(String registryType) {
        return Optional.ofNullable(recordRegistries.get(registryType));
    }

    public RecordRegistry<?> getDefaultRecordRegistry(DefaultRecordTypes registryType) {
        return recordRegistries.get(registryType.getId());
    }

    public Optional<AssetRegistry<?>> getAssetRegistry(String registryType) {
        return Optional.ofNullable(assetRegistries.get(registryType));
    }

    public AssetRegistry<?> getDefaultAssetRegistry(DefaultAssetTypes registryType) {
        return assetRegistries.get(registryType.getId());
    }

    public Optional<AbstractRecordSerializer<?>> getSerializerRegistry(String registryType) {
        return Optional.ofNullable(serializerRegistries.get(registryType));
    }

    public List<String> getLoadedPlugins() {
        return loadedPlugins;
    }

    public List<String> getLoadedMods() {
        return loadedMods;
    }

    public Map<String, RecordRegistry<?>> getRecordRegistries() {
        return Collections.unmodifiableMap(recordRegistries);
    }

    public Map<String, AbstractRecordSerializer<?>> getSerializerRegistries() {
        return Collections.unmodifiableMap(serializerRegistries);
    }

    public Map<String, AssetRegistry<?>> getAssetRegistries() {
        return Collections.unmodifiableMap(assetRegistries);
    }

    public Map<String, RecordRegistry<?>> copyRegistries() {
        Map<String, RecordRegistry<?>> copy = new HashMap<>();
        copy.putAll(recordRegistries);
        return copy;
    }

}
