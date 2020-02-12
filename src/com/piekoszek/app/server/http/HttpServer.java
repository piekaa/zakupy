package com.piekoszek.app.server.http;

import com.piekoszek.app.collections.WholeFileReader;
import com.piekoszek.app.server.Connection;
import com.piekoszek.app.server.ConnectionHandler;

import java.io.File;
import java.util.regex.Matcher;

import static com.piekoszek.app.server.http.ResponseStatus.NOT_FOUND;
import static com.piekoszek.app.server.http.ResponseStatus.OK;

public class HttpServer implements ConnectionHandler {

    private String staticPath;

    public HttpServer() {
    }

    public HttpServer(String staticPath) {
        this.staticPath = staticPath;
    }

    @Override
    public void handle(Connection connection) {
        for (; ; ) {
            Request request = RequestReader.read(connection.inputStream);
            if (request == null) {
                break;
            }
            if (!request.method.equals("GET")) {
                ResponseWriter.write(connection.outputStream, new Response(NOT_FOUND, "Not found"));
                continue;
            }
            if (staticPath == null) {
                ResponseWriter.write(connection.outputStream, new Response(OK, "Hello world 2!"));
                continue;
            }
            String filePath = request.path.replaceAll("/", Matcher.quoteReplacement(File.separator));
            File file = new File(staticPath + filePath + (request.path.endsWith("/") ? "index.html" : ""));
            if (file.exists() && !file.isDirectory()) {
                sendResponse(file, connection);
                continue;
            } else {
                file = new File(staticPath + filePath + "/index.html");
                if (file.exists() && !file.isDirectory()) {
                    sendResponse(file, connection);
                    continue;
                }
            }
            ResponseWriter.write(connection.outputStream, new Response(NOT_FOUND, request.path + " not found in server ;("));
        }
    }

    private void sendResponse(File file, Connection connection) {
        WholeFileReader reader = new WholeFileReader(file);
        Response response = new Response(OK, reader.read());
        if (file.getPath().endsWith(".svg")) {
            response.headers.put("Content-Type", "image/svg+xml");
        }
        ResponseWriter.write(connection.outputStream, response);
    }
}
