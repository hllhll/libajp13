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
        this.length = length;
    }

    static GetBodyChunkMessage readFrom(InputStream in) throws IOException {
        int length = AjpReader.readInt(in);
        return new GetBodyChunkMessage(length);
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
