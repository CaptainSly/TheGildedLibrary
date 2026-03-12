package com.duckcraftian.gildedlibrary.api.system.records;

import com.duckcraftian.gildedlibrary.api.system.Builder;
import com.duckcraftian.gildedlibrary.api.system.serialization.AbstractRecordSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public abstract class AbstractRecord implements IRecord {

    private final String id;
    private final String itemId;
    private final String editorId;
    private final String modId;
    private final String recordType;
    private final Optional<String> parentId;

    protected AbstractRecord(AbstractRecordBuilder<?, ?> builder) {
        this.itemId = builder.itemId;
        this.editorId = builder.editorId;
        this.modId = builder.modId;
        this.recordType = builder.recordType;
        this.parentId = builder.parentId;
        this.id = modId + ":" + recordType + ":" + itemId;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getItemId() {
        return itemId;
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

    public abstract AbstractRecordBuilder<?, ?> toBuilder();

    protected void fillBuilder(AbstractRecordBuilder<?, ?> builder) {
        builder.itemId(this.itemId);
        builder.editorId(this.editorId);
        builder.modId(this.modId);
        builder.recordType(this.recordType);
        this.parentId.ifPresent(builder::parentId);
    }

    public static abstract class AbstractRecordBuilder<T extends AbstractRecordBuilder<T, R>, R extends AbstractRecord> extends Builder<T, R> {
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

    public abstract static class RecordSerializer<R extends AbstractRecord> extends AbstractRecordSerializer<R> {

        @Override
        public byte[] write(R record) {
            byte[] recordBytes;
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                writeString(bos, record.getItemId());
                writeString(bos, record.getEditorId());
                writeString(bos, record.getModId());
                writeString(bos, record.getRecordType());
                writeString(bos, record.getParentId().isPresent() ? record.getParentId().get() : "");
                recordBytes = bos.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return recordBytes;
        }

        @Override
        protected void readFields(ByteArrayInputStream bis, AbstractRecordBuilder<?, ?> builder) throws IOException {
            String id = getString(bis);
            String editorId = getString(bis);
            String modId = getString(bis);
            String recordType = getString(bis);
            String parentId = getString(bis);
            builder
                    .itemId(id)
                    .editorId(editorId)
                    .modId(modId)
                    .recordType(recordType)
                    .parentId(parentId);
        }

    }

}
