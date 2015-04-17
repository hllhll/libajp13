/*
 * ForwardRequestMessage.java
 *
 * Copyright (c) 2015 Luca Carettoni
 * Copyright (c) 2010 Espen Wiborg
 *
 * Licensed under the Apache License, Version 2.0
 */
package org.nibblesec.ajp;

import java.io.IOException;
import java.io.InputStream;

class GetBodyChunkMessage
    extends AbstractAjpMessage
{
    final int length;

    GetBodyChunkMessage(int length) {
        super(Constants.PACKET_TYPE_GET_BODY_CHUNK);
        this.length = length;
        writeInt(length);
    }

    static GetBodyChunkMessage readFrom(InputStream in) throws IOException {
        int length = AjpReader.readInt(in);
        return new GetBodyChunkMessage(length);
    }

    @Override
    public String getName() {
        return "Get Body Chunk";
    }

    @Override
    public String getDescription() {
        return "Get further data from the request if it hasn't all been transferred yet";
    }
}
