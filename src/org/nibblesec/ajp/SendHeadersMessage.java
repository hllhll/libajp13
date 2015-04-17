/*
 * SendHeadersMessage.java
 *
 * Copyright (c) 2015 Luca Carettoni
 * Copyright (c) 2010 Espen Wiborg
 *
 * Licensed under the Apache License, Version 2.0
 */
package org.nibblesec.ajp;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

class SendHeadersMessage
        extends AbstractAjpMessage {

    final int statusCode;
    final String statusMessage;
    final List<Pair<String, String>> headers;

    SendHeadersMessage(int statusCode, String statusMessage, List<Pair<String, String>> headers) throws IOException {
        super(Constants.PACKET_TYPE_SEND_HEADERS);
        this.statusCode = statusCode;
        writeInt(statusCode);
        this.statusMessage = statusMessage;
        writeString(statusMessage);
        this.headers = headers;
        int numHeaders = headers.size();
        writeInt(numHeaders);
        for (Pair<String, String> header : headers) {
            String eHeader = encodeHeaders(header.a); 
            if(eHeader.contains("A0")){
              //Send HeaderName as Byte
              byte[] headerBytes = new BigInteger(eHeader,16).toByteArray();
              writeBytes(headerBytes);
            }else{
              //Send HeaderName as String
              writeString(eHeader);  
            }
            //Send HeaderValue
            writeString(header.b);  
        }
    }

    private String encodeHeaders(String name){
        for(int i=0; i<Constants.RESPONSE_HEADERS.length; i++){
            if(Constants.RESPONSE_HEADERS[i].equalsIgnoreCase(name)){
                //return encoding code
                return "A00"+Integer.toHexString(i+1);
            }
        }
        //no match, return the original string
        return name;
    }
    
    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(statusCode).append(" ").append(statusMessage).append("\n");
        for (Pair<String, String> header : headers) {
            ret.append(header.a).append(": ").append(header.b).append("\n");
        }
        ret.append("\n");
        return ret.toString();
    }

    static SendHeadersMessage readFrom(InputStream in) throws IOException {
        List<Pair<String, String>> headers = new LinkedList<Pair<String, String>>();
        int statusCode = AjpReader.readInt(in);
        String statusMessage = AjpReader.readString(in);
        int numHeaders = AjpReader.readInt(in);
        for (int i = 0; i < numHeaders; i++) {
            int b1 = AjpReader.readByte(in);
            int b2 = AjpReader.readByte(in);

            String name;
            if (b1 == 0xA0) {
                name = Constants.RESPONSE_HEADERS[b2];
            } else {
                name = AjpReader.readString(AjpReader.makeInt(b1, b2), in);
            }
            headers.add(Pair.make(name, AjpReader.readString(in)));
        }
        return new SendHeadersMessage(statusCode, statusMessage, headers);
    }

    @Override
    public String getName() {
        return "Send Headers";
    }

    @Override
    public String getDescription() {
        return "Send the response headers from the servlet container to the web server";
    }
}
