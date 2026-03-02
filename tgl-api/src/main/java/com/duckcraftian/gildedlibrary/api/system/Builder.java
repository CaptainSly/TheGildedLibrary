package com.duckcraftian.gildedlibrary.api.system;

import com.duckcraftian.gildedlibrary.api.assets.AbstractAsset;
import com.duckcraftian.gildedlibrary.api.assets.AssetType;

public abstract class Builder<T extends Builder<T, R>, R> {

    public abstract T self();

    public abstract R build();

}
