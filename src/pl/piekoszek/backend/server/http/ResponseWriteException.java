package pl.piekoszek.backend.server.http;

public class ResponseWriteException extends HttpException {

    public ResponseWriteException(Throwable cause) {
        super(cause);
    }
}
