/*
 * AjpReaderTest.java
 *
 * Copyright (c) 2015 Luca Carettoni
 * Copyright (c) 2010 Espen Wiborg
 *
 * Licensed under the Apache License, Version 2.0
 */
package org.nibblesec.ajp;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/*
 * Test cases for AJP message parsing
 */
public class AjpReaderTest
{

    @Test
    public void makeIntWorks() throws Exception
    {
        assertThat(AjpReader.makeInt(0, 1), is(1));
        assertThat(AjpReader.makeInt(1, 0), is(256));
    }

    @Test
    public void parseCping() throws Exception
    {
        byte[] cPing = new byte[]{
            (byte) 0x12, (byte) 0x34, (byte) 0x00, (byte) 0x01, (byte) 0x0a};
        AjpMessage msg = AjpReader.parseMessage(cPing);
        assertThat(msg, instanceOf(CPingMessage.class));
    }

    @Test
    public void parseCpong() throws Exception
    {
        byte[] cPong = new byte[]{
            (byte) 0x41, (byte) 0x42, (byte) 0x00, (byte) 0x01, (byte) 0x09};
        AjpMessage msg = AjpReader.parseMessage(cPong);
        assertThat(msg, instanceOf(CPongMessage.class));
    }

    @Test
    public void parseShutdown() throws Exception
    {
        byte[] shutdown = new byte[]{
            (byte) 0x12, (byte) 0x34, (byte) 0x00, (byte) 0x01, (byte) 0x07};
        AjpMessage msg = AjpReader.parseMessage(shutdown);
        assertThat(msg, instanceOf(ShutdownMessage.class));
    }

    @Test
    public void parsePing() throws Exception
    {
        byte[] ping = new byte[]{
            (byte) 0x12, (byte) 0x34, (byte) 0x00, (byte) 0x01, (byte) 0x08};
        AjpMessage msg = AjpReader.parseMessage(ping);
        assertThat(msg, instanceOf(PingMessage.class));
    }

    @Test
    public void parseEndResponse() throws Exception
    {
        byte[] endResponse = new byte[]{
            (byte) 0x41, (byte) 0x42, (byte) 0x00, (byte) 0x02, (byte) 0x05, (byte) 0x00};
        AjpMessage msg = AjpReader.parseMessage(endResponse);
        assertThat(msg, instanceOf(EndResponseMessage.class));
    }

    @Test
    public void parseSendBodyChunk() throws Exception
    {
        byte[] sendBodyChunk = new byte[]{
            (byte) 0x41, (byte) 0x42, (byte) 0x00, (byte) 0x31, (byte) 0x03,
            (byte) 0x00, (byte) 0x2d, (byte) 0x83, (byte) 0x71, (byte) 0x5e,
            (byte) 0x60, (byte) 0xc6, (byte) 0x0c, (byte) 0x33, (byte) 0xc3,
            (byte) 0x39, (byte) 0x4d, (byte) 0x59, (byte) 0x5a, (byte) 0x6e,
            (byte) 0xe5, (byte) 0x35, (byte) 0x1d, (byte) 0xee, (byte) 0x4c,
            (byte) 0xfc, (byte) 0xd2, (byte) 0xbd, (byte) 0x45, (byte) 0x7f,
            (byte) 0xe9, (byte) 0xde, (byte) 0x72, (byte) 0xe9, (byte) 0x81,
            (byte) 0x41, (byte) 0x78, (byte) 0xfb, (byte) 0x5b, (byte) 0xab,
            (byte) 0x85, (byte) 0x34, (byte) 0xfe, (byte) 0x1f, (byte) 0xb9,
            (byte) 0xa9, (byte) 0x67, (byte) 0x69, (byte) 0x92, (byte) 0x7f,
            (byte) 0x00, (byte) 0x00, (byte) 0x00};
        AjpMessage msg = AjpReader.parseMessage(sendBodyChunk);
        assertThat(msg, instanceOf(SendBodyChunkMessage.class));
    }

    @Test
    public void parseSendHeaders() throws Exception
    {
        byte[] sendHeaders = new byte[]{
            (byte) 0x41, (byte) 0x42, (byte) 0x00, (byte) 0xc7, (byte) 0x04,
            (byte) 0x00, (byte) 0xc8, (byte) 0x00, (byte) 0x02, (byte) 0x4f,
            (byte) 0x4b, (byte) 0x00, (byte) 0x00, (byte) 0x07, (byte) 0xa0,
            (byte) 0x05, (byte) 0x00, (byte) 0x1d, (byte) 0x53, (byte) 0x75,
            (byte) 0x6e, (byte) 0x2c, (byte) 0x20, (byte) 0x30, (byte) 0x31,
            (byte) 0x20, (byte) 0x4d, (byte) 0x61, (byte) 0x72, (byte) 0x20,
            (byte) 0x32, (byte) 0x30, (byte) 0x31, (byte) 0x35, (byte) 0x20,
            (byte) 0x30, (byte) 0x36, (byte) 0x3a, (byte) 0x33, (byte) 0x31,
            (byte) 0x3a, (byte) 0x32, (byte) 0x31, (byte) 0x20, (byte) 0x47,
            (byte) 0x4d, (byte) 0x54, (byte) 0x00, (byte) 0x00, (byte) 0x07,
            (byte) 0x45, (byte) 0x78, (byte) 0x70, (byte) 0x69, (byte) 0x72,
            (byte) 0x65, (byte) 0x73, (byte) 0x00, (byte) 0x00, (byte) 0x1d,
            (byte) 0x54, (byte) 0x75, (byte) 0x65, (byte) 0x2c, (byte) 0x20,
            (byte) 0x32, (byte) 0x36, (byte) 0x20, (byte) 0x41, (byte) 0x70,
            (byte) 0x72, (byte) 0x20, (byte) 0x32, (byte) 0x30, (byte) 0x31,
            (byte) 0x36, (byte) 0x20, (byte) 0x30, (byte) 0x34, (byte) 0x3a,
            (byte) 0x32, (byte) 0x34, (byte) 0x3a, (byte) 0x33, (byte) 0x31,
            (byte) 0x20, (byte) 0x47, (byte) 0x4d, (byte) 0x54, (byte) 0x00,
            (byte) 0x00, (byte) 0x0d, (byte) 0x41, (byte) 0x63, (byte) 0x63,
            (byte) 0x65, (byte) 0x70, (byte) 0x74, (byte) 0x2d, (byte) 0x52,
            (byte) 0x61, (byte) 0x6e, (byte) 0x67, (byte) 0x65, (byte) 0x73,
            (byte) 0x00, (byte) 0x00, (byte) 0x05, (byte) 0x62, (byte) 0x79,
            (byte) 0x74, (byte) 0x65, (byte) 0x73, (byte) 0x00, (byte) 0xa0,
            (byte) 0x01, (byte) 0x00, (byte) 0x09, (byte) 0x69, (byte) 0x6d,
            (byte) 0x61, (byte) 0x67, (byte) 0x65, (byte) 0x2f, (byte) 0x70,
            (byte) 0x6e, (byte) 0x67, (byte) 0x00, (byte) 0x00, (byte) 0x16,
            (byte) 0x58, (byte) 0x2d, (byte) 0x43, (byte) 0x6f, (byte) 0x6e,
            (byte) 0x74, (byte) 0x65, (byte) 0x6e, (byte) 0x74, (byte) 0x2d,
            (byte) 0x54, (byte) 0x79, (byte) 0x70, (byte) 0x65, (byte) 0x2d,
            (byte) 0x4f, (byte) 0x70, (byte) 0x74, (byte) 0x69, (byte) 0x6f,
            (byte) 0x6e, (byte) 0x73, (byte) 0x00, (byte) 0x00, (byte) 0x07,
            (byte) 0x6e, (byte) 0x6f, (byte) 0x73, (byte) 0x6e, (byte) 0x69,
            (byte) 0x66, (byte) 0x66, (byte) 0x00, (byte) 0xa0, (byte) 0x03,
            (byte) 0x00, (byte) 0x03, (byte) 0x38, (byte) 0x31, (byte) 0x34,
            (byte) 0x00, (byte) 0x00, (byte) 0x06, (byte) 0x53, (byte) 0x65,
            (byte) 0x72, (byte) 0x76, (byte) 0x65, (byte) 0x72, (byte) 0x00,
            (byte) 0x00, (byte) 0x14, (byte) 0x53, (byte) 0x65, (byte) 0x72,
            (byte) 0x76, (byte) 0x65, (byte) 0x72, (byte) 0x3a, (byte) 0x20,
            (byte) 0x4a, (byte) 0x65, (byte) 0x74, (byte) 0x74, (byte) 0x79,
            (byte) 0x28, (byte) 0x37, (byte) 0x2e, (byte) 0x78, (byte) 0x2e,
            (byte) 0x78, (byte) 0x29, (byte) 0x00};
        AjpMessage msg = AjpReader.parseMessage(sendHeaders);
        assertThat(msg, instanceOf(SendHeadersMessage.class));
        assertThat(msg.getDescription(), is("Send the response headers from the servlet container to the web server.\n"
                + "Content:\n"
                + "200 OK\n"
                + "Headers:\n"
                + "last-modified: Sun, 01 Mar 2015 06:31:21 GMT\n"
                + "Expires: Tue, 26 Apr 2016 04:24:31 GMT\n"
                + "Accept-Ranges: bytes\n"
                + "content-type: image/png\n"
                + "X-Content-Type-Options: nosniff\n"
                + "content-length: 814\n"
                + "Server: Server: Jetty(7.x.x)\n"));
    }
}
