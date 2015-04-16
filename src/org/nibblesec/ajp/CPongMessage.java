/*
 * CPongMessage.java
 *
 * Copyright (c) 2015 Luca Carettoni
 * Copyright (c) 2010 Espen Wiborg
 *
 * Licensed under the Apache License, Version 2.0
 */
package org.nibblesec.ajp;

import java.io.IOException;
import java.io.OutputStream;

class CPongMessage
        extends AbstractAjpMessage {

    CPongMessage() {
        super(Constants.PACKET_TYPE_CPONG);
    }
    
    @Override
    public void writeTo(OutputStream out) throws IOException {
        throw new UnsupportedOperationException("Not supported for a CPong packet."); 
    }

    @Override
    public String getName() {
        return "CPong";
    }

    @Override
    public String getDescription() {
        return "The reply to a CPing request";
    }
    
}
