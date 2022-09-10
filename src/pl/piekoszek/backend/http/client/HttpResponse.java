package pl.piekoszek.backend.http.client;

import pl.piekoszek.json.Piekson;
import pl.piekoszek.json.PieksonException;

import java.util.HashMap;
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
        String[] splittedResponseLine = statusLine.split(" ", 3);
        statusCode = Integer.parseInt(splittedResponseLine[1]);
        statusText = splittedResponseLine[2];
        this.headers = headers;
        successfulStatus = statusCode >= 200 && statusCode < 300;
    }

    HttpResponse(String statusLine, Map<String, String> headers, byte[] body, Class<T> type) {
        this(statusLine, headers);
        try {
            this.body = Piekson.fromJson(new String(body), type);
        } catch (PieksonException exception) {
            Map<String, Object> errorBodyMap = new HashMap<>();
            errorBodyMap.put("_error", "Response body is not JSON");
            errorBodyMap.put("_error_message", exception.getMessage());
            errorBodyMap.put("_raw_body_text", new String(body));
            errorBodyMap.put("_raw_body_bytes", body);
            this.bodyMap = errorBodyMap;
        }
    }

    HttpResponse(String statusLine, Map<String, String> headers, byte[] body) {
        this(statusLine, headers);
        try {
            this.bodyMap = Piekson.fromJson(new String(body));
        } catch (PieksonException exception) {
            Map<String, Object> errorBodyMap = new HashMap<>();
            errorBodyMap.put("_error", "Response body is not JSON");
            errorBodyMap.put("_error_message", exception.getMessage());
            errorBodyMap.put("_raw_body_text", new String(body));
            errorBodyMap.put("_raw_body_bytes", body);
            this.bodyMap = errorBodyMap;
        }
    }

    HttpResponse() {
    }
}
