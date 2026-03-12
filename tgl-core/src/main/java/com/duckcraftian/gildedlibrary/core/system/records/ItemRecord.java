package com.duckcraftian.gildedlibrary.core.system.records;

import com.duckcraftian.gildedlibrary.api.system.records.AbstractItem;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ItemRecord extends AbstractItem {


    protected ItemRecord(ItemBuilder builder) {
        super(builder);
    }

    @Override
    public ItemBuilder toBuilder() {
        ItemBuilder builder = new ItemBuilder();
        fillBuilder(builder);
        return builder;
    }

    public static class ItemBuilder extends AbstractItem.AbstractItemBuilder<ItemRecord.ItemBuilder, ItemRecord> {

        public ItemBuilder() {
            this.recordType("items");
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

    public static class ItemSerializer extends AbstractItemSerializer<ItemRecord> {

        @Override
        public ItemRecord read(byte[] recordBytes) throws IOException {
            ItemBuilder builder = new ItemBuilder();
            readFields(new ByteArrayInputStream(recordBytes), builder);

            return builder.build();
        }
    }

}
