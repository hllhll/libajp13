/*
 * ForwardRequestMessage.java
 *
 * Copyright (c) 2015 Luca Carettoni
 * Copyright (c) 2010 Espen Wiborg
 *
 * Licensed under the Apache License, Version 2.0
 */
package org.nibblesec.ajp;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

class ForwardRequestMessage
    extends AbstractAjpMessage
{
    //Mandatory fields
    private int method;
    private String protocol;
    private String requestUri;
    private String remoteAddr;
    private String remoteHost;
    private String serverName;
    private int serverPort;
    private boolean isSsl;
    private final List<Pair<String, String>> headers = new LinkedList<>();
    //Optional fields
    private String context;
    private String servletPath;
    private String remoteUser;
    private String authType;
    private String queryString;
    private String jvmRoute;
    private String sslCert;
    private String sslCipher;
    private String sslSession;
    private final List<Pair<String, String>> attributes = new LinkedList<>();
    
    
    ForwardRequestMessage() {
        
    }

    ForwardRequestMessage(int method, String protocol, String requestUri, String remoteAddr, String remoteHost, String serverName, int serverPort, boolean isSsl, List<Pair<String, String>> headers) {
        super(Constants.PACKET_TYPE_FORWARD_REQUEST);
        this.method = method;
        writeInt(statusCode);
    }
    
    ForwardRequestMessage(URL url, AjpMethod method, int contentLength) {
        this();
        setMethod(method);
        setServerName(url.getHost());

        addHeader("Content-Length", String.valueOf(contentLength));
        if (contentLength > 0) {
            addHeader("Content-Type", "application/x-www-form-urlencoded");
        }

        if (url.getPort() == -1) {
            setServerPort(url.getDefaultPort());
        } else {
            setServerPort(url.getPort());
        }
        
        setRequestUri(url.getPath());
        
        if (url.getQuery() != null) {
            addAttribute(Constants.ATTRIBUTE_QUERY_STRING, url.getQuery());
        }
    }

    final void setMethod(AjpMethod method) {
        this.method = method;
    }

    final void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    final void setServerName(String serverName) {
        this.serverName = serverName;
        addHeader("Host", serverName);
    }

    final void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    final void addHeader(String name, String value) {
        headers.add(Pair.make(name, value));
    }

    final void addAttribute(String name, String value) {
        attributes.add(Pair.make(name, value));
    }

    private void writeHeaders(List<Pair<String, String>> headers) {
        for (Pair<String, String> header : headers) {
            String name = header.a;
            String value = header.b;

            if (Constants.COMMON_HEADERS.containsKey(name.toLowerCase())) {
                writeInt(Constants.COMMON_HEADERS.get(name.toLowerCase()));
            } else {
                writeString(name);
            }
            writeString(value);
        }
    }

    private void writeAttributes(List<Pair<String, String>> attributes) {
        for (Pair<String, String> attribute : attributes) {
            String name = attribute.a;
            String value = attribute.b;

            if (Constants.COMMON_ATTRIBUTES.containsKey(name)) {
                writeInt(Constants.COMMON_ATTRIBUTES.get(name));
                writeString(value);
            } else {
                writeInt(Constants.ATTRIBUTE_GENERIC);
                writeString(name);
                writeString(value);
            }
        }
    }
    
        static ForwardRequestMessage readFrom(InputStream in) throws IOException {
        int length = AjpReader.readInt(in);
        byte[] bytes = new byte[length];
        AjpReader.fullyRead(bytes, in);
        return new ForwardRequestMessage(length, bytes);
    }
    
    @Override
    public String toString() {
        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            System.out.println("[KO] SendBodyChunkMessage UnsupportedEncodingException: " + ex.getLocalizedMessage());
            return "InvalidEncoding";
        }
    }

    @Override
    public String getName() {
        return "Forward Request (AJP's most common packet)";
    }

    @Override
    public String getDescription() {
        return "Begin the request-processing cycle with the following data. Request: " + this.toString();
    }

}
