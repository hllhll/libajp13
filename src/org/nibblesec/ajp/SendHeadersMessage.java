/*
 * SendHeadersMessage.java
 *
 * Copyright (c) 2015 Luca Carettoni
 * Copyright (c) 2010 Espen Wiborg
 *
 * Licensed under the Apache License, Version 2.0
 */
package org.nibblesec.ajp;

import java.io.IOException;
import java.io.InputStream;

import java.util.LinkedList;
import java.util.List;

class SendHeadersMessage
    extends AbstractAjpMessage
{
    final int statusCode;
    final String statusMessage;
    final List<Pair<String, String>> headers;

    SendHeadersMessage(int statusCode, String statusMessage, List<Pair<String, String>> headers) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.headers = headers;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(statusCode).append(" ").append(statusMessage).append("\n");
        for (Pair<String, String> header : headers) {
            ret.append(header.a).append(": ").append(header.b).append("\n");
        }
        ret.append("\n");
        return ret.toString();
    }
    
    static SendHeadersMessage readFrom(InputStream in) throws IOException {
        List<Pair<String, String>> headers = new LinkedList<Pair<String, String>>();
        int statusCode = AjpReader.readInt(in);
        String statusMessage = AjpReader.readString(in);
        int numHeaders = AjpReader.readInt(in);
        for (int i = 0; i < numHeaders; i++) {
            int b1 = AjpReader.readByte(in);
            int b2 = AjpReader.readByte(in);

            String name;
            if (b1 == 0xA0) {
                name = Constants.RESPONSE_HEADERS[b2];
            } else {
                name = AjpReader.readString(AjpReader.makeInt(b1, b2), in);
            }
            headers.add(Pair.make(name, AjpReader.readString(in)));
        }
        return new SendHeadersMessage(statusCode, statusMessage, headers);
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
