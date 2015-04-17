/*
 * CPingMessage.java
 *
 * Copyright (c) 2015 Luca Carettoni
 * Copyright (c) 2010 Espen Wiborg
 *
 * Licensed under the Apache License, Version 2.0
 */
package org.nibblesec.ajp;

class CPingMessage
    extends AbstractAjpMessage
{
    CPingMessage() {
        super(Constants.PACKET_TYPE_CPING); 
    }

    @Override
    public String getName() {
        return "CPing";
    }

    @Override
    public String getDescription() {
        return "The web server asks the container to respond quickly with a CPong";
    }

}
