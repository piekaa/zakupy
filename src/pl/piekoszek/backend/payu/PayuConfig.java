package pl.piekoszek.backend.payu;

import pl.piekoszek.backend.http.client.HttpRequestSender;
import pl.piekoszek.backend.http.server.EndpointsProvider;

public class PayuConfig {

    public static EndpointsProvider controller() {
        return new PayuController();
    }

    public static PayuService payuService() {
        return new PayuServiceImpl(new HttpRequestSender("https://secure.snd.payu.com", 443));
    }

}
