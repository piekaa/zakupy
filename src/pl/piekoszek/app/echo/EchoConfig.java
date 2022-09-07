package pl.piekoszek.app.echo;

import pl.piekoszek.backend.http.client.HttpRequestSender;
import pl.piekoszek.backend.http.server.EndpointsProvider;

public class EchoConfig {

    public static EndpointsProvider controller() {
        HttpRequestSender requestSender = new HttpRequestSender("localhost", 6999);
        return new EchoController(requestSender);
    }

}
