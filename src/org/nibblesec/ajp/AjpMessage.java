/*
 * AjpMessage.java
 *
 * Copyright (c) 2015 Luca Carettoni
 * Copyright (c) 2010 Espen Wiborg
 *
 * Licensed under the Apache License, Version 2.0
 */
package org.nibblesec.ajp;

interface AjpMessage {

    byte[] getBytes(); //returns the raw bytes

    String getName(); //returns a meaningful name for the packet type

    String getDescription(); //returns a description for the packet type, with actual field values
}
