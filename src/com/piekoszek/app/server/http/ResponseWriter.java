package com.piekoszek.app.server.http;

import com.piekoszek.app.collections.ByteBuffer;

import java.io.IOException;
import java.io.OutputStream;

class ResponseWriter {

    private static final String CRLF = new String(new byte[]{13, 10});

    static void write(OutputStream outputStream, Response response) throws IOException {

        byte[] text = "Siemano siemano mexicano tv otwiera wam drzwi".getBytes();

        String responseString = "HTTP/1.1 " + response.statusCode + " gitarka" + CRLF +
                "Content-Length: " + text.length + CRLF + CRLF;

        ByteBuffer byteBuffer = new ByteBuffer();
        byteBuffer.add(responseString.getBytes());
        byteBuffer.add(text);

        System.out.println("Response:");
        System.out.println(responseString);

        outputStream.write(byteBuffer.getAllBytes());
    }

}
