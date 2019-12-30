package com.piekoszek.app.server.http;

class ImageResponse extends Response {

    ImageResponse(ResponseStatus responseStatus, byte[] responseBody) {
        super(responseStatus, responseBody);
        headers.put("Content-Type", "image/png");
    }
}
