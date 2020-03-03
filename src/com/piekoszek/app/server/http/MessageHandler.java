package com.piekoszek.app.server.http;

public interface MessageHandler {

    Response handle(Request request);
}
