# libajp13 - A complete AJPv1.3 Java library implementation
As far as I know, *libajp13* is the only client/server implementation of the Apache JServ Protocol version 1.3 (ajp13), based on the [Apache Protocol Reference](https://tomcat.apache.org/connectors-doc/ajp/ajpv13a.html).

This client library has been developed from Espen Wiborg's [ajp_client](https://github.com/espenhw/ajp-client) original code. Licensed under the Apache License, Version 2.0. 

This implementation is derived from Dan Milstein's reversing work, based on the Tomcat 3.x AJP implementation. If you've discovered a mistake or a bug in my implementation, please open an issue in Github.

### How To Use it
The following code examples show how to use *libajp13*:

_CPing and CPong_
```java
//Create a CPing message
AjpMessage msg = new CPingMessage();
//Send the content of the packet - msg.getBytes()
AjpMessage reply = AjpReader.parseMessage(gotBytes);
if (reply instanceof CPongMessage) {
  System.out.println("[OK] Valid CPong");
}
```
_Shutdown_
```java
AjpMessage msg = new ShutdownMessage();
```

_EndResponse with session reuse set to 'true'_
```java
AjpMessage msg = new EndResponseMessage(true);
```

_SendBodyChunkMessage_
```java
AjpMessage msg = new SendBodyChunkMessage(4,"ABCD".getBytes());
```

_SendHeadersMessage_
```java
List<Pair<String, String>> headers = new LinkedList<>();
headers.add(Pair.make("Content-Type","text/html; charset=utf-8"));
AjpMessage msg = new SendHeadersMessage(200,"OK",headers);
```

```java
//TODO SIMPLE EXAMPLE OF ForwardRequest
```

```java
//TODO COMPLICATED EXAMPLE OF ForwardRequest
```

### Useful links
* https://tomcat.apache.org/connectors-doc/ajp/ajpv13a.html
* https://tomcat.apache.org/tomcat-7.0-doc/api/org/apache/coyote/ajp/Constants.html
* https://github.com/kohsuke/ajp-client
* http://isu.ifmo.ru/docs/IAS904/web.904/q20202/protocol/AJPv21.html
* http://en.wikipedia.org/wiki/Apache_JServ_Protocol
* https://tomcat.apache.org/tomcat-7.0-doc/config/ajp.html

