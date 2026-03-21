package com.duckcraftian.gildedlibrary.api.assets;

public abstract class AbstractTextureAsset extends AbstractAsset {

    public AbstractTextureAsset(AbstractAssetBuilder<?, ?> builder) {
        super(builder);
    }

    public abstract int getWidth();
    public abstract int getHeight();

}
