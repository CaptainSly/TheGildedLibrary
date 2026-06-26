package com.duckcraftian.gildedlibrary.core.system.items;

import com.duckcraftian.gildedlibrary.api.system.records.AbstractWeapon;
import com.duckcraftian.gildedlibrary.api.system.records.RecordReference;
import com.duckcraftian.gildedlibrary.core.system.records.WeaponRecord;

public class Weapon {

    protected final RecordReference<WeaponRecord> weaponRecord;

    public Weapon(String recordId) {
        this.weaponRecord = new RecordReference<>(recordId);
    }
}
