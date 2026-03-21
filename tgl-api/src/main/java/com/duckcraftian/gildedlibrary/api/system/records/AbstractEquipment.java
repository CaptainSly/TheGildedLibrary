package com.duckcraftian.gildedlibrary.api.system.records;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class AbstractEquipment extends AbstractItem {

    private final String equipmentSlot;

    protected AbstractEquipment(AbstractEquipmentBuilder<?, ?> builder) {
        super(builder);
        this.equipmentSlot = builder.equipmentSlot;
    }

    protected void fillBuilder(AbstractEquipmentBuilder<?, ?> builder) {
        super.fillBuilder(builder);
        builder.equipmentSlot(this.equipmentSlot);
    }

    public String getEquipmentSlot() {
        return equipmentSlot;
    }

    public static abstract class AbstractEquipmentBuilder<T extends AbstractEquipmentBuilder<T, R>, R extends AbstractEquipment> extends AbstractItemBuilder<T, R> {

        private String equipmentScript;
        private String equipmentSlot;

        public T equipmentSlot(String equipmentSlot) {
            this.equipmentSlot = equipmentSlot;
            return self();
        }

        public T equipmentScript(String equipmentScript) {
            this.equipmentScript = equipmentScript;
            return self();
        }

    }

    public static abstract class AbstractEquipmentSerializer<R extends AbstractEquipment> extends AbstractItemSerializer<R> {
        @Override
        public byte[] write(R record) {
            byte[] recordBytes;
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bos.write(super.write(record));
                writeString(bos, record.getEquipmentSlot());
                recordBytes = bos.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return recordBytes;
        }

        protected void readFields(ByteArrayInputStream bis, AbstractEquipmentBuilder<?, ?> builder) throws IOException {
            super.readFields(bis, builder);
            String equipmentSlot = getString(bis);

            builder.equipmentSlot(equipmentSlot);
        }
    }


}
