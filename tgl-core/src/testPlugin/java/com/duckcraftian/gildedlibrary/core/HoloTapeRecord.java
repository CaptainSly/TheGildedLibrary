package com.duckcraftian.gildedlibrary.core;

import com.duckcraftian.gildedlibrary.api.system.AbstractRecord;
import com.duckcraftian.gildedlibrary.api.system.Builder;

public class HoloTapeRecord extends AbstractRecord {

    public HoloTapeRecord(HoloTapeBuilder builder) {
        super(builder);
    }

    public static class HoloTapeBuilder extends AbstractRecordBuilder<HoloTapeBuilder> {

        public HoloTapeBuilder() {
            this.recordType("holotape");
        }

        @Override
        public HoloTapeBuilder self() {
            return this;
        }

        @Override
        public HoloTapeRecord build() {
            return new HoloTapeRecord(this);
        }
    }

}
