package com.duckcraftian.gildedlibrary.api.system.records;

import com.duckcraftian.gildedlibrary.api.system.character.EquipmentSlot;
import com.duckcraftian.gildedlibrary.api.system.geometry.Transform;

import java.util.ArrayList;
import java.util.List;

public class AbstractActor extends AbstractRecord {

    private String name;

    private RecordReference<AbstractRace> race;

    private List<RecordReference<AbstractAttribute>> attributes;
    private List<RecordReference<AbstractSkill>> skills;
    private List<EquipmentSlot> equipmentSlots;

    protected AbstractActor(AbstractRecordBuilder<?, ?> builder) {
        super(builder);
        attributes = new ArrayList<>();
        skills = new ArrayList<>();
        equipmentSlots = new ArrayList<>();
    }

    @Override
    public AbstractRecordBuilder<?, ?> toBuilder() {
        return null;
    }
}