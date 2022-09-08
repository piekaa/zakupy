package pl.piekoszek.app.calculation;

import pl.piekoszek.backend.http.handlers.BasicAuthMessageHandler;
import pl.piekoszek.backend.http.server.*;

import java.util.HashMap;
import java.util.Map;

class CalculatorController implements EndpointsProvider {

    private Calculator calculator;

    CalculatorController(Calculator calculator) {
        this.calculator = calculator;
    }

    private MessageHandler<AddRequest> add = (info, body) -> new AddResponse(calculator.add(body.a, body.b));
    private MessageHandler<AddRequest> multiply = (info, body) -> new AddResponse(calculator.multiply(body.a, body.b));

    @Override
    public EndpointInfo[] endpoints() {
        return new EndpointInfo[]{
                new EndpointInfo("POST", "/calc/add", add, AddRequest.class),
                new EndpointInfo("POST", "/calc/multiply", multiply,
                        new BasicAuthMessageHandler("Piekoszek", "Fotoszek"),
                        AddRequest.class)
        };
    }
}
