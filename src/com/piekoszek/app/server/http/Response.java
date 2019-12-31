package com.piekoszek.app.server.http;

import java.util.HashMap;
import java.util.Map;

public class Response {

    ResponseStatus responseStatus;
    byte[] responseBytes;
    Map<String, String> headers = new HashMap<>();

    public Response(ResponseStatus responseStatus, String responseBody) {
        this.responseStatus = responseStatus;
        responseBytes = responseBody.getBytes();
    }

    public Response(ResponseStatus responseStatus, byte[] responseBody) {
        this.responseStatus = responseStatus;
        this.responseBytes = responseBody;
    }
}
