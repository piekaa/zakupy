package pl.piekoszek.app.shopping.cart;

import pl.piekoszek.app.shopping.auth.CollectionUtil;
import pl.piekoszek.app.shopping.item.Item;
import pl.piekoszek.backend.http.server.*;
import pl.piekoszek.json.Piekson;
import pl.piekoszek.mongo.Mongo;


class CartController implements EndpointsProvider {

    private static final String COLLECTION = "item";

    private Mongo mongo;
    private BasicAuthMessageHandler basicAuthMessageHandler;

    CartController(Mongo mongo, BasicAuthMessageHandler basicAuthMessageHandler) {
        this.mongo = mongo;
        this.basicAuthMessageHandler = basicAuthMessageHandler;
    }

    private MessageHandler<Object> addToCart = (info, body) -> {
        setInCart(info, true, info);
        return new ResponseInfo(ResponseStatus.OK);
    };

    private MessageHandler<Object> removeFromCart = (info, body) -> {
        setInCart(info, false, info);
        return new ResponseInfo(ResponseStatus.OK);
    };

    private MessageHandler<Object> addToMissing = (info, body) -> {
        setMissing(info, true, info);
        return new ResponseInfo(ResponseStatus.OK);
    };

    private MessageHandler<Object> removeFromMissing = (info, body) -> {
        setMissing(info, false, info);
        return new ResponseInfo(ResponseStatus.OK);
    };

    private MessageHandler<FinishRequest> finish = (info, body) -> {
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
        mongo.delete(CollectionUtil.collectionByUser(COLLECTION, info), query);

        return new ResponseInfo(ResponseStatus.OK);
    };

    private void setInCart(RequestInfo requestInfo, boolean inCart, RequestInfo info) {
        var item = mongo.getById(requestInfo.getPathParams().get("id"), CollectionUtil.collectionByUser(COLLECTION, info), Item.class);
        item.inCart = inCart;
        item.missing = false;
        mongo.update(CollectionUtil.collectionByUser(COLLECTION, info), item);
    }

    private void setMissing(RequestInfo requestInfo, boolean missing, RequestInfo info) {
        var item = mongo.getById(requestInfo.getPathParams().get("id"), CollectionUtil.collectionByUser(COLLECTION, info), Item.class);
        item.missing = missing;
        mongo.update(CollectionUtil.collectionByUser(COLLECTION, info), item);
    }

    @Override
    public EndpointInfo[] endpoints() {
        return new EndpointInfo[]{
                new EndpointInfo("PUT", "/api/cart/:id", addToCart, basicAuthMessageHandler, Object.class),
                new EndpointInfo("DELETE", "/api/cart/:id", removeFromCart, basicAuthMessageHandler, Object.class),
                new EndpointInfo("PUT", "/api/missing/:id", addToMissing, basicAuthMessageHandler, Object.class),
                new EndpointInfo("DELETE", "/api/missing/:id", removeFromMissing, basicAuthMessageHandler, Object.class),
                new EndpointInfo("POST", "/api/cart/finish", finish, basicAuthMessageHandler, FinishRequest.class)
        };
    }
}
