package com.piekoszek.app.server.http;

public interface MessageHandler {

    Response handler(Request request);
}
