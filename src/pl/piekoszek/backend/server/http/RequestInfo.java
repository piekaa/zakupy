package pl.piekoszek.backend.server.http;

import java.util.HashMap;
import java.util.Map;

public class RequestInfo {

    Request request;
    Map<String, String> pathParams = new HashMap<>();

    public RequestInfo(Request request) {
        this.request = request;
    }

    public Request getRequest() {
        return request;
    }

    public Map<String, String> getPathParams() {
        return pathParams;
    }
}
