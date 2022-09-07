package pl.piekoszek.app.echo;

import pl.piekoszek.backend.http.client.HttpRequestSender;
import pl.piekoszek.backend.http.server.EndpointInfo;
import pl.piekoszek.backend.http.server.EndpointsProvider;
import pl.piekoszek.backend.http.server.MessageHandler;

import java.util.Map;

class EchoController implements EndpointsProvider {

    private HttpRequestSender requestSender;

    EchoController(HttpRequestSender requestSender) {
        this.requestSender = requestSender;
    }

    private MessageHandler<Map<String, Object>> echoUpper = (info, body) -> requestSender.post("/api/upper", body).bodyMap;

    @Override
    public EndpointInfo[] endpoints() {
        return new EndpointInfo[]{
                new EndpointInfo("POST", "/echo/upper", echoUpper, Map.class)
        };
    }
}
