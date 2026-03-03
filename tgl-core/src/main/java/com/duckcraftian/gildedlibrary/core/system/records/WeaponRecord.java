package com.duckcraftian.gildedlibrary.core.system.records;

import com.duckcraftian.gildedlibrary.api.system.records.AbstractWeapon;

public class WeaponRecord extends AbstractWeapon {

    protected WeaponRecord(WeaponBuilder builder) {
        super(builder);
    }

    public static class WeaponBuilder extends AbstractWeaponBuilder<WeaponBuilder> {

        public WeaponBuilder() {
            this.recordType("weapon");
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

}
