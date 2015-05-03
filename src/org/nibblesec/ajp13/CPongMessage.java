/*
 * libajp13 - CPongMessage.java
 *
 * Copyright (c) 2015 Luca Carettoni
 * Copyright (c) 2010 Espen Wiborg
 *
 * Licensed under the Apache License, Version 2.0
 */
package org.nibblesec.ajp13;

/**
 * AJP's CPong message, from the J2EE container to the web server
 */
public class CPongMessage
        extends AbstractAjpMessage
{

    /**
     * CPongMessage constructor
     *
     * @return Instance of CPongMessage
     */
    public CPongMessage()
    {
        super(Constants.PACKET_TYPE_CPONG);
    }

    /**
     * Returns a meaningful name for the packet type
     *
     * @return Name of the packet type
     */
    @Override
    public String getName()
    {
        return "CPong";
    }

    /**
     * Returns a description for the packet type
     *
     * @return Description of the packet type.
     */
    @Override
    public String getDescription()
    {
        return "The reply to a CPing request";
    }
}
