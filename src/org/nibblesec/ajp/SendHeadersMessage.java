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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/*
 * This class represents AJP's Send Headers message, from the container to the web server
 */
class SendHeadersMessage
        extends AbstractAjpMessage
{

    private int statusCode;
    private String statusMessage;
    private List<Pair<String, String>> headers;

    SendHeadersMessage(int statusCode, String statusMessage, List<Pair<String, String>> headers) throws IOException
    {
        super(Constants.PACKET_TYPE_SEND_HEADERS);
        this.statusCode = statusCode;
        writeInt(statusCode);
        this.statusMessage = statusMessage;
        writeString(statusMessage, true);
        this.headers = headers;
        int numHeaders = headers.size();
        writeInt(numHeaders);
        for (Pair<String, String> header : headers) {
            String name = header.a;
            String value = header.b;

            if (Constants.RESPONSE_HEADERS.containsKey(name.toLowerCase())) {
                //Send HeaderName as Byte
                writeByte(Constants.HEADERS_GENERIC);
                writeByte(Constants.RESPONSE_HEADERS.get(name.toLowerCase()));
            } else {
                //Send HeaderName as String
                writeString(name, true);
            }
            //Send HeaderValue
            writeString(value, true);
        }
    }

    //Getters and Setters
    public int getStatusCode()
    {
        return statusCode;
    }

    public void setStatusCode(int statusCode)
    {
        this.statusCode = statusCode;
    }

    public String getStatusMessage()
    {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage)
    {
        this.statusMessage = statusMessage;
    }

    public List<Pair<String, String>> getHeaders()
    {
        return headers;
    }

    public void setHeaders(List<Pair<String, String>> headers)
    {
        this.headers = headers;
    }

    @Override
    public String toString()
    {
        StringBuilder ret = new StringBuilder();
        ret.append(statusCode).append(" ").append(statusMessage).append("\n");
        ret.append("Headers:\n");
        for (Pair<String, String> header : headers) {
            ret.append(header.a).append(": ").append(header.b).append("\n");
        }
        return ret.toString();
    }

    static SendHeadersMessage readFrom(InputStream in) throws IOException
    {
        int statusCode = AjpReader.readInt(in);
        String statusMessage = AjpReader.readString(in);
        int numHeaders = AjpReader.readInt(in);
        List<Pair<String, String>> headers = new LinkedList<>();
        for (int i = 0; i < numHeaders; i++) {
            int b1 = AjpReader.readByte(in);
            int b2 = AjpReader.readByte(in);

            String name = "";
            if (b1 == Constants.HEADERS_GENERIC && Constants.RESPONSE_HEADERS.containsValue(b2)) {
                for (Map.Entry<String, Integer> entry : Constants.RESPONSE_HEADERS.entrySet()) {
                    String key = entry.getKey();
                    Integer value = entry.getValue();
                    if (value == b2) {
                        //According to RFC 7230, header field names are case-insensitive
                        name = key;
                    }
                }
            } else {
                name = AjpReader.readString(AjpReader.makeInt(b1, b2), in);
            }
            headers.add(Pair.make(name, AjpReader.readString(in)));
        }
        return new SendHeadersMessage(statusCode, statusMessage, headers);
    }

    @Override
    public String getName()
    {
        return "Send Headers";
    }

    @Override
    public String getDescription()
    {
        return "Send the response headers from the servlet container to the web server.\nContent:\n" + this.toString();
    }
}
