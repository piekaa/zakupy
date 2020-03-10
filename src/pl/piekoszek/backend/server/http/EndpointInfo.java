package pl.piekoszek.backend.server.http;

public class EndpointInfo {

    private String method;
    private String path;
    private MessageHandler<?> messageHandler;
    private Class<?> requestBodyClass;

    public EndpointInfo(String method, String path, MessageHandler<?> messageHandler, Class<?> requestBodyClass) {
        this.method = method;
        this.path = path;
        this.messageHandler = messageHandler;
        this.requestBodyClass = requestBodyClass;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public MessageHandler<?> getMessageHandler() {
        return messageHandler;
    }

    public Class<?> getRequestBodyClass() {
        return requestBodyClass;
    }
}
