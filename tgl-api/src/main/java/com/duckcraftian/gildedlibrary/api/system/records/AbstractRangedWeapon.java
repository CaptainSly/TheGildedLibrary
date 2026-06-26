package com.duckcraftian.gildedlibrary.api.system.records;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class AbstractRangedWeapon extends AbstractWeapon {

    private final RecordReference<AbstractAmmunition> ammunition;

    protected AbstractRangedWeapon(AbstractRangedWeaponBuilder<?, ?> builder) {
        super(builder);
        this.ammunition = new RecordReference<>(builder.ammunition);
    }

    public RecordReference<AbstractAmmunition> getAmmunition() {
        return ammunition;
    }

    protected void fillBuilder(AbstractRangedWeaponBuilder<?, ?> builder) {
        super.fillBuilder(builder);
        builder.ammunition(this.ammunition.id());
    }

    public static abstract class AbstractRangedWeaponBuilder<T extends AbstractRangedWeaponBuilder<T, R>, R extends AbstractRangedWeapon> extends AbstractWeaponBuilder<T, R> {

        private String ammunition;

        public AbstractRangedWeaponBuilder() {
            this.recordType("ranged_weapons");
        }

        public T ammunition(String ammunition) {
            this.ammunition = ammunition;
            return self();
        }

    }

    public static abstract class AbstractRangedWeaponSerializer<R extends AbstractRangedWeapon> extends AbstractWeaponSerializer<R> {

        @Override
        public byte[] write(R record) {
            byte[] recordBytes;
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bos.write(super.write(record));
                writeString(bos, record.getAmmunition().id());
                recordBytes = bos.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return recordBytes;
        }

        protected void readFields(ByteArrayInputStream bis, AbstractRangedWeaponBuilder<?, ?> builder) throws IOException {
            super.readFields(bis, builder);
            String ammunition = getString(bis);

            builder
                    .ammunition(ammunition);
        }

    }

}