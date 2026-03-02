package com.duckcraftian.gildedlibrary.api.assets;

import com.duckcraftian.gildedlibrary.api.system.IDisposable;

public interface IAsset extends IDisposable {

    String getId();
    String getPath();
    AssetState getState();
    AssetType getType();

}
