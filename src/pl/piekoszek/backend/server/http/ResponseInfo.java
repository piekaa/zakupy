package pl.piekoszek.backend.server.http;

public class ResponseInfo {

    Object responseBody;
    ResponseStatus responseStatus = ResponseStatus.OK;

    public ResponseInfo(Object responseBody, ResponseStatus responseStatus) {
        this.responseBody = responseBody;
        this.responseStatus = responseStatus;
    }
}
