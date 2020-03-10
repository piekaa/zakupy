package pl.piekoszek.backend.server.http;

import java.util.Map;

public class Request {

    String requestLine;
    Map<String, String> headers;
    String method;
    String path;
    byte[] body;

    Request(String requestLine, Map<String, String> headers, byte[] body) {
        this(requestLine, headers);
        this.body = body;
    }

    Request(String requestLine, Map<String, String> headers) {
        this.requestLine = requestLine;
        this.headers = headers;
        String[] requestLineSplit = requestLine.split(" ");
        method = requestLineSplit[0];
        path = requestLineSplit[1];
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public byte[] getBody() {
        return body;
    }

    public String bodyText() {
        return new String(body);
    }
}
