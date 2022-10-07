package pl.piekoszek.backend.http.server;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Response {

    ResponseStatus responseStatus;
    byte[] responseBytes;
    Map<String, String> headers = new HashMap<>();

    public Response(ResponseStatus responseStatus, String responseBody) {
        this.responseStatus = responseStatus;
        responseBytes = responseBody.getBytes(StandardCharsets.UTF_8);
    }

    public Response(ResponseStatus responseStatus, byte[] responseBody) {
        this.responseStatus = responseStatus;
        this.responseBytes = responseBody;
    }

    void addHeader(String name, String value) {
        headers.put(name, value);
    }
}
