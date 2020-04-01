package pl.piekoszek.backend.http.client;

public class HttpRequestException extends RuntimeException {

    public HttpRequestException(Throwable cause) {
        super(cause);
    }
}
