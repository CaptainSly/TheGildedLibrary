package com.duckcraftian.gildedlibrary.api.system.records;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class AbstractAttribute extends AbstractRecord {

    private String name;
    private String description;
    private int baseValue;
    private int min = 0;
    private int max = 1000;

    public AbstractAttribute(AbstractAttributeBuilder builder) {
        super(builder);
        this.name = builder.name;
        this.description = builder.description;
        this.baseValue = builder.baseValue;
        this.min = builder.min;
        this.max = builder.max;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getBaseValue() {
        return baseValue;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    protected void fillBuilder(AbstractAttributeBuilder<?, ?> builder) {
        super.fillBuilder(builder);
        builder.name(this.name);
        builder.description(this.description);
        builder.baseValue(this.baseValue);
        builder.min(this.min);
        builder.max(this.max);
    }

    public static abstract class AbstractAttributeBuilder<T extends AbstractAttributeBuilder<T, R>, R extends AbstractAttribute> extends AbstractRecordBuilder<T, R> {

        private String name;
        private String description;

        private int baseValue;
        private int min;
        private int max;

        public AbstractAttributeBuilder() {
            this.recordType("attributes");
        }

        public T name(String name) {
            this.name = name;
            return self();
        }

        public T description(String description) {
            this.description = description;
            return self();
        }

        public T baseValue(int baseValue) {
            this.baseValue = baseValue;
            return self();
        }

        public T min(int min) {
            this.min = min;
            return self();
        }

        public T max(int max) {
            this.max = max;
            return self();
        }


    }

    public static abstract class AbstractAttributeSerializer<R extends AbstractAttribute> extends RecordSerializer<R> {

        @Override
        public byte[] write(R record) {
            byte[] recordBytes;
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bos.write(super.write(record));
                writeString(bos, record.getName());
                writeString(bos, record.getDescription());
                write(bos, record.getBaseValue());
                write(bos, record.getMin());
                write(bos, record.getMax());
                recordBytes = bos.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return recordBytes;
        }

        protected void readFields(ByteArrayInputStream bis, AbstractAttributeBuilder<?, ?> builder) throws IOException {
            super.readFields(bis, builder);
            String name = getString(bis);
            String description = getString(bis);
            int baseValue = get(bis);
            int min = get(bis);
            int max = get(bis);

            builder.name(name);
            builder.description(description);
            builder.baseValue(baseValue);
            builder.min(min);
            builder.max(max);
        }
    }


}