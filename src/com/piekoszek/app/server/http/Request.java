package com.piekoszek.app.server.http;

class Request {

    String requestLine;
    String headers;
    String method;
    String path;

    Request(String requestLine, String headers) {
        this.requestLine = requestLine;
        this.headers = headers;
        String[] splittedRequestLine = requestLine.split(" ");
        method = splittedRequestLine[0];
        path = splittedRequestLine[1];
    }
}
