/*
 * libajp13 - ShutdownMessage.java
 *
 * Copyright (c) 2015 Luca Carettoni 
 * Copyright (c) 2010 Espen Wiborg
 *
 * Licensed under the Apache License, Version 2.0
 */
package org.nibblesec.ajp13;

/**
 * AJP's Shutdown message, from the web server to the J2EE container
 */
public class ShutdownMessage
        extends AbstractAjpMessage
{

    /**
     * ShutdownMessage constructor
     *
     * @return Instance of ShutdownMessage
     */
    public ShutdownMessage()
    {
        super(Constants.PACKET_TYPE_SHUTDOWN);
    }

    /**
     * Returns a meaningful name for the packet type
     *
     * @return Name of the packet type
     */
    @Override
    public String getName()
    {
        return "Shutdown";
    }

    /**
     * Returns a description for the packet type
     *
     * @return Description of the packet type.
     */
    @Override
    public String getDescription()
    {
        return "The web server asks the container to shut itself down.\n(Hopefully) "
                + "the container will only perform the Shutdown if the request comes "
                + "from the same machine";
    }
}
