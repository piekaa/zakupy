Hello World endpoint

````java
import pl.piekoszek.backend.http.server.EndpointInfo;
import pl.piekoszek.backend.http.server.HttpServer;
import pl.piekoszek.backend.tcp.server.TcpServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = new HttpServer();
        httpServer.register(new EndpointInfo("GET", "/hello", (info, body) -> "Hello World!" ));
        new TcpServer(httpServer, 8080);
    }
}
````

To serve files from `static` directory.

````java
import pl.piekoszek.backend.http.server.EndpointInfo;
import pl.piekoszek.backend.http.server.HttpServer;
import pl.piekoszek.backend.tcp.server.TcpServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = new HttpServer("static");
        new TcpServer(httpServer, 8080);
    }
}
````

For more see `src/pl/piekoszek/backend/Main.java`