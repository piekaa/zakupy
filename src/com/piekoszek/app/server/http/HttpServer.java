package com.piekoszek.app.server.http;

import com.piekoszek.app.collections.WholeFileReader;
import com.piekoszek.app.server.Connection;
import com.piekoszek.app.server.ConnectionHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
            try {
                if (!request.method.equals("GET")) {
                    ResponseWriter.write(connection.outputStream, new Response(NOT_FOUND, "Not found"));
                    continue;
                }
                if (staticPath != null) {
                    String filePath = request.path.replaceAll("/", Matcher.quoteReplacement(File.separator));
                    File file = new File(staticPath + filePath + (request.path.endsWith("/") ? "index.html" : ""));
                    try {
                        WholeFileReader reader = new WholeFileReader(file);
                        ResponseWriter.write(connection.outputStream, new Response(OK, reader.read()));
                        continue;
                    } catch (FileNotFoundException e) {
                        ResponseWriter.write(connection.outputStream, new Response(NOT_FOUND, request.path + " not found in server ;("));
                        continue;
                    }
                }
                ResponseWriter.write(connection.outputStream, new Response(OK, "Hello world 2!"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
