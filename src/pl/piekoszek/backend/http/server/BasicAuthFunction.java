package pl.piekoszek.backend.http.server;

public interface BasicAuthFunction {

    /**
     *
     * @return true if Authorized
     */
    boolean authenticate(String username, String password);
}
