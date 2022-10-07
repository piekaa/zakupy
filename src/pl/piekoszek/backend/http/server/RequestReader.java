package pl.piekoszek.backend.http.server;

import pl.piekoszek.collections.ByteBuffer;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class RequestReader {

    static Request read(InputStream inputStream) {
        ByteBuffer byteBuffer = new ByteBuffer();
        String requestLine;
        String headersText;
        try {
            int value;
            for (; ; ) {
                value = inputStream.read();
                if (value == -1) {
                    return null;
                }
                byteBuffer.add((byte) value);
                if (byteBuffer.endsWith1310()) {
                    break;
                }
            }
            requestLine = new String(byteBuffer.getAllBytes(), StandardCharsets.UTF_8);
            byteBuffer = new ByteBuffer();
            for (; ; ) {
                value = inputStream.read();
                if (value == -1) {
                    return null;
                }
                byteBuffer.add((byte) value);
                if (byteBuffer.endsWith13101310()) {
                    break;
                }
            }
            headersText = new String(byteBuffer.getAllBytes(), StandardCharsets.UTF_8);
            Map<String, String> headers = new HashMap<>();
            String[] splitedHeaders = headersText.split(new String(new byte[]{13, 10}));
            for (String header : splitedHeaders) {
                if (!header.contains(":")) {
                    throw new RequestParseException();
                }
                String[] nameAndValue = header.split(":");
                headers.put(nameAndValue[0].trim(), nameAndValue[1].trim());
            }
            if (!headers.containsKey("Content-Length")) {
                return new Request(requestLine, headers);
            }
            int contentLength = Integer.parseInt(headers.get("Content-Length"));
            byteBuffer = new ByteBuffer();
            for (int i = 0; i < contentLength; i++) {
                value = inputStream.read();
                if (value == -1) {
                    return new Request(requestLine, headers);
                }
                byteBuffer.add((byte) value);
            }
            return new Request(requestLine, headers, byteBuffer.getAllBytes());
        } catch (Exception e) {
            System.out.println("Exception while reading request: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
