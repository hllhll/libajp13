/*
 * AjpMessageTest.java
 *
 * Copyright (c) 2015 Luca Carettoni
 * Copyright (c) 2010 Espen Wiborg
 *
 * Licensed under the Apache License, Version 2.0
 */
package org.nibblesec.ajp;

import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;
import static org.junit.Assert.*;

/*
 * Test cases for all AJP messages
 */
public class AjpMessageTest
{

    @Test
    public void cpingMessageIsTypeRequest() throws Exception
    {
        AjpMessage cping = new CPingMessage();
        byte[] cpingBytes = cping.getBytes();
        assertThat(cpingBytes[0], is((byte) 18));
        assertThat(cpingBytes[1], is((byte) 52));
    }
    
    @Test
    public void cpingMessageIsCorrectLength() throws Exception
    {
        AjpMessage cping = new CPingMessage();
        byte[] cpingBytes = cping.getBytes();
        assertThat(cpingBytes.length, is(5));
    }

    @Test
    public void cpingMessagePayloadLengthIsOne() throws Exception
    {
        AjpMessage cping = new CPingMessage();
        byte[] cpingBytes = cping.getBytes();
        assertThat(cpingBytes[2], is((byte) 0));
        assertThat(cpingBytes[3], is((byte) 1));
    }
    
    @Test
    public void cpongMessageIsTypeResponse() throws Exception
    {
        AjpMessage cpong = new CPongMessage();
        byte[] cpongBytes = cpong.getBytes();
        assertThat(cpongBytes[0], is((byte) 65));
        assertThat(cpongBytes[1], is((byte) 66));
    }
    
    @Test
    public void cpongMessageIsCorrectLength() throws Exception
    {
        AjpMessage cpong = new CPongMessage();
        byte[] cpongBytes = cpong.getBytes();
        assertThat(cpongBytes.length, is(5));
    }

    @Test
    public void cpongMessagePayloadLengthIsOne() throws Exception
    {
        AjpMessage cpong = new CPongMessage();
        byte[] cpongBytes = cpong.getBytes();
        assertThat(cpongBytes[2], is((byte) 0));
        assertThat(cpongBytes[3], is((byte) 1));
    }
    
    @Test
    public void shutdownMessageIsTypeRequest() throws Exception
    {
        AjpMessage shutdown = new ShutdownMessage();
        byte[] shutdownBytes = shutdown.getBytes();
        assertThat(shutdownBytes[0], is((byte) 18));
        assertThat(shutdownBytes[1], is((byte) 52));
    }
    
    @Test
    public void shutdownMessageIsCorrectLength() throws Exception
    {
        AjpMessage shutdown = new ShutdownMessage();
        byte[] shutdownBytes = shutdown.getBytes();
        assertThat(shutdownBytes.length, is(5));
    }

    @Test
    public void shutdownMessagePayloadLengthIsOne() throws Exception
    {
        AjpMessage shutdown = new ShutdownMessage();
        byte[] shutdownBytes = shutdown.getBytes();
        assertThat(shutdownBytes[2], is((byte) 0));
        assertThat(shutdownBytes[3], is((byte) 1));
    }
    
    @Test
    public void pingMessageIsTypeRequest() throws Exception
    {
        AjpMessage ping = new PingMessage();
        byte[] pingBytes = ping.getBytes();
        assertThat(pingBytes[0], is((byte) 18));
        assertThat(pingBytes[1], is((byte) 52));
    }
    
    @Test
    public void pingMessageIsCorrectLength() throws Exception
    {
        AjpMessage ping = new PingMessage();
        byte[] pingBytes = ping.getBytes();
        assertThat(pingBytes.length, is(5));
    }

    @Test
    public void pingMessagePayloadLengthIsOne() throws Exception
    {
        AjpMessage ping = new PingMessage();
        byte[] pingBytes = ping.getBytes();
        assertThat(pingBytes[2], is((byte) 0));
        assertThat(pingBytes[3], is((byte) 1));
    }
    
    @Test
    public void endResponseMessageIsTypeResponse() throws Exception
    {
        AjpMessage endResponse = new EndResponseMessage(true);
        byte[] endResponseBytes = endResponse.getBytes();
        assertThat(endResponseBytes[0], is((byte) 65));
        assertThat(endResponseBytes[1], is((byte) 66));
    }
    
    @Test
    public void endResponseMessagePayloadLengthIsTwo() throws Exception
    {
        AjpMessage endResponse = new EndResponseMessage(false);
        byte[] endResponseBytes = endResponse.getBytes();
        assertThat(endResponseBytes[2], is((byte) 0));
        assertThat(endResponseBytes[3], is((byte) 2));
    }
    
    @Test
    public void sendBodyChunkMessageIsTypeResponse() throws Exception
    {
        AjpMessage sendBodyChunk = new SendBodyChunkMessage(4,"ABCD".getBytes());
        byte[] sendBodyChunkBytes = sendBodyChunk.getBytes();
        assertThat(sendBodyChunkBytes[0], is((byte) 65));
        assertThat(sendBodyChunkBytes[1], is((byte) 66));
    }
    
    @Test
    public void sendBodyChunkMessageIsCorrectLength() throws Exception
    {
        AjpMessage sendBodyChunk = new SendBodyChunkMessage(4,"ABCD".getBytes());
        byte[] sendBodyChunkBytes = sendBodyChunk.getBytes();
        assertThat(sendBodyChunkBytes[2], is((byte) 0)); //payload size
        assertThat(sendBodyChunkBytes[3], is((byte) 7));
        //We assume no trailing null byte. In some implementations, it's null-byte terminated!?
        assertThat(sendBodyChunkBytes[5], is((byte) 0)); //binary data size.  
        assertThat(sendBodyChunkBytes[6], is((byte) 4)); 
        assertThat(sendBodyChunkBytes.length, is(11)); //total packet size
    }
    
    //TODO SendHeaders
}