package com.piekoszek.app.server.http;

import com.piekoszek.app.collections.ByteBuffer;

import java.io.IOException;
import java.io.InputStream;

class RequestReader {
    static Request read(InputStream inputStream) {
        System.out.println();
        ByteBuffer byteBuffer = new ByteBuffer();
        String requestLine;
        String headers;
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
            requestLine = new String(byteBuffer.getAllBytes());
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
            headers = new String(byteBuffer.getAllBytes());

            System.out.println("Request Line:");
            System.out.println(requestLine);

            System.out.println();
            System.out.println("Headers:");
            System.out.println(headers);

            return new Request(requestLine, headers);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
