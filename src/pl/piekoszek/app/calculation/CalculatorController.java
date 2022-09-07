package pl.piekoszek.app.calculation;

import pl.piekoszek.backend.http.server.EndpointInfo;
import pl.piekoszek.backend.http.server.EndpointsProvider;
import pl.piekoszek.backend.http.server.MessageHandler;

class CalculatorController implements EndpointsProvider {

    private Calculator calculator;

    CalculatorController(Calculator calculator) {
        this.calculator = calculator;
    }

    private MessageHandler<AddRequest> add = (info, body) -> new AddResponse(calculator.add(body.a, body.b));

    @Override
    public EndpointInfo[] endpoints() {
        return new EndpointInfo[]{
                new EndpointInfo("POST", "/calc/addLengthAndString", add, AddRequest.class)
        };
    }
}
