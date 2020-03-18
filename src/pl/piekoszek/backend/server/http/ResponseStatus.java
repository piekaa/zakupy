package pl.piekoszek.backend.server.http;

public enum ResponseStatus {

    OK(200, "dla mnie się podoba"),
    CREATED(201, "nu tak i ja stworzył"),
    NOT_FOUND(404, "takiego czegoś na pewno nigdy nie było"),
    INTERNAL_SERVER_ERROR(500, "coś, coś się popsuło i nie było mnie słychać");

    public final int code;
    public final String message;

    ResponseStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public final String toRequestLine() {
        return "HTTP/1.1 " + code + " " + message;
    }
}
