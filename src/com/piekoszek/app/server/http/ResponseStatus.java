package com.piekoszek.app.server.http;

public enum ResponseStatus {

    OK(200, "dla mnie się podoba"),
    NOT_FOUND(404, "takiego czegoś na pewno nigdy nie było");

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
