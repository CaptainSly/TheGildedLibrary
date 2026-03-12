package com.duckcraftian.gildedlibrary.api.system.records;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class AbstractWeapon extends AbstractItem {

    private final String weaponType;
    private final float attackPower;
    private final float speedCost;
    private final float critChance;

    protected AbstractWeapon(AbstractWeaponBuilder<?, ?> builder) {
        super(builder);
        this.weaponType = builder.weaponType;
        this.attackPower = builder.attackPower;
        this.speedCost = builder.speedCost;
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

    public float getCritChance() {
        return critChance;
    }

    protected void fillBuilder(AbstractWeaponBuilder<?, ?> builder) {
        super.fillBuilder(builder);
        builder.weaponType(this.weaponType);
        builder.attackPower(this.attackPower);
        builder.speedCost(this.speedCost);
        builder.critChance(this.critChance);
    }

    public static abstract class AbstractWeaponBuilder<T extends AbstractWeaponBuilder<T, R>, R extends AbstractWeapon> extends AbstractItemBuilder<T, R> {
        private String weaponType;
        private float attackPower;
        private float speedCost;
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

        public T critChance(float critChance) {
            this.critChance = critChance;
            return self();
        }
    }

    public static abstract class AbstractWeaponSerializer<R extends AbstractWeapon> extends AbstractItemSerializer<R> {
        @Override
        public byte[] write(R record) {
            byte[] recordBytes;
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bos.write(super.write(record));
                writeString(bos, record.getWeaponType());
                writeFloat(bos, record.getAttackPower());
                writeFloat(bos, record.getSpeedCost());
                writeFloat(bos, record.getCritChance());
                recordBytes = bos.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return recordBytes;
        }

        protected void readFields(ByteArrayInputStream bis, AbstractWeaponBuilder<?, ?> builder) throws IOException {
            super.readFields(bis, builder);
            String weaponType = getString(bis);
            Float attackPower = getFloat(bis);
            Float speedCost = getFloat(bis);
            Float critChance = getFloat(bis);
            builder
                    .weaponType(weaponType)
                    .attackPower(attackPower)
                    .speedCost(speedCost)
                    .critChance(critChance);
        }
    }

}
