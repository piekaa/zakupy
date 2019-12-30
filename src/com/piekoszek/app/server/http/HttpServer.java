package com.piekoszek.app.server.http;

import com.piekoszek.app.collections.WholeFileReader;
import com.piekoszek.app.server.Connection;
import com.piekoszek.app.server.ConnectionHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
                break;
            }
            try {
                if (staticPath != null) {
                    String filePath = request.path.replaceAll("/", Matcher.quoteReplacement(File.separator));
                    File file = new File(staticPath + request.path + (request.path.endsWith("/") ? "index.html" : ""));
                    try {
                        WholeFileReader reader = new WholeFileReader(file);
                        if(request.path.endsWith(".png")) {
                            ResponseWriter.write(connection.outputStream, new ImageResponse(OK, reader.read()));
                            continue;
                        } else {
                            ResponseWriter.write(connection.outputStream, new Response(OK, reader.read()));
                            continue;
                        }
                    } catch (FileNotFoundException e) {
                        ResponseWriter.write(connection.outputStream, new Response(NOT_FOUND, filePath + " not found in server ;("));
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
