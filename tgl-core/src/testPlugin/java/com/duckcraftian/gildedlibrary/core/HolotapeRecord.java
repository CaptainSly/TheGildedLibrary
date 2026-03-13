package com.duckcraftian.gildedlibrary.core;

import com.duckcraftian.gildedlibrary.api.system.records.AbstractItem;

public class HolotapeRecord extends AbstractItem {

    private final String[] data;

    public HolotapeRecord(HolotapeBuilder builder) {
        super(builder);
        this.data = builder.data;
    }

    @Override
    public HolotapeBuilder toBuilder() {
        return new HolotapeBuilder();
    }

    public static class HolotapeBuilder extends AbstractItemBuilder<HolotapeBuilder, HolotapeRecord> {

        private String[] data;

        public HolotapeBuilder() {
            this.recordType("holotapes");
        }

        public HolotapeBuilder data(String... data) {
            this.data = new String[data.length];
            for (int i = 0; i < data.length; i++)
                this.data[i] = data[i];

            return self();
        }

        @Override
        public HolotapeBuilder self() {
            return this;
        }

        @Override
        public HolotapeRecord build() {
            return new HolotapeRecord(this);
        }
    }

}