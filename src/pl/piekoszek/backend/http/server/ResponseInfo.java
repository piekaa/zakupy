package pl.piekoszek.backend.http.server;

import java.util.HashMap;
import java.util.Map;

public class ResponseInfo {

    final Object body;
    final ResponseStatus status;
    final Map<String, String> headers;

    public ResponseInfo(Object body, Map<String, String> headers, ResponseStatus status) {
        assert body != null;
        assert headers != null;
        assert status != null;

        this.body = body;
        this.headers = headers;
        this.status = status;
    }

    public ResponseInfo(Object body, ResponseStatus status) {
        this(body, new HashMap<>(), status);
    }

    public ResponseInfo(ResponseStatus status) {
        this(new Object(), new HashMap<>(), status);
    }
}
