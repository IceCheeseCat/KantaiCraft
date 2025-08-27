package com.github.icecheesecat.kantaicraft.network.packet;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class FriendlyByteBufHelper {

    public static byte[] encodeInt(int[] intArray) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(intArray.length * 4);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(intArray);

        return byteBuffer.array();
    }

    public static int[] decodeInt(byte[] byteArray) {
        IntBuffer intBuffer = IntBuffer.allocate(byteArray.length / 4);
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);
        intBuffer.put(byteBuffer.asIntBuffer());


        return intBuffer.array();
    }

}
