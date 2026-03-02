package com.duckcraftian.gildedlibrary.api.system.mods;

import com.duckcraftian.gildedlibrary.api.system.Builder;

public class ModMetadata {

    public enum ModType {
        MOD, MASTER, GAME;

        public static ModType getType(String type) {
            return switch (type) {
                case "mod" -> MOD;
                case "master" -> MASTER;
                default -> GAME;
            };
        }
    }

    private final String id;
    private final String name;
    private final String version;
    private final String description;
    private final String author;
    private final String website;
    private final String engineVersion;
    private final ModType type;

    private final int priority;

    private final String[] tags;
    private final String[] dependencies;
    private final String[] optionalDependencies;
    private final String[] conflicts;

    private final long creationDate;
    private final long lastUpdated;

    public ModMetadata(ModMetadataBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.version = builder.version;
        this.description = builder.description;
        this.author = builder.author;
        this.website = builder.website;
        this.engineVersion = builder.engineVersion;
        this.type = builder.type;
        this.priority = builder.priority;
        this.tags = builder.tags;
        this.dependencies = builder.dependencies;
        this.optionalDependencies = builder.optionalDependencies;
        this.conflicts = builder.conflicts;
        this.creationDate = builder.creationDate;
        this.lastUpdated = builder.lastUpdated;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public String getWebsite() {
        return website;
    }

    public String getEngineVersion() {
        return engineVersion;
    }

    public ModType getType() {
        return type;
    }

    public int getPriority() {
        return priority;
    }

    public String[] getTags() {
        return tags;
    }

    public String[] getDependencies() {
        return dependencies;
    }

    public String[] getOptionalDependencies() {
        return optionalDependencies;
    }

    public String[] getConflicts() {
        return conflicts;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public static class ModMetadataBuilder extends Builder<ModMetadataBuilder, ModMetadata> {
        private String id;
        private String name;
        private String version;
        private String description;
        private String author;
        private String website;
        private String engineVersion;
        private ModType type;

        private int priority;

        private String[] tags = {};
        private String[] dependencies = {};
        private String[] optionalDependencies = {};
        private String[] conflicts = {};

        private long creationDate;
        private long lastUpdated;

        public ModMetadataBuilder id(String id) {
            this.id = id;
            return self();
        }

        public ModMetadataBuilder name(String name) {
            this.name = name;
            return self();
        }

        public ModMetadataBuilder version(String version) {
            this.version = version;
            return self();
        }

        public ModMetadataBuilder description(String description) {
            this.description = description;
            return self();
        }

        public ModMetadataBuilder author(String author) {
            this.author = author;
            return self();
        }

        public ModMetadataBuilder website(String website) {
            this.website = website;
            return self();
        }

        public ModMetadataBuilder engineVersion(String engineVersion) {
            this.engineVersion = engineVersion;
            return self();
        }

        public ModMetadataBuilder type(ModType type) {
            this.type = type;
            return self();
        }

        public ModMetadataBuilder priority(int priority) {
            this.priority = priority;
            return self();
        }

        public ModMetadataBuilder tags(String... tags) {
            this.tags = tags;
            return self();
        }

        public ModMetadataBuilder dependencies(String... dependencies) {
            this.dependencies = dependencies;
            return self();
        }

        public ModMetadataBuilder optionalDependencies(String... optionalDependencies) {
            this.optionalDependencies = optionalDependencies;
            return self();
        }

        public ModMetadataBuilder conflicts(String... conflicts) {
            this.conflicts = conflicts;
            return self();
        }

        public ModMetadataBuilder creationDate(long creationDate) {
            this.creationDate = creationDate;
            return self();
        }

        public ModMetadataBuilder lastUpdated(long lastUpdated) {
            this.lastUpdated = lastUpdated;
            return self();
        }

        @Override
        public ModMetadataBuilder self() {
            return this;
        }

        @Override
        public ModMetadata build() {
            return new ModMetadata(this);
        }
    }

}
