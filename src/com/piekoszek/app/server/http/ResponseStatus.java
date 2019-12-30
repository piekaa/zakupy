package com.piekoszek.app.server.http;

enum ResponseStatus {

    OK(200, "ok"),
    NOT_FOUND(404, "not found");

    public final int code;
    public final String message;

    ResponseStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public final String toRequestLine() {
        return "HTTP/1.1 " + code + " " + message;
    }
}
