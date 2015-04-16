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

    static final byte[] AJP_TAG = {0x12, 0x34};
    private final ByteArrayOutputStream bos;

    AbstractAjpMessage(int packetType) {
        bos = new ByteArrayOutputStream();
        bos.write(AJP_TAG, 0, AJP_TAG.length);
        // Write two placeholder getBytes for the length
        bos.write(0);
        bos.write(0);
        if (packetType != Constants.PACKET_TYPE_DATA) {
            bos.write(packetType);
        }
    }

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

    protected void writeString(String s) {
        if (s == null) {
            bos.write(-1);
        } else {
            // size (2 bytes) + string + \0
            writeInt(s.length());
            try {
                byte[] buf = s.getBytes("UTF-8");
                bos.write(buf, 0, buf.length);
                bos.write('\0');
            } catch (UnsupportedEncodingException ex) {
                System.out.println("[KO] AbstractAjpMessage UnsupportedEncodingException: " + ex.getLocalizedMessage());
            }
        }
    }
}
