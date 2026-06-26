package com.duckcraftian.gildedlibrary.api.system.records;

import com.duckcraftian.gildedlibrary.api.assets.AbstractModelAsset;
import com.duckcraftian.gildedlibrary.api.assets.AssetReference;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class AbstractAmmunition extends AbstractItem {

    private String weaponType;

    protected AbstractAmmunition(AbstractAmmunitionBuilder<?, ?> builder) {
        super(builder);
        this.weaponType = builder.weaponType;
    }

    public String getWeaponType() {
        return weaponType;
    }

    public static abstract class AbstractAmmunitionBuilder<T extends AbstractItemBuilder<T, R>, R extends AbstractAmmunition> extends AbstractItemBuilder<T, R> {

        private String weaponType;

        public AbstractAmmunitionBuilder() {
            this.recordType("ammo");
        }

        public T weaponType(String weaponType) {
            this.weaponType = weaponType;
            return self();
        }

    }

    public static abstract class AbstractAmmunitionSerializer<R extends AbstractAmmunition> extends AbstractItemSerializer<R> {

        @Override
        public byte[] write(R record) {
            byte[] recordBytes;
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bos.write(super.write(record));
                writeString(bos, record.getWeaponType());
                recordBytes = bos.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return recordBytes;
        }

        protected void readFields(ByteArrayInputStream bis, AbstractAmmunitionBuilder<?, ?> builder) throws IOException {
            super.readFields(bis, builder);

        }

    }

}