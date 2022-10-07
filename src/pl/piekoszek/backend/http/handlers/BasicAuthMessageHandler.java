package pl.piekoszek.backend.http.handlers;

import pl.piekoszek.backend.http.server.*;


import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class BasicAuthMessageHandler implements MessageHandler<Object> {

    private final String basicAuthString;

    public BasicAuthMessageHandler(String username, String password) {
        basicAuthString = "Basic " + new String(Base64.getEncoder().encode((username + ":" + password).getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

    @Override
    public ResponseInfo handle(RequestInfo requestInfo, Object param) {
        if (requestInfo.getRequest().getHeaders().containsKey("Authorization")) {
            if (requestInfo.getRequest().getHeaders().get("Authorization").equals(basicAuthString)) {
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
