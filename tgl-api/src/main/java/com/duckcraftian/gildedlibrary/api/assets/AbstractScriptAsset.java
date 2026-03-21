package com.duckcraftian.gildedlibrary.api.assets;

public abstract class AbstractScriptAsset extends AbstractAsset{

    public AbstractScriptAsset(AbstractAssetBuilder<?, ?> builder) {
        super(builder);
    }

    public abstract void executeFunction(String function, Object... args);

}
