package pl.piekoszek.backend.http.server;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EndpointInfo {

    private String method;
    private String path;

    private List<MessageHandler<?>> preMessageHandlers;
    private MessageHandler<?> messageHandler;
    private Class<?> requestBodyClass;
    List<String> pathParamNames = new ArrayList<>();
    List<Integer> positions = new ArrayList<>();
    Pattern pathPattern;

    public EndpointInfo(String method, String path, MessageHandler<?> messageHandler, MessageHandler<?> preMessageHandler, Class<?> requestBodyClass) {
        this(method, path, messageHandler, requestBodyClass);
        preMessageHandlers.add(preMessageHandler);
    }

    public EndpointInfo(String method, String path, MessageHandler<?> messageHandler, Class<?> requestBodyClass) {
        this(method, path, messageHandler, new ArrayList<>(), requestBodyClass);
    }

    public EndpointInfo(String method, String path, MessageHandler<?> messageHandler, List<MessageHandler<?>> preMessageHandlers, Class<?> requestBodyClass) {
        this.method = method;
        this.path = path;
        this.messageHandler = messageHandler;
        this.requestBodyClass = requestBodyClass;

        assert preMessageHandlers != null;
        this.preMessageHandlers = preMessageHandlers;

        if (path.contains(":")) {
            if (!path.endsWith("/")) {
                path += "/";
            }
            pathPattern = Pattern.compile(method + path.replaceAll(":.*?/", ".*?/"));
            Matcher matcher = Pattern.compile(":.*?/").matcher(path);
            int end = 0;
            int slashCount = 0;
            while (matcher.find()) {
                for (int i = end; i < matcher.start(); i++) {
                    if (path.charAt(i) == '/') {
                        slashCount++;
                    }
                }
                end = matcher.start();
                positions.add(slashCount);
                String param = matcher.group();
                pathParamNames.add(param.substring(1, param.length() - 1));
            }
        }
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

    public List<MessageHandler<?>> getPreMessageHandlers() {
        return preMessageHandlers;
    }

    public Class<?> getRequestBodyClass() {
        return requestBodyClass;
    }
}
