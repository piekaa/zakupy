package pl.piekoszek.backend.server.http;

public class RequestInfo {

    private Request request;

    public RequestInfo(Request request) {
        this.request = request;
    }

    public Request getRequest() {
        return request;
    }
}
