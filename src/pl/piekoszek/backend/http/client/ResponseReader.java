package pl.piekoszek.backend.http.client;

import pl.piekoszek.collections.ByteBuffer;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

class ResponseReader {

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
                byteBuffer.readOneByte(inputStream);
                if (byteBuffer.endsWith1310()) {
                    break;
                }
            }
            statusLine = new String(byteBuffer.getAllBytes(), StandardCharsets.UTF_8);
            byteBuffer = new ByteBuffer();
            for (; ; ) {
                byteBuffer.readOneByte(inputStream);
                if (byteBuffer.endsWith13101310()) {
                    break;
                }
            }
            headersText = new String(byteBuffer.getAllBytes(), StandardCharsets.UTF_8);
            Map<String, String> headers = new HashMap<>();
            String[] splitedHeaders = headersText.split(new String(new byte[]{13, 10}));
            for (String header : splitedHeaders) {
                if (!header.contains(":")) {
                    throw new ResponseParseException("Header does not contain :");
                }
                String[] nameAndValue = header.split(":", 2);
                headers.put(nameAndValue[0].trim().toLowerCase(), nameAndValue[1].trim());
            }

            if ("chunked".equals(headers.get("transfer-encoding"))) {
                byteBuffer = readChunkedBody(inputStream);
            } else if (headers.containsKey("content-length")) {
                int contentLength = Integer.parseInt(headers.get("content-length"));
                byteBuffer = readBody(inputStream, contentLength);
            } else {
                System.out.println("No content-length or transfer-coding");
                return new HttpResponse<>(statusLine, headers);
            }

            int statusCode = Integer.parseInt(statusLine.split(" ")[1]);
            if (responseType == null || statusCode < 200 || statusCode >= 400) {
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

    private static ByteBuffer readBody(InputStream inputStream, int contentLength) {
        ByteBuffer byteBuffer = new ByteBuffer();
        for (int i = 0; i < contentLength; i++) {
            byteBuffer.readOneByte(inputStream);
        }
        return byteBuffer;
    }

    private static ByteBuffer readChunkedBody(InputStream inputStream) {
        ByteBuffer bodyBuffer = new ByteBuffer();
        for (; ; ) {
            int chunkSize = chunkSize(inputStream);
            if (chunkSize == 0) {
                ByteBuffer clRfClRfBuffer = new ByteBuffer();
                clRfClRfBuffer.add((byte) 13);
                clRfClRfBuffer.add((byte) 10);
                while (!clRfClRfBuffer.endsWith13101310()) {
                    clRfClRfBuffer.readOneByte(inputStream);
                }
                break;
            }
            for (int i = 0; i < chunkSize; i++) {
                bodyBuffer.readOneByte(inputStream);
            }

            ByteBuffer clRfBuffer = new ByteBuffer();
            clRfBuffer.readOneByte(inputStream);
            clRfBuffer.readOneByte(inputStream);

            if (!clRfBuffer.endsWith1310()) {
                throw new ResponseParseException("Chunk data doesn't end with 1310");
            }

        }
        return bodyBuffer;
    }

    private static int chunkSize(InputStream inputStream) {

        ByteBuffer byteBuffer = new ByteBuffer();
        while (!byteBuffer.endsWith1310()) {
            byteBuffer.readOneByte(inputStream);
        }
        byte[] bytes = byteBuffer.getAllBytes();
        String line = new String(bytes, 0, bytes.length - 2, StandardCharsets.UTF_8);
        String hex;
        hex = line.contains(";") ? line.split(";", 2)[0] : line;
        return Integer.parseInt(hex, 16);
    }
}