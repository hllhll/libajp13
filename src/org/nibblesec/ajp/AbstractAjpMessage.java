/*
 * AbstractAjpMessage.java
 *
 * Copyright (c) 2015 Luca Carettoni
 * Copyright (c) 2010 Espen Wiborg
 *
 * Licensed under the Apache License, Version 2.0
 */
package org.nibblesec.ajp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

abstract class AbstractAjpMessage
        implements AjpMessage {

    static final byte[] AJP_TAG_REQ = {0x12, 0x34};
    static final byte[] AJP_TAG_RESP = {0x41, 0x42};
    private final ByteArrayOutputStream bos;

    AbstractAjpMessage(int packetType) {
        bos = new ByteArrayOutputStream();
        if (packetType == Constants.PACKET_TYPE_DATA || packetType == Constants.PACKET_TYPE_FORWARD_REQUEST || packetType == Constants.PACKET_TYPE_SHUTDOWN || packetType == Constants.PACKET_TYPE_PING || packetType == Constants.PACKET_TYPE_CPING) {
            bos.write(AJP_TAG_REQ, 0, AJP_TAG_REQ.length);
        } else {
            bos.write(AJP_TAG_RESP, 0, AJP_TAG_RESP.length);
        }
        // Write two placeholder getBytes for the length
        bos.write(0);
        bos.write(0);
        if (packetType != Constants.PACKET_TYPE_DATA) {
            bos.write(packetType);
        }
    }

    @Override
    public void writeTo(OutputStream out) throws IOException {
        out.write(getBytes());
        out.flush();
    }

    @Override
    public final byte[] getBytes() {
        byte[] bytes = bos.toByteArray();
        int length = bytes.length - 4;
        if (length == -1) {
            bytes[2] = -1;
            bytes[3] = -1;
        } else {
            bytes[2] = (byte) ((length & 0xff00) >> 8);
            bytes[3] = (byte) (length & 0x00ff);
        }
        return bytes;
    }

    protected void writeByte(int b) {
        bos.write(b);
    }

    protected void writeBytes(byte[] ba) throws IOException {
        bos.write(ba);
    }

    protected void writeInt(int i) {
        bos.write((i & 0xff00) >> 8);
        bos.write(i & 0x00ff);
    }

    protected void writeBoolean(boolean b) {
        bos.write(b ? 1 : 0);
    }

    protected void writeString(String s, boolean term) {
        if (s == null) {
            bos.write(0);
        } else {
            // size (2 bytes) + string + \0
            writeInt(s.length());
            try {
                byte[] buf = s.getBytes("UTF-8");
                bos.write(buf, 0, buf.length);
                //From my experiments, the bodyMessage packet doesn't contain the string terminator (bad implementation?)
                if(term) bos.write('\0');
            } catch (UnsupportedEncodingException ex) {
                System.out.println("[KO] AbstractAjpMessage UnsupportedEncodingException: " + ex.getLocalizedMessage());
            }
        }
    }
}

class Pair<T, U> {

    final T a;
    final U b;

    Pair(T a, U b) {
        this.a = a;
        this.b = b;
    }

    static <K, V> Pair<K, V> make(K k, V v) {
        return new Pair<K, V>(k, v);
    }
}
