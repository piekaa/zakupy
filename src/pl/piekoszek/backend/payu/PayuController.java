package pl.piekoszek.backend.payu;

import pl.piekoszek.backend.http.server.EndpointInfo;
import pl.piekoszek.backend.http.server.EndpointsProvider;
import pl.piekoszek.backend.http.server.MessageHandler;

import java.util.Map;

class PayuController implements EndpointsProvider {

    private MessageHandler<Map<String, Object>> echoUpper = (info, body) -> {
        System.out.println(info.getRequest().bodyText());
        return "";
    };

    @Override
    public EndpointInfo[] endpoints() {
        return new EndpointInfo[]{
                new EndpointInfo("POST", "/statusCallback", echoUpper, Map.class)
        };
    }
}
