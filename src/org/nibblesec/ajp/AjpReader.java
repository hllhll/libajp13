/*
 * AjpReader.java
 *
 * Copyright (c) 2015 Luca Carettoni
 * Copyright (c) 2010 Espen Wiborg
 *
 * Licensed under the Apache License, Version 2.0
 */
package org.nibblesec.ajp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

final class AjpReader {

    private AjpReader() {
    }

    static public AjpMessage parseMessage(byte[] reply) throws IOException {
        //AB + size (2 bytes) + [type]
        InputStream in = new ByteArrayInputStream(reply);
        consume('A', in);
        consume('B', in);
        int length = readInt(in);
        if (length <= 0) return null; //empty AjpMessage
        int type = in.read();
        byte[] bytes = new byte[length - 1];
        fullyRead(bytes, in);

        switch (type) {
            //From WebServer to Containers
            case Constants.PACKET_TYPE_CPING:
                return new CPingMessage();
            case Constants.PACKET_TYPE_FORWARD_REQUEST:
                return ForwardRequestMessage.readFrom(new ByteArrayInputStream(reply));
            case Constants.PACKET_TYPE_SHUTDOWN:
                return new ShutdownMessage();
            case Constants.PACKET_TYPE_PING:
                return new PingMessage();
            //From Containers to WebServer
            case Constants.PACKET_TYPE_CPONG:
                return new CPongMessage();
            case Constants.PACKET_TYPE_SEND_HEADERS:
                return SendHeadersMessage.readFrom(new ByteArrayInputStream(reply));
            case Constants.PACKET_TYPE_SEND_BODY_CHUNK:
                return SendBodyChunkMessage.readFrom(new ByteArrayInputStream(reply));
            case Constants.PACKET_TYPE_GET_BODY_CHUNK:
                return GetBodyChunkMessage.readFrom(new ByteArrayInputStream(reply));
            case Constants.PACKET_TYPE_END_RESPONSE:
                return EndResponseMessage.readFrom(new ByteArrayInputStream(reply));
            default:
                //Probably a Data packet (none code)
                return BodyMessage.readFrom(new ByteArrayInputStream(reply));
        }
    }

    static int readInt(InputStream in) throws IOException {
        byte[] buf = new byte[2];
        fullyRead(buf, in);
        return makeInt(buf[0], buf[1]);
    }

    static int makeInt(int b1, int b2) {
        return b1 << 8 | (b2 & 0xff);
    }

    static String readString(InputStream in) throws IOException {
        int len = readInt(in);
        return readString(len, in);
    }

    static String readString(int len, InputStream in) throws IOException {
        return new String(readBytes(len, in), "UTF-8");
    }

    static byte[] readBytes(InputStream in) throws IOException {
        int len = readInt(in);
        return readBytes(len, in);
    }

    private static byte[] readBytes(int len, InputStream in) throws IOException {
        if (len < 1) {
            return null;
        }
        byte[] buf = new byte[len];
        fullyRead(buf, in);
        // Skip the terminating \0
        in.read();
        return buf;
    }

    static void fullyRead(byte[] buffer, InputStream in) throws IOException {
        int totalRead = 0;
        int read = 0;
        while ((read = in.read(buffer, totalRead, buffer.length - totalRead)) > 0) {
            totalRead += read;
        }
        if (totalRead != buffer.length) {
            //ShortAjpRead
            System.out.println("[KO] AjpReader Short Read. Buffer: " + buffer.length + ", Read: " + totalRead);
        }
    }

    static int readByte(InputStream in) throws IOException {
        return in.read();
    }

    static boolean readBoolean(InputStream in) throws IOException {
        return readByte(in) > 0;
    }

    private static void consume(int expected, InputStream in) throws IOException {
        int readByte = readByte(in);
        if (readByte != expected) {
            System.out.println("[KO] AjpReader Unexpected Byte: " + readByte);
        }
    }
    
    //Convert a byte array to a string representation of hex values
    static String getHex(byte[] raw) {
        final String HEXES = "0123456789ABCDEF";
        if (raw == null) {
            return null;
        }
        final StringBuilder hex = new StringBuilder(2 * raw.length);
        for (final byte b : raw) {
            hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F)));
        }
        return hex.toString();
    }
    
    //Convert a string representation of hex values to byte array
    static byte[] toHex(String s) {
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                             + Character.digit(s.charAt(i+1), 16));
    }
    return data;
}
}
