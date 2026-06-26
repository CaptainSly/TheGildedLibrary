package com.duckcraftian.gildedlibrary.core.system.records;

import com.duckcraftian.gildedlibrary.api.system.records.AbstractSkill;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SkillRecord extends AbstractSkill {

    public SkillRecord(SkillRecordBuilder builder) {
        super(builder);
    }

    public SkillRecordBuilder toBuilder() {
        SkillRecordBuilder builder = new SkillRecordBuilder();
        fillBuilder(builder);
        return builder;
    }

    public static class SkillRecordBuilder extends AbstractSkillBuilder<SkillRecordBuilder,SkillRecord> {

        @Override
        public SkillRecordBuilder self() {
            return this;
        }

        @Override
        public SkillRecord build() {
            return new SkillRecord(this);
        }
    }

    public static class SkillRecordSerializer extends AbstractSkillSerializer<SkillRecord> {

        @Override
        public SkillRecord read(byte[] recordBytes) throws IOException {
            SkillRecordBuilder builder = new SkillRecordBuilder();
            readFields(new ByteArrayInputStream(recordBytes), builder);
            return builder.build();
        }
    }

}