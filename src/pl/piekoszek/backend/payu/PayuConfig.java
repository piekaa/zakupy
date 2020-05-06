package pl.piekoszek.backend.payu;

import pl.piekoszek.backend.http.server.EndpointsProvider;

public class PayuConfig {

    public static EndpointsProvider controller() {
        return new PayuController();
    }

}
