package com.piekoszek.app.server.http;

class Request {

    String requestLine;
    String headers;

    Request(String requestLine, String headers) {
        this.requestLine = requestLine;
        this.headers = headers;
    }
}
