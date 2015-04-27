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

/*
 * This class represents AJP's Send Body Chunk message, from the container to the web server
 */
class SendBodyChunkMessage
        extends AbstractAjpMessage
{

    final int length;
    final byte[] bytes;

    SendBodyChunkMessage(int length, byte[] bytes) throws IOException
    {
        super(Constants.PACKET_TYPE_SEND_BODY_CHUNK);
        this.length = length;
        writeInt(length);
        this.bytes = bytes;
        writeBytes(bytes);
    }

    @Override
    public String toString()
    {
        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            System.out.println("[!] SendBodyChunkMessage UnsupportedEncodingException: " + ex.getLocalizedMessage());
            return "InvalidEncoding";
        }
    }

    static SendBodyChunkMessage readFrom(InputStream in) throws IOException
    {
        int length = AjpReader.readInt(in);
        byte[] bytes = new byte[length];
        AjpReader.fullyRead(bytes, in);
        return new SendBodyChunkMessage(length, bytes);
    }

    @Override
    public String getName()
    {
        return "Send Body Chunk";
    }

    @Override
    public String getDescription()
    {
        return "Send a chunk of the body from the servlet container to the web server.\nContent:\n0x" + AjpReader.getHex(bytes);
    }
}
