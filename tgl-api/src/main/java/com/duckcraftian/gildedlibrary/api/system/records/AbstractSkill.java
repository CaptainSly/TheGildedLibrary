package com.duckcraftian.gildedlibrary.api.system.records;

public abstract class AbstractSkill extends AbstractRecord {

    private final String name;
    private final String description;

    protected AbstractSkill(AbstractSkillBuilder<?> builder) {
        super(builder);
        this.name = builder.name;
        this.description = builder.description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static abstract class AbstractSkillBuilder<T extends AbstractSkillBuilder<T>> extends AbstractRecordBuilder<T> {

        private String name;
        private String description;

        public T name(String name) {
            this.name = name;
            return self();
        }

        public T description(String description) {
            this.description = description;
            return self();
        }

    }

}
