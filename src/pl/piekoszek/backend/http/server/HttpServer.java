package pl.piekoszek.backend.http.server;

import pl.piekoszek.backend.tcp.Connection;
import pl.piekoszek.backend.tcp.ConnectionHandler;
import pl.piekoszek.collections.WholeFileReader;
import pl.piekoszek.json.PieksonException;

import java.io.File;
import java.util.regex.Matcher;

public class HttpServer implements ConnectionHandler {

    private String staticPath;

    private Endpoints endpoints = new Endpoints();


    public HttpServer() {
    }

    public HttpServer(String staticPath) {
        this.staticPath = staticPath;
    }

    @Override
    public void handle(Connection connection) {
        try {
            for (; ; ) {
                Request request = RequestReader.read(connection.inputStream);
                if (request == null) {
                    break;
                }

                try {
                    if (endpoints.handleMessageIfRegistered(request, connection)) {
                        continue;
                    }
                } catch (PieksonException e) {
                    ResponseWriter.write(connection.outputStream, new Response(ResponseStatus.BAD_REQUEST, e.getMessage()));
                    continue;
                } catch (Exception e) {
                    e.printStackTrace();
                    ResponseWriter.write(connection.outputStream, new Response(ResponseStatus.INTERNAL_SERVER_ERROR, "Coś nie pykło..."));
                    continue;
                }
                if (!request.method.equals("GET")) {
                    ResponseWriter.write(connection.outputStream, new Response(ResponseStatus.NOT_FOUND, "Not found"));
                    continue;
                }
                if (staticPath == null) {
                    ResponseWriter.write(connection.outputStream, new Response(ResponseStatus.OK, "Hello world 2!"));
                    continue;
                }
                String filePath = request.path.replaceAll("/", Matcher.quoteReplacement(File.separator));

                if (filePath.contains("?")) {
                    filePath = filePath.substring(0, filePath.indexOf("?"));
                }

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
                ResponseWriter.write(connection.outputStream, new Response(ResponseStatus.NOT_FOUND, request.path + " not found in server ;("));
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void register(EndpointInfo[] endpointInfos) {
        endpoints.register(endpointInfos);
    }

    public void register(EndpointInfo endpointInfo) {
        endpoints.register(endpointInfo);
    }

    private void sendResponse(File file, Connection connection) {
        WholeFileReader reader = new WholeFileReader(file);
        Response response = new Response(ResponseStatus.OK, reader.read());
        if (file.getPath().endsWith(".svg")) {
            response.headers.put("Content-Type", "image/svg+xml");
        }
        ResponseWriter.write(connection.outputStream, response);
    }
}
