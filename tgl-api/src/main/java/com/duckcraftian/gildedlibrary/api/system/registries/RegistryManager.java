package com.duckcraftian.gildedlibrary.api.system.registries;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RegistryManager {

    private final Map<String, Registry<?>> registries;

    public RegistryManager() {
        registries = new HashMap<>();
    }

    public void addRegistry(String registryType, Registry<?> registry) {
        registries.put(registryType, registry);
    }

    public void removeRegistry(String registryType) {
        registries.remove(registryType);
    }

    public boolean containsRegistry(String registryType) {
        return registries.containsKey(registryType);
    }

    public Optional<Registry<?>> getRegistry(String registryType) {
        return Optional.ofNullable(registries.get(registryType));
    }

    public Map<String, Registry<?>> getRegistries() {
        return registries;
    }

}
