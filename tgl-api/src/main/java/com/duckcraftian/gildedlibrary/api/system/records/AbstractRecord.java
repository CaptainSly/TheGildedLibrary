package com.duckcraftian.gildedlibrary.api.system.records;

import com.duckcraftian.gildedlibrary.api.assets.AbstractAsset;
import com.duckcraftian.gildedlibrary.api.system.Builder;

import java.util.Optional;

public abstract class AbstractRecord implements IRecord {

    private final String id;
    private final String editorId;
    private final String modId;
    private final String recordType;
    private final Optional<String> parentId;

    protected AbstractRecord(AbstractRecordBuilder<?> builder) {
        this.editorId = builder.editorId;
        this.modId = builder.modId;
        this.recordType = builder.recordType;
        this.parentId = builder.parentId;
        this.id = modId + ":" + recordType + ":" + builder.itemId;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getEditorId() {
        return editorId;
    }

    @Override
    public String getModId() {
        return modId;
    }

    @Override
    public String getRecordType() {
        return recordType;
    }

    @Override
    public Optional<String> getParentId() {
        return parentId;
    }

    public static abstract class AbstractRecordBuilder<T extends AbstractRecordBuilder<T>> extends Builder<T, AbstractRecord> {
        private String itemId;
        private String editorId;
        private String modId;
        private String recordType;
        private Optional<String> parentId = Optional.empty();

        public T itemId(String itemId) {
            this.itemId = itemId;
            return self();
        }

        public T editorId(String editorId) {
            this.editorId = editorId;
            return self();
        }

        public T modId(String modId) {
            this.modId = modId;
            return self();
        }

        public T recordType(String recordType) {
            this.recordType = recordType;
            return self();
        }

        public T parentId(String parentId) {
            this.parentId = Optional.of(parentId);
            return self();
        }

    }

}
