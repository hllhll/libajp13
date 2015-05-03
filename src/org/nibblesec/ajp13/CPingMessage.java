/*
 * libajp13 - CPingMessage.java
 *
 * Copyright (c) 2015 Luca Carettoni
 * Copyright (c) 2010 Espen Wiborg
 *
 * Licensed under the Apache License, Version 2.0
 */
package org.nibblesec.ajp13;

/**
 * AJP's CPing packet, from the web server to the J2EE container
 */
public class CPingMessage
        extends AbstractAjpMessage
{

    /**
     * CPingMessage constructor
     *
     * @return Instance of CPingMessage
     */
    public CPingMessage()
    {
        super(Constants.PACKET_TYPE_CPING);
    }

    /**
     * Returns a meaningful name for the packet type
     *
     * @return Name of the packet type
     */
    @Override
    public String getName()
    {
        return "CPing";
    }

    /**
     * Returns a description for the packet type
     *
     * @return Description of the packet type.
     */
    @Override
    public String getDescription()
    {
        return "The web server asks the container to respond quickly with a CPong";
    }
}
