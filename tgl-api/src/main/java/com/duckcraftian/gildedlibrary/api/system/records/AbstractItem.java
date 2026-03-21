package com.duckcraftian.gildedlibrary.api.system.records;

import com.duckcraftian.gildedlibrary.api.assets.AbstractModelAsset;
import com.duckcraftian.gildedlibrary.api.assets.AbstractScriptAsset;
import com.duckcraftian.gildedlibrary.api.assets.AbstractTextureAsset;
import com.duckcraftian.gildedlibrary.api.assets.AssetReference;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

public abstract class AbstractItem extends AbstractRecord {

    private final String name;
    private final String description;
    private final AssetReference<AbstractTextureAsset> image;
    private final AssetReference<AbstractModelAsset> model;
    private final Optional<AssetReference<AbstractScriptAsset>> script;
    private final String rarity;

    private final float weight;
    private final float cost;

    protected AbstractItem(AbstractItemBuilder<?, ?> builder) {
        super(builder);
        this.name = builder.name;
        this.description = builder.description;
        this.image = new AssetReference<>(builder.image);
        this.model = new AssetReference<>(builder.model);
        this.script = Optional.ofNullable(builder.script == null ? null : new AssetReference<>(builder.script));
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

    public AssetReference<AbstractTextureAsset> getImage() {
        return image;
    }

    public AssetReference<AbstractModelAsset> getModel() {
        return model;
    }

    public Optional<AssetReference<AbstractScriptAsset>> getScript() {
        return script;
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
        builder.image(this.image.id());
        builder.model(this.model.id());
        builder.rarity(this.rarity);
        builder.weight(this.weight);
        builder.cost(this.cost);

        var scriptPresent = this.script.isPresent();
        if (scriptPresent) {
            AssetReference<AbstractScriptAsset> script = this.script.get();
            builder.script(script.id());
        }

    }

    public static abstract class AbstractItemBuilder<T extends AbstractItemBuilder<T, R>, R extends AbstractItem> extends AbstractRecordBuilder<T, R> {
        private String name;
        private String description;
        private String image;
        private String model;
        private String script;
        private String rarity;
        private float weight;
        private float cost;

        public T script(String script) {
            this.script = script;
            return self();
        }

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
                writeString(bos, record.getImage().id());
                writeString(bos, record.getModel().id());

                var scriptPresent = record.getScript().isPresent();
                if (scriptPresent) {
                    bos.write(1);
                    AssetReference<AbstractScriptAsset> script = record.getScript().get();
                    writeString(bos, script.id());
                } else
                    bos.write(0);

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
            String script = bis.read() == 1 ? getString(bis) : null;
            String rarity = getString(bis);
            Float weight = getFloat(bis);
            Float cost = getFloat(bis);

            builder.name(name).description(description).image(image).model(model).script(script).rarity(rarity).weight(weight).cost(cost);
        }
    }

}
