package pl.piekoszek.backend.http.client;

public class ResponseParseException extends RuntimeException {

    public ResponseParseException() {
    }

    public ResponseParseException(Throwable cause) {
        super(cause);
    }

    public ResponseParseException(String message) {
        super(message);
    }
}
