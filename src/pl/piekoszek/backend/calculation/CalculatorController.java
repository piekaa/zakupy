package pl.piekoszek.backend.calculation;

import pl.piekoszek.backend.server.http.EndpointInfo;
import pl.piekoszek.backend.server.http.EndpointsProvider;
import pl.piekoszek.backend.server.http.MessageHandler;

class CalculatorController implements EndpointsProvider {

    private Calculator calculator;

    public CalculatorController(Calculator calculator) {
        this.calculator = calculator;
    }

    private MessageHandler<AddRequest> add = (info, body) -> new AddResponse(calculator.add(body.a, body.b));

    @Override
    public EndpointInfo[] endpoints() {
        return new EndpointInfo[]{
                new EndpointInfo("POST", "/calc/add", add, AddRequest.class)
        };
    }
}
