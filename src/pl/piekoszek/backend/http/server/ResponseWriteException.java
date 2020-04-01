package pl.piekoszek.backend.http.server;

public class ResponseWriteException extends HttpException {

    public ResponseWriteException(Throwable cause) {
        super(cause);
    }
}
