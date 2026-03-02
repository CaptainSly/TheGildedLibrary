package com.duckcraftian.gildedlibrary.core.system.records;

import com.duckcraftian.gildedlibrary.api.system.records.AbstractItem;
import com.duckcraftian.gildedlibrary.api.system.records.AbstractWeapon;

public class ItemRecord extends AbstractItem {


    protected ItemRecord(ItemBuilder builder) {
        super(builder);
    }

    public static class ItemBuilder extends AbstractItem.AbstractItemBuilder<ItemRecord.ItemBuilder> {

        public ItemBuilder() {
            this.recordType("item");
        }

        @Override
        public ItemBuilder self() {
            return this;
        }

        @Override
        public ItemRecord build() {
            return new ItemRecord(this);
        }
    }

}
