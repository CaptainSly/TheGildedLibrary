package com.duckcraftian.gildedlibrary.api.system.records;

import com.duckcraftian.gildedlibrary.api.system.serialization.AbstractRecordSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class AbstractSkill extends AbstractRecord {

    private String name;
    private String description;
    private String specialization;

    private int baseValue;

    private RecordReference<AbstractAttribute> governingAttribute;

    protected AbstractSkill(AbstractSkillBuilder<?, ?> builder) {
        super(builder);
        this.name = builder.name;
        this.description = builder.description;
        this.specialization = builder.specialization;
        this.baseValue = builder.baseValue;
        this.governingAttribute = new RecordReference<>(builder.governingAttribute);
    }

    protected void fillBuilder(AbstractSkillBuilder<?, ?> builder) {
        super.fillBuilder(builder);
        builder.name(this.name);
        builder.description(this.description);
        builder.specialization(this.specialization);
        builder.baseValue(this.baseValue);
        builder.governingAttribute(this.governingAttribute.id());
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getSpecialization() {
        return specialization;
    }

    public int getBaseValue() {
        return baseValue;
    }

    public RecordReference<AbstractAttribute> getGoverningAttribute() {
        return governingAttribute;
    }

    public static abstract class AbstractSkillBuilder<T extends AbstractSkillBuilder<T, R>, R extends AbstractSkill> extends AbstractRecordBuilder<T, R> {

        private String name;
        private String description;
        private String specialization;
        private String governingAttribute;
        private int baseValue;

        public AbstractSkillBuilder() {
            this.recordType("skills");
        }

        public T name(String name) {
            this.name = name;
            return self();
        }

        public T description(String description) {
            this.description = description;
            return self();
        }

        public T specialization(String specialization) {
            this.specialization = specialization;
            return self();
        }

        public T governingAttribute(String governingAttribute) {
            this.governingAttribute = governingAttribute;
            return self();
        }

        public T baseValue(int baseValue) {
            this.baseValue = baseValue;
            return self();
        }

    }

    public static abstract class AbstractSkillSerializer<R extends AbstractSkill> extends RecordSerializer<R> {

        @Override
        public byte[] write(R record) {
            byte[] recordBytes;
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bos.write(super.write(record));
                writeString(bos, record.getName());
                writeString(bos, record.getDescription());
                writeString(bos, record.getSpecialization());
                write(bos, record.getBaseValue());
                writeString(bos, record.getGoverningAttribute().id());
                recordBytes = bos.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return recordBytes;
        }

        protected void readFields(ByteArrayInputStream bis, AbstractSkillBuilder<?, ?> builder) throws IOException {
            super.readFields(bis, builder);
            String name = getString(bis);
            String description = getString(bis);
            String specialization = getString(bis);
            int baseValue = get(bis);
            String governingAttribute = getString(bis);

            builder.name(name);
            builder.description(description);
            builder.specialization(specialization);
            builder.baseValue(baseValue);
            builder.governingAttribute(governingAttribute);
        }
    }

}