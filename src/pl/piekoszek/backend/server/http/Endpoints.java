package pl.piekoszek.backend.server.http;

import pl.piekoszek.backend.json.Piekson;
import pl.piekoszek.backend.server.Connection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

class Endpoints {

    private Map<String, EndpointInfo> endpoints = new HashMap<>();

    void register(EndpointInfo[] endpointInfos) {
        for (EndpointInfo endpointInfo : endpointInfos) {
            register(endpointInfo);
        }
    }

    void register(EndpointInfo endpointInfo) {
        endpoints.put(endpointInfo.getMethod() + endpointInfo.getPath(), endpointInfo);
    }

    boolean handleMessageIfRegistered(Request request, Connection connection) {
        String endpointKey = request.method + request.path;

        if (!endpoints.containsKey(endpointKey)) {
            return false;
        }

        EndpointInfo endpointInfo = endpoints.get(endpointKey);

        Object requestBody = Piekson.fromJson(request.bodyText(), endpointInfo.getRequestBodyClass());
        Object result;
        try {
            Method method = endpointInfo.getMessageHandler().getClass().getMethods()[0];
            method.setAccessible(true);
            result = method.invoke(endpointInfo.getMessageHandler(), new RequestInfo(request), requestBody);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }

        String responseBodyString = "";
        if (result != null) {
            responseBodyString = Piekson.toJson(result);
        }

        Response response = new Response(ResponseStatus.OK, responseBodyString);
        response.addHeader("Content-Type", "application/json");

        ResponseWriter.write(connection.outputStream, response);

        return true;
    }
}