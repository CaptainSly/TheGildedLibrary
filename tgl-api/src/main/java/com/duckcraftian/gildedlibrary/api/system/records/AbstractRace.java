package com.duckcraftian.gildedlibrary.api.system.records;

import com.duckcraftian.gildedlibrary.api.system.character.BaseDefinitions;
import com.duckcraftian.gildedlibrary.api.system.character.SlotDefinition;

import java.util.ArrayList;
import java.util.List;

public class AbstractRace extends AbstractRecord {

    private String name;
    private String description;

    private List<RecordReference<AbstractAttribute>> baseAttributes;
    private List<RecordReference<AbstractSkill>> baseSkills;

    private List<SlotDefinition> validSlotDefinitions;

    protected AbstractRace(AbstractRecordBuilder<?, ?> builder) {
        super(builder);
        baseAttributes = new ArrayList<>();
        baseSkills = new ArrayList<>();
        validSlotDefinitions = new ArrayList<>();
        validSlotDefinitions.add(BaseDefinitions.HEAD_DEFINITION);
        validSlotDefinitions.add(BaseDefinitions.EAR_DEFINITION);
        validSlotDefinitions.add(BaseDefinitions.NECK_DEFINITION);
        validSlotDefinitions.add(BaseDefinitions.SHOULDER_DEFINITION);
        validSlotDefinitions.add(BaseDefinitions.ARM_DEFINITION);
        validSlotDefinitions.add(BaseDefinitions.HAND_DEFINITION);
        validSlotDefinitions.add(BaseDefinitions.CHEST_DEFINITION);
        validSlotDefinitions.add(BaseDefinitions.LEG_DEFINITION);
        validSlotDefinitions.add(BaseDefinitions.FEET_DEFINITION);
    }

    @Override
    public AbstractRecordBuilder<?, ?> toBuilder() {
        return null;
    }
}