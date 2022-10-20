package pl.piekoszek.backend.http.server;


import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class BasicAuthMessageHandler implements MessageHandler<Object> {

    private final BasicAuthFunction basicAuthFunction;

    public BasicAuthMessageHandler(BasicAuthFunction basicAuthFunction) {
        this.basicAuthFunction = basicAuthFunction;
    }

    @Override
    public ResponseInfo handle(RequestInfo requestInfo, Object param) {
        if (requestInfo.getRequest().getHeaders().containsKey("Authorization")) {

            var basicAuthString = requestInfo.getRequest().getHeaders().get("Authorization").substring(6);
            var usernameAndPassword = new String(Base64.getDecoder().decode(basicAuthString)).split(":");
            var username = usernameAndPassword[0];
            var password = usernameAndPassword[1];

            if (basicAuthFunction.authenticate(username, password)) {
                requestInfo.authInfo = new AuthInfo(username, password);
                return null;
            }
            return new ResponseInfo("Invalid username, password or authorization type", wwwAuthenticateHeader(), ResponseStatus.UNAUTHORIZED);
        }
        return new ResponseInfo("Authorization header is missing", wwwAuthenticateHeader(), ResponseStatus.UNAUTHORIZED);
    }

    private Map<String, String> wwwAuthenticateHeader() {
        Map<String, String> headers = new HashMap<>();
        headers.put("WWW-Authenticate", "basic");
        return headers;
    }
}
