package com.duckcraftian.gildedlibrary.api.assets;

import com.duckcraftian.gildedlibrary.api.system.registries.AssetRegistry;

import java.util.Optional;

public record AssetReference<A extends AbstractAsset>(String id) {

    public Optional<? extends A> resolve(AssetRegistry<? extends A> assetRegistry) {
        return assetRegistry.resolve(id);
    }

}
