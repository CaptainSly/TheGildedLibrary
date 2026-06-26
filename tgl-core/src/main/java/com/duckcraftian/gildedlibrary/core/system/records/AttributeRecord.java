package com.duckcraftian.gildedlibrary.core.system.records;

import com.duckcraftian.gildedlibrary.api.system.records.AbstractAttribute;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class AttributeRecord extends AbstractAttribute {

    protected AttributeRecord(AttributeBuilder builder) {
        super(builder);
    }

    public AttributeBuilder toBuilder() {
        AttributeBuilder builder = new AttributeBuilder();
        fillBuilder(builder);
        return builder;
    }

    public static class AttributeBuilder extends AbstractAttributeBuilder<AttributeBuilder, AttributeRecord> {

        @Override
        public AttributeBuilder self() {
            return this;
        }

        @Override
        public AttributeRecord build() {
            return new AttributeRecord(this);
        }
    }

    public static class AttributeSerializer extends AbstractAttributeSerializer<AttributeRecord> {

        @Override
        public AttributeRecord read(byte[] recordBytes) throws IOException {
            AttributeBuilder builder = new AttributeBuilder();
            readFields(new ByteArrayInputStream(recordBytes), builder);

            return builder.build();
        }
    }

}