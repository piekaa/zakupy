package pl.piekoszek.app.shopping.auth;

import pl.piekoszek.backend.http.server.RequestInfo;

public class CollectionUtil {

    public static final String collectionByUser(String collection, RequestInfo requestInfo) {
        return collectionByUser(collection, requestInfo.getAuthInfo().username);
    }

    public static final String collectionByUser(String collection, String username) {
        return collection + "_" + username;
    }
}
