package pl.piekoszek.backend.calculation;

import pl.piekoszek.backend.server.http.EndpointsProvider;

public class CalculatorConfig {

    public static EndpointsProvider controller() {
        return new CalculatorController(new Calculator());
    }

}
