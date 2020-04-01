package pl.piekoszek.backend.http.client;

import pl.piekoszek.backend.tcp.Connection;
import pl.piekoszek.backend.tcp.client.TcpClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestSender {

    private String host;
    private int port;
    private Connection connection;

    public HttpRequestSender(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            connection = new TcpClient(host, port).connection();
        } catch (IOException e) {
            throw new HttpRequestException(e);
        }
    }

    public <T> HttpResponse<T> get(String path, Map<String, String> headers, Class<T> responseType) {
        byte[] requestBytes = RequestWriter.requestBytes("GET", path, headers, host, port);
        return sendRequestReadResponse(requestBytes, responseType);
    }

    public <T> HttpResponse<T> post(String path, Map<String, String> headers, Object requestBody, Class<T> responseType) {
        byte[] requestBytes = RequestWriter.requestBytes("POST", path, headers, requestBody, host, port);
        return sendRequestReadResponse(requestBytes, responseType);
    }

    public <T> HttpResponse<T> post(String path, Object requestBody, Class<T> responseType) {
        byte[] requestBytes = RequestWriter.requestBytes("POST", path, new HashMap<>(), requestBody, host, port);
        return sendRequestReadResponse(requestBytes, responseType);
    }

    public HttpResponse<Map<String,Object>> post(String path, Map<String, String> headers, Object requestBody) {
        byte[] requestBytes = RequestWriter.requestBytes("POST", path, headers, requestBody, host, port);
        return sendRequestReadResponse(requestBytes);
    }

    public HttpResponse<Map<String,Object>> post(String path, Object requestBody) {
        byte[] requestBytes = RequestWriter.requestBytes("POST", path, new HashMap<>(), requestBody, host, port);
        return sendRequestReadResponse(requestBytes);
    }

    private <T> HttpResponse<T> sendRequestReadResponse(byte[] requestBytes, Class<T> responseType) {
        try {
            connection.outputStream.write(requestBytes);
            HttpResponse<T> response = ResponseReader.read(connection.inputStream, responseType);
            response.failed = false;
            return response;
        } catch (IOException e) {
            try {
                connection = new TcpClient(host, port).connection();
                connection.outputStream.write(requestBytes);
                HttpResponse<T> response = ResponseReader.read(connection.inputStream, responseType);
                response.failed = false;
                return response;

            } catch (Exception e1) {
                HttpResponse<T> httpResponse = new HttpResponse<>();
                httpResponse.failed = true;
                httpResponse.exception = new HttpRequestException(e1);
                return httpResponse;
            }
        }
    }

    private <T> HttpResponse<T> sendRequestReadResponse(byte[] requestBytes) {
        try {
            connection.outputStream.write(requestBytes);
            HttpResponse<T> response = ResponseReader.read(connection.inputStream);
            response.failed = false;
            return response;
        } catch (IOException e) {
            try {
                connection = new TcpClient(host, port).connection();
                connection.outputStream.write(requestBytes);
                HttpResponse<T> response = ResponseReader.read(connection.inputStream);
                response.failed = false;
                return response;

            } catch (Exception e1) {
                HttpResponse<T> httpResponse = new HttpResponse<>();
                httpResponse.failed = true;
                httpResponse.exception = new HttpRequestException(e1);
                return httpResponse;
            }
        }
    }

    public void close() {
        connection.close();
    }
}
