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

class EndResponseMessage
    extends AbstractAjpMessage
{
    final boolean reuse;

    EndResponseMessage(boolean reuse) {
        super(Constants.PACKET_TYPE_END_RESPONSE);
        this.reuse = reuse;
        writeBoolean(reuse);
    }

    @Override
    public String toString() {
        return String.format("Reuse? %sreuse", reuse ? "Yes" : "No");
    }

    static EndResponseMessage readFrom(InputStream in) throws IOException {
        return new EndResponseMessage(AjpReader.readBoolean(in));
    }

    @Override
    public String getName() {
        return "End Response";
    }

    @Override
    public String getDescription() {
        return "Marks the end of the response (and thus the request-handling cycle). "+ this.toString();
    }

}
