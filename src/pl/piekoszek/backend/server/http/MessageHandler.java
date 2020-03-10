package pl.piekoszek.backend.server.http;

public interface MessageHandler<T> {

    Object handle(RequestInfo requestInfo, T param);
}