package com.piekoszek.app.server.http;

public class ResponseWriteException extends HttpException {

    public ResponseWriteException(Throwable cause) {
        super(cause);
    }
}
