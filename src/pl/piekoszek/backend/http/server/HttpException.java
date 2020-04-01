package pl.piekoszek.backend.http.server;

class HttpException extends RuntimeException {

    HttpException() {
    }

    HttpException(Throwable cause) {
        super(cause);
    }
}
