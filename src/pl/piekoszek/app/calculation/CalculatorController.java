package pl.piekoszek.app.calculation;

import pl.piekoszek.backend.http.server.*;

import java.util.HashMap;
import java.util.Map;

class CalculatorController implements EndpointsProvider {

    private Calculator calculator;

    CalculatorController(Calculator calculator) {
        this.calculator = calculator;
    }

    private MessageHandler<AddRequest> add = (info, body) ->
            new ResponseInfo(new AddResponse(calculator.add(body.a, body.b)),
                    customHeader(),
                    ResponseStatus.OK);

    private Map<String, String> customHeader() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Custom", "Header");
        return headers;
    }


    @Override
    public EndpointInfo[] endpoints() {
        return new EndpointInfo[]{
                new EndpointInfo("POST", "/calc/add", add, AddRequest.class)
        };
    }
}
