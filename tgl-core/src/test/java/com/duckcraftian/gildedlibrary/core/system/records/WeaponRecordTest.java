package com.duckcraftian.gildedlibrary.core.system.records;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WeaponRecordTest {

    @Test
    void testWeaponRecordBuildsCorrectly() {
        WeaponRecord sword = new WeaponRecord.WeaponBuilder()
                .modId("arcanumoria")
                .itemId("sword")
                .name("test sword")
                .attackPower(10.0f)
                .build();

        assertEquals("arcanumoria:weapon:sword", sword.getId());
        assertEquals("test sword", sword.getName());
        assertEquals(10.0f, sword.getAttackPower());

    }

    @Test
    void testItemRecordBuildsCorrectly() {
        ItemRecord key = new ItemRecord.ItemBuilder()
                .modId("arcanumoria")
                .itemId("silver_key")
                .name("Silver Key")
                .build();

        assertEquals("arcanumoria:item:silver_key", key.getId());
        assertEquals("Silver Key", key.getName());
    }

}
