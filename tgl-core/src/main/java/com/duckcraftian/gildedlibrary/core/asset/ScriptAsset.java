package com.duckcraftian.gildedlibrary.core.asset;

import com.duckcraftian.gildedlibrary.api.assets.AbstractScriptAsset;
import com.duckcraftian.gildedlibrary.api.assets.AssetState;

public class ScriptAsset extends AbstractScriptAsset {

    public ScriptAsset(ScriptBuilder builder) {
        super(builder);
    }

    @Override
    public void executeFunction(String function, Object... args) {

    }

    @Override
    public void load() {
        this.setState(AssetState.LOADING);

        this.setState(AssetState.LOADED);
    }

    @Override
    public void unload() {

        this.setState(AssetState.UNLOADED);
    }

    @Override
    public void onDispose() {

    }


    public static class ScriptBuilder extends AbstractAssetBuilder<ScriptBuilder, ScriptAsset> {
        @Override
        public ScriptBuilder self() {
            return this;
        }

        @Override
        public ScriptAsset build() {
            return new ScriptAsset(this);
        }
    }
}
