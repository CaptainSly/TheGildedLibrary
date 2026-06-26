package com.duckcraftian.gildedlibrary.core.system.records;

import com.duckcraftian.gildedlibrary.api.system.records.AbstractRangedWeapon;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class RangedWeaponRecord extends AbstractRangedWeapon {

    public RangedWeaponRecord(RangedWeaponBuilder builder) {
        super(builder);
    }

    @Override
    public RangedWeaponBuilder toBuilder() {
        RangedWeaponBuilder builder = new RangedWeaponBuilder();
        fillBuilder(builder);
        return builder;
    }

    public static class RangedWeaponBuilder extends AbstractRangedWeaponBuilder<RangedWeaponBuilder, RangedWeaponRecord> {

        @Override
        public RangedWeaponBuilder self() {
            return this;
        }

        @Override
        public RangedWeaponRecord build() {
            return new RangedWeaponRecord(this);
        }
    }

    public static class RangedWeaponSerializer extends AbstractRangedWeaponSerializer<RangedWeaponRecord> {

        @Override
        public RangedWeaponRecord read(byte[] recordBytes) throws IOException {
            RangedWeaponBuilder weaponBuilder = new RangedWeaponBuilder();
            readFields(new ByteArrayInputStream(recordBytes), weaponBuilder);

            return weaponBuilder.build();
        }
    }

}
