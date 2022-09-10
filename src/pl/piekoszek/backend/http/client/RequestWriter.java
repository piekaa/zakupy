package pl.piekoszek.backend.http.client;

import pl.piekoszek.collections.ByteBuffer;
import pl.piekoszek.json.Piekson;

import java.util.Map;

class RequestWriter {

    private static final byte[] CLRF = new byte[]{13, 10};

    public static byte[] requestBytes(String method, String path, Map<String, String> headers, String host, int port) {
        return requestLineAndHeaders(method, path, headers, host, port).getAllBytes();
    }

    public static byte[] requestBytes(String method, String path, Map<String, String> headers, Object body, String host, int port) {
        byte[] bodyJsonBytes = Piekson.toJson(body).getBytes();
        headers.put("Content-Length", bodyJsonBytes.length + "");
        ByteBuffer byteBuffer = requestLineAndHeaders(method, path, headers, host, port);
        byteBuffer.add(bodyJsonBytes);
        return byteBuffer.getAllBytes();
    }

    public static byte[] requestBytesRawText(String method, String path, Map<String, String> headers, String rawText, String host, int port) {
        byte[] bodyBytes = rawText.getBytes();
        headers.put("Content-Length", bodyBytes.length + "");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        ByteBuffer byteBuffer = requestLineAndHeaders(method, path, headers, host, port);
        byteBuffer.add(bodyBytes);
        return byteBuffer.getAllBytes();
    }

    private static ByteBuffer requestLineAndHeaders(String method, String path, Map<String, String> headers, String host, int port) {
        ByteBuffer byteBuffer = new ByteBuffer();
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        byteBuffer.add(method + " " + path + " HTTP/1.1").add(CLRF);

        host = host.replace("https://", "");
        host = host.replace("http://", "");

        headers.put("Host", host + ":" + port);
        if (port == 80 || port == 443) {
            headers.put("Host", host);
        }

        if (!headers.containsKey("Content-Type")) {
            headers.put("Content-Type", "application/json");
        }
        headers.forEach((k, v) -> byteBuffer.add(k + ": " + v).add(CLRF));
        byteBuffer.add(CLRF);
        return byteBuffer;
    }

}
