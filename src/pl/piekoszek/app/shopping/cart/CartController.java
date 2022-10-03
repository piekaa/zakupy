package pl.piekoszek.app.shopping.cart;

import pl.piekoszek.app.shopping.item.Item;
import pl.piekoszek.backend.http.server.*;
import pl.piekoszek.json.Piekson;
import pl.piekoszek.mongo.Mongo;

import java.util.stream.Collectors;


class CartController implements EndpointsProvider {

    private static final String COLLECTION = "item";

    private Mongo mongo;

    CartController(Mongo mongo) {
        this.mongo = mongo;
    }

    private MessageHandler<Object> addToCart = (info, body) -> {
        setInCart(info, true);
        return new ResponseInfo(ResponseStatus.OK);
    };

    private MessageHandler<Object> removeFromCart = (info, body) -> {
        setInCart(info, false);
        return new ResponseInfo(ResponseStatus.OK);
    };

    private MessageHandler<FinishRequest> finish = (info, body) -> {
//        body.categories.forEach(category -> {
        var query = """
                {
                  "inCart": true,
                  "categories": {
                    "$elemMatch": {
                        "_id": {
                            "$in": %s
                        }
                    }
                  }
                }
                """.formatted(Piekson.toJson(body.categories));
        System.out.println(query);
        mongo.delete(COLLECTION, query);
//        });


        return new ResponseInfo(ResponseStatus.OK);
    };

    private void setInCart(RequestInfo requestInfo, boolean inCart) {
        var item = mongo.getById(requestInfo.getPathParams().get("id"), COLLECTION, Item.class);
        item.inCart = inCart;
        mongo.update(COLLECTION, item);
    }

    @Override
    public EndpointInfo[] endpoints() {
        return new EndpointInfo[]{
                new EndpointInfo("PUT", "/api/cart/:id", addToCart, Object.class),
                new EndpointInfo("POST", "/api/cart/finish", finish, FinishRequest.class),
                new EndpointInfo("DELETE", "/api/cart/:id", removeFromCart, Object.class)
        };
    }
}
