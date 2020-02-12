package com.piekoszek.app.server.http;

class HttpException extends RuntimeException {

    HttpException() {
    }

    HttpException(Throwable cause) {
        super(cause);
    }
}
