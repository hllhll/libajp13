/*
 * SendBodyChunkMessage.java
 *
 * Copyright (c) 2015 Luca Carettoni
 * Copyright (c) 2010 Espen Wiborg
 *
 * Licensed under the Apache License, Version 2.0
 */
package org.nibblesec.ajp;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

class SendBodyChunkMessage
    extends AbstractAjpMessage
{
    final int length;
    final byte[] bytes;

    SendBodyChunkMessage(int length, byte[] bytes) {
        this.length = length;
        this.bytes = bytes;
    }

    @Override
    public String toString() {
        try {
            return new String(bytes, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Your JVM is broken", e);
        }
    }

    static SendBodyChunkMessage readFrom(InputStream in) throws IOException {
        int length = AjpReader.readInt(in);
        byte[] bytes = new byte[length];
        AjpReader.fullyRead(bytes, in);
        return new SendBodyChunkMessage(length, bytes);
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getDescription() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getSize() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
