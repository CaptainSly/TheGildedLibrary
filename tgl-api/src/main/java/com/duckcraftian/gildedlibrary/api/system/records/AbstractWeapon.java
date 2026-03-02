package com.duckcraftian.gildedlibrary.api.system.records;

import com.duckcraftian.gildedlibrary.api.system.AbstractRecord;

public abstract class AbstractWeapon extends AbstractItem {

    private final String weaponType;
    private final float attackPower;
    private final float speedCost;
    private final RecordReference<AbstractSkill> requiredSkill;
    private final RecordReference<AbstractEnchantment>[] enchantments;
    private final float critChance;

    protected AbstractWeapon(AbstractWeaponBuilder<?> builder) {
        super(builder);
        this.weaponType = builder.weaponType;
        this.attackPower = builder.attackPower;
        this.speedCost = builder.speedCost;
        this.requiredSkill = builder.requiredSkill;
        this.enchantments = builder.enchantments;
        this.critChance = builder.critChance;
    }

    public String getWeaponType() {
        return weaponType;
    }

    public float getAttackPower() {
        return attackPower;
    }

    public float getSpeedCost() {
        return speedCost;
    }

    public RecordReference<AbstractSkill> getRequiredSkill() {
        return requiredSkill;
    }

    public RecordReference<AbstractEnchantment>[] getEnchantments() {
        return enchantments;
    }

    public float getCritChance() {
        return critChance;
    }

    public static abstract class AbstractWeaponBuilder<T extends AbstractWeaponBuilder<T>> extends AbstractItemBuilder<T> {
        private String weaponType;
        private float attackPower;
        private float speedCost;
        private RecordReference<AbstractSkill> requiredSkill;
        private RecordReference<AbstractEnchantment>[] enchantments;
        private float critChance;

        public T weaponType(String weaponType) {
            this.weaponType = weaponType;
            return self();
        }

        public T attackPower(float attackPower) {
            this.attackPower = attackPower;
            return self();
        }

        public T speedCost(float speedCost) {
            this.speedCost = speedCost;
            return self();
        }

        public T requiredSkill(RecordReference<AbstractSkill> requiredSkill) {
            this.requiredSkill = requiredSkill;
            return self();
        }

        @SafeVarargs
        public final T enchantments(RecordReference<AbstractEnchantment>... enchantments) {
            this.enchantments = enchantments;
            return self();
        }

        public T critChance(float critChance) {
            this.critChance = critChance;
            return self();
        }
    }

}
