package com.duckcraftian.gildedlibrary.api.assets;

import com.duckcraftian.gildedlibrary.api.system.Builder;

/**
 * Defines the Contract for an AbstractAsset. Any type of asset consumed by the engine uses this as a base class</p>
 * </p>
 * i.e, Textures, Meshes, Models, Sounds, Scripts, etc.,
 *</p>
 * If you want to create a custom asset, you can use the <code>AssetType.OTHER</code> value for the type.
 */
public abstract class AbstractAsset implements IAsset {

    private final String id;
    private final String path;
    private AssetState state;
    private final AssetType type;

    public AbstractAsset(AbstractAssetBuilder<?> builder) {
        this.id = builder.id;
        this.path = builder.path;
        this.type = builder.type;
        this.state = AssetState.UNLOADED;
    }

    public abstract void load();

    public abstract void unload();

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getPath() {
        return path;
    }

    public void setState(AssetState state) {
        this.state = state;
    }

    @Override
    public AssetState getState() {
        return state;
    }

    @Override
    public AssetType getType() {
        return type;
    }

    public static abstract class AbstractAssetBuilder<T extends AbstractAssetBuilder<T>> extends Builder<T, AbstractAsset> {
        private String id;
        private String path;
        private AssetType type;

        public T id(String id) {
            this.id = id;
            return self();
        }

        public T path(String path) {
            this.path = path;
            return self();
        }

        public T type(AssetType type) {
            this.type = type;
            return self();
        }
    }

}
