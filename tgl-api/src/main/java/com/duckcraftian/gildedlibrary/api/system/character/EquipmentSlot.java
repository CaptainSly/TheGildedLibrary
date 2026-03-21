package com.duckcraftian.gildedlibrary.api.system.character;

import com.duckcraftian.gildedlibrary.api.system.records.AbstractEquipment;
import com.duckcraftian.gildedlibrary.api.system.records.RecordReference;

import java.util.Optional;

public class EquipmentSlot {

    private Optional<RecordReference<AbstractEquipment>> equipment;

    public EquipmentSlot(String equipmentId) {
        equipment = Optional.of(new RecordReference<>(equipmentId));
    }

    public Optional<RecordReference<AbstractEquipment>> getEquipment() {
        return equipment;
    }

}
