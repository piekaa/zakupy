package pl.piekoszek.backend.json;

public class PieksonException extends RuntimeException {

    public PieksonException() {
    }

    public PieksonException(String message) {
        super(message);
    }

    public PieksonException(String message, Throwable cause) {
        super(message, cause);
    }

    public PieksonException(Throwable cause) {
        super(cause);
    }
}
