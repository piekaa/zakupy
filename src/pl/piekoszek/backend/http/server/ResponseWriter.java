package pl.piekoszek.backend.http.server;

import pl.piekoszek.collections.ByteBuffer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

class ResponseWriter {

    private static final String CRLF = new String(new byte[]{13, 10});

    static void write(OutputStream outputStream, Response response) {
        byte[] body = response.responseBytes;
        String responseLine = response.responseStatus.toRequestLine() + CRLF;
        StringBuilder headers = new StringBuilder();
        for (Map.Entry<String, String> entry : response.headers.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue();
            headers.append(name).append(": ").append(value).append(CRLF);
        }
        headers.append("Content-Length: ").append(body.length).append(CRLF);
        headers.append(CRLF);
        ByteBuffer byteBuffer = new ByteBuffer();
        byteBuffer.add((responseLine + headers).getBytes());
        byteBuffer.add(body);
//        System.out.println(new String(byteBuffer.getAllBytes()));
        try {
            outputStream.write(byteBuffer.getAllBytes());
        } catch (IOException e) {
            throw new ResponseWriteException(e);
        }
    }

}
