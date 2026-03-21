package com.duckcraftian.gildedlibrary.core.system.items;

import com.duckcraftian.gildedlibrary.api.system.records.RecordReference;
import com.duckcraftian.gildedlibrary.core.system.records.ItemRecord;

public class Item {

    protected final RecordReference<ItemRecord> itemRecord;

    public Item(final String recordId) {
        itemRecord = new RecordReference<>(recordId);
    }

}
