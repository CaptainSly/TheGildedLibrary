package com.duckcraftian.gildedlibrary.api.system.serialization;

import com.duckcraftian.gildedlibrary.api.system.records.AbstractItem;
import com.duckcraftian.gildedlibrary.api.system.records.AbstractRecord;
import com.duckcraftian.gildedlibrary.api.system.records.AbstractWeapon;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 *
 * Used to Serialize and Deserialize {@code <R>} Type Records.
 * <p>
 * You must implement the {@code read}, {@code write} and {@code readFields} methods, you can use {@link AbstractItem}/{@link AbstractWeapon}'s Serializers as an example
 * <p>
 * When records are serialized, the engine serializes them inside archives with their fully qualified Record Id (MODID:RECORDTYPE:ITEMID). This is done automatically, and is something to note if you do anything with the VFS system.
 *
 * @param <R> ? extends {@link AbstractRecord}
 */
public abstract class AbstractRecordSerializer<R extends AbstractRecord> {

    public abstract byte[] write(R record);

    public abstract R read(byte[] recordBytes) throws IOException;

    protected abstract void readFields(ByteArrayInputStream bis, AbstractRecord.AbstractRecordBuilder<?, ?> builder) throws IOException;

    protected void writeString(ByteArrayOutputStream bos, String string) throws IOException {
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        int length = bytes.length;

        bos.write((length >> 24) & 0xFF);
        bos.write((length >> 16) & 0xFF);
        bos.write((length >> 8) & 0xFF);
        bos.write((length) & 0xFF);
        bos.write(bytes);
    }

    protected void writeFloat(ByteArrayOutputStream bos, Float value) throws IOException {
        int floatBits = Float.floatToIntBits(value);
        byte[] floatBytes = {
                (byte) (floatBits >> 24),
                (byte) (floatBits >> 16),
                (byte) (floatBits >> 8),
                (byte) (floatBits),
        };

        bos.write(floatBytes);
    }

    protected String getString(ByteArrayInputStream bin) throws IOException {
        byte[] lengthBytes = new byte[4];
        var _ = bin.read(lengthBytes);

        int length = (lengthBytes[0] << 24) | (lengthBytes[1] << 16) | (lengthBytes[2] << 8) | lengthBytes[3];
        byte[] stringBytes = new byte[length];
        var _ = bin.read(stringBytes);

        return new String(stringBytes);
    }

    protected Float getFloat(ByteArrayInputStream bin) throws IOException {
        byte[] floatByte = new byte[4];
        var _ = bin.read(floatByte);

        int floatBits = ((floatByte[0] & 0xFF) << 24) | ((floatByte[1] & 0xFF) << 16) | ((floatByte[2] & 0xFF) << 8) | floatByte[3];
        return Float.intBitsToFloat(floatBits);
    }

}
