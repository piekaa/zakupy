package pl.piekoszek.backend.http.server;

public class AuthInfo {
    public final String username;
    public final String password;

    public AuthInfo(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
