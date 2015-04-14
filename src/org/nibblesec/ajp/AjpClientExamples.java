/*
 * AjpClientExamples.java
 *
 * Copyright (c) 2015 Luca Carettoni
 * Copyright (c) 2010 Espen Wiborg
 *
 * Licensed under the Apache License, Version 2.0
 */
package org.nibblesec.ajp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;

/*
 * Send and Retrieve AJP packets - Test Cases
 * 
 * This class should give you an idea on how to use this AJP13 client library 
 */
public class AjpClientExamples {

    private String ip;
    private int port;

    AjpClientExamples() {
        this.ip = "127.0.0.1";
        this.port = 8009;
    }

    AjpClientExamples(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public static void main(String[] args) throws IOException {
        System.out.println("[*] AJP13 Client - Test Cases");
        System.out.println("[*] Luca Carettoni, Espen Wiborg\n");

        AjpClientExamples myClient;
        if (args.length == 2) {
            myClient = new AjpClientExamples(args[0], Integer.parseInt(args[1]));
        } else {
            myClient = new AjpClientExamples();
        }

        AjpMessage msg;
        byte[] replyByte;
        AjpMessage reply;

        // Test Case 1 - CPing/CPong
        msg = new CPingMessage();
        replyByte = myClient.send(msg.getBytes());
        reply = AjpReader.parseMessage(replyByte);
        if (reply instanceof CPongMessage) {
            System.out.println("[OK] Valid " + ((CPongMessage) reply).getName());
        }

        // Test Case 2 - Shutdown
        msg = new ShutdownMessage();
        replyByte = myClient.send(msg.getBytes());
        //reply = AjpReader.parseMessage(replyByte);
    }

    private byte[] send(byte[] data) throws UnsupportedEncodingException {
        System.out.println("\n[*]--------------------------------------------");
        System.out.println("--> SENDING");
        System.out.println("---> Hex: 0x" + getHex(data));
        System.out.println("---> Ascii: " + new String(data, "UTF-8"));

        //The max packet size is 8 * 1024 getBytes (8K)
        byte[] reply = new byte[8192];
        int size = 0;

        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(ip, port), 2000);
            socket.setSoTimeout(8000);
            DataOutputStream os = new DataOutputStream(socket.getOutputStream());
            DataInputStream is = new DataInputStream(socket.getInputStream());
            os.write(data);
            os.flush();
            size = is.read(reply);
            socket.close();
        } catch (IOException ex) {
            System.out.println("[KO] AjpClientExamples IOException: " + ex.getLocalizedMessage());
        }

        if (size > 0) {
            System.out.println("--> RECEIVING");
            System.out.println("---> Hex: 0x" + getHex(Arrays.copyOfRange(reply, 0, size)));
            System.out.println("---> Ascii: " + new String(reply, "UTF-8"));
        }
        System.out.println("[*]--------------------------------------------");
        
        return reply;
    }

    private static String getHex(byte[] raw) {
        final String HEXES = "0123456789ABCDEF";
        if (raw == null) {
            return null;
        }
        final StringBuilder hex = new StringBuilder(2 * raw.length);
        for (final byte b : raw) {
            hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F)));
        }
        return hex.toString();
    }
}
