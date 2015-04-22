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

class DataMessage
        extends AbstractAjpMessage {

    private final byte[] bodyData;

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
    public String getName() {
        return "Data";
    }

    @Override
    public String getDescription() {
        return "Remaining body data. Content: 0x" + AjpReader.getHex(bodyData);
    }
}
