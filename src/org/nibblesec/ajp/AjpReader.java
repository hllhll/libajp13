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
        int type = reply[4];

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
                return DataMessage.readFrom(new ByteArrayInputStream(reply));
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
        if (len < 1) {
            return null;
        }
        byte[] buf = new byte[len];
        fullyRead(buf, in);
        // Skip the terminating \0
        in.read();
        return new String(buf, "UTF-8");
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
}