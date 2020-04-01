package pl.piekoszek.backend.http.client;

import pl.piekoszek.json.Piekson;

import java.util.Map;

public class HttpResponse<T> {

    public T body;
    public Map<String, Object> bodyMap;
    public Map<String, String> headers;
    public int statusCode;
    public String statusText;
    public boolean failed;
    public boolean successfulStatus = false;
    public HttpRequestException exception;

    HttpResponse(String statusLine, Map<String, String> headers) {
        String[] splittedResponseLine = statusLine.split(" ");
        statusCode = Integer.parseInt(splittedResponseLine[1]);
        statusText = splittedResponseLine[2];
        this.headers = headers;
        successfulStatus = statusCode >= 200 && statusCode < 300;
    }

    HttpResponse(String statusLine, Map<String, String> headers, byte[] body, Class<T> type) {
        this(statusLine, headers);
        this.body = Piekson.fromJson(new String(body), type);
    }

    HttpResponse(String statusLine, Map<String, String> headers, byte[] body) {
        this(statusLine, headers);
        this.bodyMap = Piekson.fromJson(new String(body));
    }

    HttpResponse() {
    }
}
