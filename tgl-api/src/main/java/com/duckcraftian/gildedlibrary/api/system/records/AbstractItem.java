package com.duckcraftian.gildedlibrary.api.system.records;

public abstract class AbstractItem extends AbstractRecord {

    private final String name;
    private final String description;
    private final String image;
    private final String model;
    private final String rarity;

    private final float weight;
    private final float cost;

    protected AbstractItem(AbstractItemBuilder<?> builder) {
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

    public static abstract class AbstractItemBuilder<T extends AbstractItemBuilder<T>> extends AbstractRecordBuilder<T> {
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

}
