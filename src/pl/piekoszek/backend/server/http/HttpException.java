package pl.piekoszek.backend.server.http;

class HttpException extends RuntimeException {

    HttpException() {
    }

    HttpException(Throwable cause) {
        super(cause);
    }
}
