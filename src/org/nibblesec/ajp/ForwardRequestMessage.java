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
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/*
 * This class begins the request-processing cycle from the server to the container
 */
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
    private List<Pair<String, String>> headers = new LinkedList<>();
    //Optional fields
    private List<Pair<String, String>> attributes = new LinkedList<>();

    ForwardRequestMessage(int method, URL url, String remoteAddr,
            String remoteHost, List<Pair<String, String>> headers,
            List<Pair<String, String>> attributes)
    {
        this(method, url.getProtocol(), url.getPath(), remoteAddr, remoteHost,
                url.getHost(), ((url.getPort() == -1) ? url.getDefaultPort() : url.getPort()),
                url.getProtocol().equalsIgnoreCase("https"), headers, attributes);

        if (url.getQuery() != null) {
            addAttribute(Constants.ATTRIBUTE_QUERY_STRING, url.getQuery());
        }
    }

    ForwardRequestMessage(int method, String protocol, String requestUri,
            String remoteAddr, String remoteHost, String serverName,
            int serverPort, boolean isSsl, List<Pair<String, String>> headers,
            List<Pair<String, String>> attributes)
    {

        super(Constants.PACKET_TYPE_FORWARD_REQUEST);

        this.method = method;
        writeByte(method);
        this.protocol = protocol;
        writeString(protocol, true);
        this.requestUri = requestUri;
        writeString(requestUri, true);
        this.remoteAddr = remoteAddr; //e.g. 127.0.0.1
        writeString(remoteAddr, true);
        this.remoteHost = remoteHost;
        writeString(remoteHost, true); //e.g. localhost
        this.serverName = serverName;
        writeString(serverName, true);
        addHeader("Host", serverName);
        this.serverPort = serverPort;
        writeInt(serverPort);
        this.isSsl = isSsl;
        writeBoolean(isSsl);

        //headers
        this.headers = headers;
        writeInt(headers.size());
        for (Pair<String, String> header : headers) {
            String name = header.a;
            String value = header.b;

            if (Constants.COMMON_HEADERS.containsKey(name.toLowerCase())) {
                //Send HeaderName as Byte
                writeByte(Constants.HEADERS_GENERIC);
                writeByte(Constants.COMMON_HEADERS.get(name.toLowerCase()));
            } else {
                //Send HeaderName as String
                writeString(name, true);
            }
            //Send HeaderValue
            writeString(value, true);
        }

        //attributes (optionals)
        this.attributes = attributes;
        for (Pair<String, String> attribute : attributes) {
            String name = attribute.a;
            String value = attribute.b;

            if (Constants.COMMON_ATTRIBUTES.containsKey(name.toLowerCase())) {
                //Known attribute type
                writeByte(Constants.COMMON_ATTRIBUTES.get(name.toLowerCase()));
            } else {
                //Extra attribute type
                writeByte(Constants.COMMON_ATTRIBUTES.get(Constants.ATTRIBUTE_REQATTR_STRING));
                //Send attribute name
                writeString(name, true);
            }
            //Send attribute value
            writeString(value, true);
        }

        //End of the packet
        writeByte(Constants.REQUEST_TERMINATOR);
    }

    static public ForwardRequestMessage ForwardRequestMessageGetBuilder(URL url)
    {
        return new ForwardRequestMessage(2, url, "127.0.0.1", "localhost", null, null);
    }

    static public ForwardRequestMessage ForwardRequestMessagePostBuilder(URL url, int contentLength)
    {
        List<Pair<String, String>> headers = new LinkedList<>();
        headers.add(Pair.make("Content-Length", String.valueOf(contentLength)));
        if (contentLength > 0) {
            headers.add(Pair.make("Content-Type", "application/x-www-form-urlencoded"));
        }
        return new ForwardRequestMessage(4, url, "127.0.0.1", "localhost", headers, null);
    }

    public int getMethod()
    {
        return method;
    }

    public void setMethod(int method)
    {
        this.method = method;
    }

    public String getProtocol()
    {
        return protocol;
    }

    public void setProtocol(String protocol)
    {
        this.protocol = protocol;
    }

    public String getRequestUri()
    {
        return requestUri;
    }

    public void setRequestUri(String requestUri)
    {
        this.requestUri = requestUri;
    }

    public String getRemoteAddr()
    {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr)
    {
        this.remoteAddr = remoteAddr;
    }

    public String getRemoteHost()
    {
        return remoteHost;
    }

    public void setRemoteHost(String remoteHost)
    {
        this.remoteHost = remoteHost;
    }

    public String getServerName()
    {
        return serverName;
    }

    public void setServerName(String serverName)
    {
        this.serverName = serverName;
    }

    public int getServerPort()
    {
        return serverPort;
    }

    public void setServerPort(int serverPort)
    {
        this.serverPort = serverPort;
    }

    public boolean isIsSsl()
    {
        return isSsl;
    }

    public void setIsSsl(boolean isSsl)
    {
        this.isSsl = isSsl;
    }

    public List<Pair<String, String>> getHeaders()
    {
        return headers;
    }

    public void setHeaders(List<Pair<String, String>> headers)
    {
        this.headers = headers;
    }

    public List<Pair<String, String>> getAttributes()
    {
        return attributes;
    }

    public void setAttributes(List<Pair<String, String>> attributes)
    {
        this.attributes = attributes;
    }

    public final void addHeader(String name, String value)
    {
        headers.add(Pair.make(name, value));
    }

    public final void addAttribute(String name, String value)
    {
        attributes.add(Pair.make(name, value));
    }

    public int numHeaders()
    {
        return headers.size();
    }

    public int numAttributes()
    {
        return attributes.size();
    }

    static ForwardRequestMessage readFrom(InputStream in) throws IOException
    {
        int method = AjpReader.readByte(in);
        String protocol = AjpReader.readString(in);
        String requestUri = AjpReader.readString(in);
        String remoteAddr = AjpReader.readString(in);
        String remoteHost = AjpReader.readString(in);
        String serverName = AjpReader.readString(in);
        int serverPort = AjpReader.readInt(in);
        boolean isSsl = AjpReader.readBoolean(in);
        int numHeaders = AjpReader.readInt(in);

        List<Pair<String, String>> headers = new LinkedList<>();
        for (int i = 0; i < numHeaders; i++) {
            int b1 = AjpReader.readByte(in);
            int b2 = AjpReader.readByte(in);

            String name = "";
            if (b1 == Constants.HEADERS_GENERIC && Constants.COMMON_HEADERS.containsValue(b2)) {
                for (Map.Entry<String, Integer> entry : Constants.COMMON_HEADERS.entrySet()) {
                    String key = entry.getKey();
                    Integer value = entry.getValue();
                    if (value == b2) {
                        name = key;
                    }
                }
            } else {
                name = AjpReader.readString(AjpReader.makeInt(b1, b2), in);
            }
            headers.add(Pair.make(name, AjpReader.readString(in)));
        }

        //read 'till the end
        List<Pair<String, String>> attributes = new LinkedList<>();
        while (in.available() > 0) {
            int next = AjpReader.readByte(in);
            if (next == Constants.REQUEST_TERMINATOR) {
                break;
            } else if (Constants.COMMON_ATTRIBUTES.containsValue(next)) {
                String name = "";
                for (Map.Entry<String, Integer> entry : Constants.COMMON_ATTRIBUTES.entrySet()) {
                    String key = entry.getKey();
                    Integer value = entry.getValue();
                    if (value == next) {
                        name = key;
                        //Exception for req_attribute
                        if (name.equalsIgnoreCase(Constants.ATTRIBUTE_REQATTR_STRING)) {
                            name = AjpReader.readString(in);
                        }
                    }
                }
                attributes.add(Pair.make(name, AjpReader.readString(in)));
            } else {
                System.out.println("[!] ForwardRequestMessage Unexpected Attribute: " + next);
            }
        }

        return new ForwardRequestMessage(method, protocol, requestUri, remoteAddr,
                remoteHost, serverName, serverPort, isSsl, headers, attributes);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Method: ").append(method).append("\n");
        sb.append("Protocol: ").append(protocol).append("\n");
        sb.append("RequestUri: ").append(requestUri).append("\n");
        sb.append("RemoteAddr: ").append(remoteAddr).append("\n");
        sb.append("RemoteHost: ").append(remoteHost).append("\n");
        sb.append("ServerName: ").append(serverName).append("\n");
        sb.append("ServerPort: ").append(serverPort).append("\n");
        sb.append("isSsl: ").append(isSsl).append("\n");
        for (Pair<String, String> header : headers) {
            String name = header.a;
            String value = header.b;
            sb.append("Header: ").append(name).append(" ").append(value).append("\n");
        }

        for (Pair<String, String> attribute : attributes) {
            String name = attribute.a;
            String value = attribute.b;
            sb.append("Attribute: ").append(name).append(" ").append(value).append("\n");
        }

        return sb.toString();
    }

    @Override
    public String getName()
    {
        return "Forward Request (begin the request-processing cycle)";
    }

    @Override
    public String getDescription()
    {
        return "Begin the request-processing cycle with the following data.\nRequest:\n" + this.toString();
    }
}
