package pl.piekoszek.backend.http.server;

public class ResponseInfo {

    Object responseBody;
    ResponseStatus responseStatus = ResponseStatus.OK;

    public ResponseInfo(Object responseBody, ResponseStatus responseStatus) {
        this.responseBody = responseBody;
        this.responseStatus = responseStatus;
    }
}
