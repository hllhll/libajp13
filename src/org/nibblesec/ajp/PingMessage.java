/*
 * PingMessage.java
 *
 * Copyright (c) 2015 Luca Carettoni
 * Copyright (c) 2010 Espen Wiborg
 *
 * Licensed under the Apache License, Version 2.0
 */
package org.nibblesec.ajp;

/*
 * This class represents a Ping (not CPing!) packet from the server to the container.
 * @TODO Reverse any implementation of the ping type handler
 */
class PingMessage
        extends AbstractAjpMessage
{

    PingMessage()
    {
        super(Constants.PACKET_TYPE_PING);
    }

    @Override
    public String getName()
    {
        return "Ping";
    }

    @Override
    public String getDescription()
    {
        return "The web server asks the container to take control (secure login phase)";
    }
}
