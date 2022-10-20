package pl.piekoszek.app.shopping.auth;

import pl.piekoszek.backend.http.server.BasicAuthFunction;
import pl.piekoszek.backend.http.server.BasicAuthMessageHandler;
import pl.piekoszek.mongo.Mongo;

public class ShoppingBasicAuthHandler extends BasicAuthMessageHandler {

    private static final String COLLECTION = "account";

    private Mongo mongo;

    public ShoppingBasicAuthHandler(Mongo mongo) {
        super((username, password) -> {

            var account = mongo.getById(username, COLLECTION, Account.class);

            if (account == null) {
                account = new Account();
                account._id = username;
                account.password = password;
                mongo.insert(COLLECTION, account);
                return true;
            }
            return account.password.equals(password);
        });
        this.mongo = mongo;
    }

    public ShoppingBasicAuthHandler(BasicAuthFunction basicAuthFunction) {
        super(basicAuthFunction);
    }
}
