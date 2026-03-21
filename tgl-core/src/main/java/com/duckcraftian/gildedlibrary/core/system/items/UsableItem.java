package com.duckcraftian.gildedlibrary.core.system.items;

import com.duckcraftian.gildedlibrary.api.assets.AbstractScriptAsset;
import com.duckcraftian.gildedlibrary.api.system.items.IUsable;
import com.duckcraftian.gildedlibrary.api.system.registries.AssetRegistry;
import com.duckcraftian.gildedlibrary.api.system.registries.RecordRegistry;
import com.duckcraftian.gildedlibrary.api.system.registries.RegistryManager;
import com.duckcraftian.gildedlibrary.core.TGL;
import com.duckcraftian.gildedlibrary.core.system.records.ItemRecord;

public class UsableItem extends Item implements IUsable {


    public UsableItem(String recordId) {
        super(recordId);
    }

    @Override
    public void onUse() {
        // Get the Record
        itemRecord.resolve((RecordRegistry<? extends ItemRecord>) TGL.REGISTRY_MANAGER.getDefaultRecordRegistry(RegistryManager.DefaultRecordTypes.ITEMS))
                .flatMap(record -> {
                    return record.getScript();
                }).flatMap(script -> {
                    return script.resolve((AssetRegistry<? extends AbstractScriptAsset>) TGL.REGISTRY_MANAGER.getDefaultAssetRegistry(RegistryManager.DefaultAssetTypes.SCRIPT));
                }).ifPresent(script -> {
                    script.executeFunction("onUse");
                });

    }
}
