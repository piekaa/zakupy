package pl.piekoszek.backend.calculation;

import pl.piekoszek.backend.http.server.EndpointsProvider;

public class CalculatorConfig {

    public static EndpointsProvider controller() {
        return new CalculatorController(new Calculator());
    }

}
