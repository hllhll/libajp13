/*
 * libajp13 - BodyMessage.java
 *
 * Copyright (c) 2015 Luca Carettoni
 * Copyright (c) 2010 Espen Wiborg
 *
 * Licensed under the Apache License, Version 2.0
 */
package org.nibblesec.ajp13;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * AJP's BodyMessage type, from the web server to the J2EE container
 */
public class BodyMessage
        extends AbstractAjpMessage
{

    final int length;
    final byte[] bytes;

    /**
     * BodyMessage constructor
     *
     * @param bytes[] The body message content (data request)
     * @throws IOException
     * @return Instance of BodyMessage
     */
    public BodyMessage(byte[] bytes) throws IOException
    {
        super(Constants.PACKET_TYPE_DATA);
        this.length = bytes.length;
        writeInt(length);
        this.bytes = bytes;
        writeBytes(bytes);
    }

    static BodyMessage readFrom(InputStream in) throws IOException
    {
        int length = AjpReader.readInt(in);
        byte[] bytes = AjpReader.readBytes(length, in);
        return new BodyMessage(bytes);
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

    /**
     * Returns the entire body content
     *
     * @return the body content
     */
    public byte[] getBody()
    {
        return bytes;
    }

    /**
     * Returns a meaningful name for the packet type
     *
     * @return Name of the packet type
     */
    @Override
    public String getName()
    {
        return "Body Data Message";
    }

    /**
     * Returns a description for the packet type
     *
     * @return Description of the packet type.
     */
    @Override
    public String getDescription()
    {
        return "Remaining request body data."
                + "\nContent (HEX):\n0x" + AjpReader.getHex(bytes)
                + "\nContent (Ascii):\n" + this.toString();
    }
}
