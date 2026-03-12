package com.duckcraftian.gildedlibrary.api.system.records;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class AbstractItem extends AbstractRecord {

    private final String name;
    private final String description;
    private final String image;
    private final String model;
    private final String rarity;

    private final float weight;
    private final float cost;

    protected AbstractItem(AbstractItemBuilder<?, ?> builder) {
        super(builder);
        this.name = builder.name;
        this.description = builder.description;
        this.image = builder.image;
        this.model = builder.model;
        this.rarity = builder.rarity;
        this.weight = builder.weight;
        this.cost = builder.cost;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getModel() {
        return model;
    }

    public String getRarity() {
        return rarity;
    }

    public float getWeight() {
        return weight;
    }

    public float getCost() {
        return cost;
    }

    protected void fillBuilder(AbstractItemBuilder<?, ?> builder) {
        super.fillBuilder(builder);
        builder.name(this.name);
        builder.description(this.description);
        builder.image(this.image);
        builder.model(this.model);
        builder.rarity(this.rarity);
        builder.weight(this.weight);
        builder.cost(this.cost);
    }

    public static abstract class AbstractItemBuilder<T extends AbstractItemBuilder<T, R>, R extends AbstractItem> extends AbstractRecordBuilder<T, R> {
        private String name;
        private String description;
        private String image;
        private String model;
        private String rarity;
        private float weight;
        private float cost;

        public T name(String name) {
            this.name = name;
            return self();
        }

        public T description(String description) {
            this.description = description;
            return self();
        }

        public T image(String image) {
            this.image = image;
            return self();
        }

        public T model(String model) {
            this.model = model;
            return self();
        }

        public T rarity(String rarity) {
            this.rarity = rarity;
            return self();
        }

        public T weight(float weight) {
            this.weight = weight;
            return self();
        }

        public T cost(float cost) {
            this.cost = cost;
            return self();
        }

    }

    public abstract static class AbstractItemSerializer<R extends AbstractItem> extends RecordSerializer<R> {

        @Override
        public byte[] write(R record) {
            byte[] recordBytes;
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bos.write(super.write(record));
                writeString(bos, record.getName());
                writeString(bos, record.getDescription());
                writeString(bos, record.getImage());
                writeString(bos, record.getModel());
                writeString(bos, record.getRarity());
                writeFloat(bos, record.getWeight());
                writeFloat(bos, record.getCost());
                recordBytes = bos.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return recordBytes;
        }

        public void readFields(ByteArrayInputStream bis, AbstractItemBuilder<?, ?> builder) throws IOException {
            super.readFields(bis, builder);
            String name = getString(bis);
            String description = getString(bis);
            String image = getString(bis);
            String model = getString(bis);
            String rarity = getString(bis);
            Float weight = getFloat(bis);
            Float cost = getFloat(bis);

            builder.name(name).description(description).image(image).model(model).rarity(rarity).weight(weight).cost(cost);
        }
    }

}
