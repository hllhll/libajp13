/*
 * BodyMessage.java
 *
 * Copyright (c) 2015 Luca Carettoni
 * Copyright (c) 2010 Espen Wiborg
 *
 * Licensed under the Apache License, Version 2.0
 */
package org.nibblesec.ajp;

import java.io.IOException;
import java.io.InputStream;

/*
 * This class represents a request body packet from the server to the container
 */
class BodyMessage
        extends AbstractAjpMessage
{

    final int length;
    final byte[] bytes;

    BodyMessage(int length, byte[] bytes) throws IOException
    {
        super(Constants.PACKET_TYPE_DATA);
        this.length = length;
        writeInt(length);
        this.bytes = bytes;
        writeBytes(bytes);
    }

    static BodyMessage readFrom(InputStream in) throws IOException
    {
        int length = AjpReader.readInt(in);
        byte[] bytes = AjpReader.readBytes(in);
        return new BodyMessage(length, bytes);
    }

    @Override
    public String getName()
    {
        return "Body Data Message";
    }

    @Override
    public String getDescription()
    {
        return "Remaining request body data.\nContent:\n0x" + AjpReader.getHex(bytes);
    }
}
