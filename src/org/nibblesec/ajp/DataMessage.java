/*
 * DataMessage.java
 *
 * Copyright (c) 2015 Luca Carettoni
 * Copyright (c) 2010 Espen Wiborg
 *
 * Licensed under the Apache License, Version 2.0
 */
package org.nibblesec.ajp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class DataMessage
        extends AbstractAjpMessage {

    private byte[] bodyData;

    DataMessage(byte[] bodyData) throws IOException {
        super(Constants.PACKET_TYPE_DATA);
        this.bodyData = bodyData;
        writeBytes(bodyData);
    }

    static DataMessage readFrom(InputStream in) throws IOException {
        byte[] bodyData = AjpReader.readBytes(in);
        return new DataMessage(bodyData);
    }
    
    @Override
    public final void writeTo(OutputStream out) throws IOException {
        writeBytes(bodyData);
        super.writeTo(out);
    }
    

    @Override
    public String getName() {
        return "Data";
    }

    @Override
    public String getDescription() {
        return "Remaining body data. Content: 0x" + AjpReader.getHex(bodyData);
    }
}
