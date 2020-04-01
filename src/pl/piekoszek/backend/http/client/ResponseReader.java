package pl.piekoszek.backend.http.client;

import pl.piekoszek.collections.ByteBuffer;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ResponseReader {

    static <T> HttpResponse<T> read(InputStream inputStream, Class<T> responseType) {
        return readAndResolveType(inputStream, responseType);
    }

    static <T> HttpResponse<T> read(InputStream inputStream) {
        return readAndResolveType(inputStream, null);
    }

    private static <T> HttpResponse<T> readAndResolveType(InputStream inputStream, Class<T> responseType) {
        ByteBuffer byteBuffer = new ByteBuffer();
        String statusLine;
        String headersText;
        try {
            int value;
            for (; ; ) {
                value = inputStream.read();
                if (value == -1) {
                    throw new ResponseParseException("Unexpected end of response");
                }
                byteBuffer.add((byte) value);
                if (byteBuffer.endsWith1310()) {
                    break;
                }
            }
            statusLine = new String(byteBuffer.getAllBytes());
            byteBuffer = new ByteBuffer();
            for (; ; ) {
                value = inputStream.read();
                if (value == -1) {
                    throw new ResponseParseException("Unexpected end of response");
                }
                byteBuffer.add((byte) value);
                if (byteBuffer.endsWith13101310()) {
                    break;
                }
            }
            headersText = new String(byteBuffer.getAllBytes());
            Map<String, String> headers = new HashMap<>();
            String[] splitedHeaders = headersText.split(new String(new byte[]{13, 10}));
            for (String header : splitedHeaders) {
                if (!header.contains(":")) {
                    throw new ResponseParseException();
                }
                String[] nameAndValue = header.split(":");
                headers.put(nameAndValue[0].trim(), nameAndValue[1].trim());
            }
            if (!headers.containsKey("Content-Length")) {
                return new HttpResponse<>(statusLine, headers);
            }
            int contentLength = Integer.parseInt(headers.get("Content-Length"));
            byteBuffer = new ByteBuffer();
            for (int i = 0; i < contentLength; i++) {
                value = inputStream.read();
                if (value == -1) {
                    return new HttpResponse<>(statusLine, headers);
                }
                byteBuffer.add((byte) value);
            }
            int statusCode = Integer.parseInt(statusLine.split(" ")[1]);
            if (responseType == null || statusCode < 200 || statusCode >= 300) {
                return new HttpResponse<>(statusLine, headers, byteBuffer.getAllBytes());
            } else {
                return new HttpResponse<>(statusLine, headers, byteBuffer.getAllBytes(), responseType);
            }
        } catch (Exception e) {
            HttpResponse<T> httpResponse = new HttpResponse<>();
            httpResponse.failed = true;
            httpResponse.exception = new HttpRequestException(new ResponseParseException((e)));
            return httpResponse;
        }
    }
}
