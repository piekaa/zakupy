package com.piekoszek.app.server.http;

import java.util.HashMap;
import java.util.Map;

class Response {

    ResponseStatus responseStatus;
    byte[] responseBytes;
    Map<String, String> headers = new HashMap<>();

    Response(ResponseStatus responseStatus, String responseBody) {
        this.responseStatus = responseStatus;
        responseBytes = responseBody.getBytes();
    }

    Response(ResponseStatus responseStatus, byte[] responseBody) {
        this.responseStatus = responseStatus;
        this.responseBytes = responseBody;
    }
}
