package com.github.kamomesg.befriends;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class StringArrayItemTagType implements PersistentDataType<byte[], String[]> {
    private Charset charset;

    public StringArrayItemTagType(Charset charset) {
        this.charset = charset;
    }

    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public Class<String[]> getComplexType() {
        return String[].class;
    }

    @Override
    public byte[] toPrimitive(String[] complex, PersistentDataAdapterContext context) {
        byte[][] allStringBytes = new byte[complex.length][];
        int total = 0;
        for (int i = 0; i < allStringBytes.length; i++) {
            byte[] bytes = complex[i].getBytes(charset);
            allStringBytes[i] = bytes;
            total += bytes.length;
        }

        ByteBuffer buffer = ByteBuffer.allocate(total + allStringBytes.length * 4); //stores integers
        for (byte[] bytes : allStringBytes) {
            buffer.putInt(bytes.length);
            buffer.put(bytes);
        }

        return buffer.array();
    }

    @Override
    public String[] fromPrimitive(byte[] primitive, PersistentDataAdapterContext context) {
        ByteBuffer buffer = ByteBuffer.wrap(primitive);
        ArrayList<String> list = new ArrayList<>();

        while (buffer.remaining() > 0) {
            if (buffer.remaining() < 4) break;
            int stringLength = buffer.getInt();
            if (buffer.remaining() < stringLength) break;

            byte[] stringBytes = new byte[stringLength];
            buffer.get(stringBytes);

            list.add(new String(stringBytes, charset));
        }

        return list.toArray(new String[list.size()]);
    }
}
