/*
 * EndResponseMessage.java
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

class EndResponseMessage
    extends AbstractAjpMessage
{
    final boolean reuse;

    EndResponseMessage(boolean reuse) {
        this.reuse = reuse;
    }

    @Override
    public String toString() {
        return String.format("END (%sreuse)", reuse ? "" : "don't ");
    }

    static EndResponseMessage readFrom(InputStream in) throws IOException {
        return new EndResponseMessage(AjpReader.readBoolean(in));
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
