package com.duckcraftian.gildedlibrary.api.assets;

public abstract class AbstractAudioAsset extends AbstractAsset{

    private int length;
    private boolean mono;

    public AbstractAudioAsset(AbstractAssetBuilder<?, AbstractAudioAsset> builder) {
        super(builder);
    }

}