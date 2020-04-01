package pl.piekoszek.backend.http.server;

public interface MessageHandler<T> {

    Object handle(RequestInfo requestInfo, T param);
}