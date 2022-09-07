package pl.piekoszek.backend.http.server;

import pl.piekoszek.backend.tcp.Connection;
import pl.piekoszek.json.Piekson;
import pl.piekoszek.json.PieksonException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

class Endpoints {

    private Map<String, EndpointInfo> endpoints = new HashMap<>();
    private Map<Pattern, EndpointInfo> endpointsWithPathParams = new HashMap<>();

    void register(EndpointInfo[] endpointInfos) {
        for (EndpointInfo endpointInfo : endpointInfos) {
            register(endpointInfo);
        }
    }

    void register(EndpointInfo endpointInfo) {
        if (endpointInfo.pathPattern != null) {
            endpointsWithPathParams.put(endpointInfo.pathPattern, endpointInfo);
        } else {
            endpoints.put(endpointInfo.getMethod() + endpointInfo.getPath(), endpointInfo);
        }
    }

    boolean handleMessageIfRegistered(Request request, Connection connection) {
        String path;
        if (request.path.contains("?")) {
            path = request.path.substring(0, request.path.indexOf('?'));
        } else {
            path = request.path;
        }
        String endpointKey = request.method + path;

        EndpointInfo endpointInfo = null;
        RequestInfo requestInfo = new RequestInfo(request);

        if (endpoints.containsKey(endpointKey)) {
            endpointInfo = endpoints.get(endpointKey);
        } else {
            endpointKey += endpointKey.endsWith("/") ? "" : "/";
            for (Map.Entry<Pattern, EndpointInfo> entry : endpointsWithPathParams.entrySet()) {
                if (entry.getKey().matcher(endpointKey).matches()) {
                    endpointInfo = entry.getValue();
                    String[] splittedPath = request.path.split("/");
                    for (int i = 0; i < endpointInfo.positions.size(); i++) {
                        requestInfo.pathParams.put(endpointInfo.pathParamNames.get(i), splittedPath[endpointInfo.positions.get(i)]);
                    }
                }
            }
        }

        if (endpointInfo == null) {
            return false;
        }

        Object requestBody = new Object();
        if (request.body.length > 0) {
            try {
                if (endpointInfo.getRequestBodyClass() == Map.class) {
                    requestBody = Piekson.fromJson(request.bodyText());
                } else {
                    requestBody = Piekson.fromJson(request.bodyText(), endpointInfo.getRequestBodyClass());
                }
            } catch (Exception exception) {
                throw new PieksonException(exception);
            }
        }
        Object result;
        try {
            Method method = endpointInfo.getMessageHandler().getClass().getMethods()[0];
            method.setAccessible(true);
            result = method.invoke(endpointInfo.getMessageHandler(), requestInfo, requestBody);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }

        ResponseStatus responseStatus = ResponseStatus.OK;

        String responseBodyString = "";
        ResponseInfo responseInfo = null;
        if (result != null) {
            Object body = result;
            if (result instanceof ResponseInfo) {
                responseInfo = (ResponseInfo) result;
                responseStatus = responseInfo.status;
                body = responseInfo.body;
            }
            responseBodyString = Piekson.toJson(body);
        }

        Response response = new Response(responseStatus, responseBodyString);
        if (responseInfo != null) {
            for (Map.Entry<String, String> entry : responseInfo.headers.entrySet()) {
                response.addHeader(entry.getKey(), entry.getValue());
            }
        }
        response.addHeader("Content-Type", "application/json");

        ResponseWriter.write(connection.outputStream, response);

        return true;
    }
}