package com.duckcraftian.gildedlibrary.api.assets;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractModelAsset extends AbstractAsset {

    protected List<AbstractMeshAsset> meshList;

    public AbstractModelAsset(AbstractAssetBuilder<?, ?> builder) {
        super(builder);
        meshList = new ArrayList<>();
    }

    public List<AbstractMeshAsset> getMeshList() {
        return meshList;
    }

}
