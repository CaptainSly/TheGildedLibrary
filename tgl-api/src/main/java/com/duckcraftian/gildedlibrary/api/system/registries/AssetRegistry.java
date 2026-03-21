package com.duckcraftian.gildedlibrary.api.system.registries;

import com.duckcraftian.gildedlibrary.api.assets.AbstractAsset;
import com.duckcraftian.gildedlibrary.api.assets.AssetReference;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AssetRegistry<A extends AbstractAsset> {

    private final HashMap<String, A> registry;
    private final String assetType;

    public AssetRegistry(String assetType) {
        this.assetType = assetType;
        registry = new HashMap<>();
    }

    public void addAsset(A asset) {
        registry.put(asset.getId(), asset);
    }

    public void removeAsset(A asset) {
        registry.remove(asset.getId());
    }

    public String getAssetType() {
        return assetType;
    }

    public Optional<A> resolve(String assetId) {
        if (assetId.isEmpty() || assetId.isBlank()) return Optional.empty();
        return Optional.ofNullable(registry.get(assetId));
    }

    public Map<String, A> getRegistry() {
        return Collections.unmodifiableMap(registry);
    }

}
