/*
 * PingMessage.java
 *
 * Copyright (c) 2015 Luca Carettoni
 * Copyright (c) 2010 Espen Wiborg
 *
 * Licensed under the Apache License, Version 2.0
 */
package org.nibblesec.ajp;

import java.io.IOException;
import java.io.OutputStream;

class PingMessage
        extends AbstractAjpMessage {

    PingMessage() {
        super(Constants.PACKET_TYPE_PING);
    }

    @Override
    public void writeTo(OutputStream out) throws IOException {
        throw new UnsupportedOperationException("Not supported for a Ping packet");
    }
    
    @Override
    public String getName() {
        return "Ping";
    }

    @Override
    public String getDescription() {
        return "The web server asks the container to take control (secure login phase)";
    }

}
