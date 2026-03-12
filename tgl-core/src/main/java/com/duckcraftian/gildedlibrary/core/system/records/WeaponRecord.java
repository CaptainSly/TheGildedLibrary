package com.duckcraftian.gildedlibrary.core.system.records;

import com.duckcraftian.gildedlibrary.api.system.records.AbstractWeapon;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class WeaponRecord extends AbstractWeapon {

    protected WeaponRecord(WeaponBuilder builder) {
        super(builder);
    }

    @Override
    public WeaponBuilder toBuilder() {
        WeaponBuilder builder = new WeaponBuilder();
        fillBuilder(builder);
        return builder;
    }

    public static class WeaponBuilder extends AbstractWeaponBuilder<WeaponBuilder, WeaponRecord> {

        public WeaponBuilder() {
            this.recordType("weapons");
        }

        @Override
        public WeaponBuilder self() {
            return this;
        }

        @Override
        public WeaponRecord build() {
            return new WeaponRecord(this);
        }
    }

    public static class WeaponSerializer extends AbstractWeaponSerializer<WeaponRecord> {

        @Override
        public WeaponRecord read(byte[] recordBytes) throws IOException {
            WeaponBuilder weaponBuilder = new WeaponBuilder();
            readFields(new ByteArrayInputStream(recordBytes), weaponBuilder);

            return weaponBuilder.build();
        }
    }

}
