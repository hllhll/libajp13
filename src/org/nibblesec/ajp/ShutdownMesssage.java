/*
 * ShutdownMessage.java
 *
 * Copyright (c) 2015 Luca Carettoni
 * Copyright (c) 2010 Espen Wiborg
 *
 * Licensed under the Apache License, Version 2.0
 */
package org.nibblesec.ajp;

/*
 * This class represents AJP's Shutdown message, from the web server to the container
 */
class ShutdownMessage
        extends AbstractAjpMessage
{

    ShutdownMessage()
    {
        super(Constants.PACKET_TYPE_SHUTDOWN);
    }

    @Override
    public String getName()
    {
        return "Shutdown";
    }

    @Override
    public String getDescription()
    {
        return "The web server asks the container to shut itself down.\n(Hopefully) the container "
                + "will only perform the Shutdown if the request comes from the "
                + "same machine on which it's hosted";
    }
}
