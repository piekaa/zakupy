package pl.piekoszek.app.gs;

import pl.piekoszek.backend.http.server.*;
import pl.piekoszek.app.payu.PayuBuyer;
import pl.piekoszek.app.payu.PayuOrderResponse;
import pl.piekoszek.app.payu.PayuProduct;
import pl.piekoszek.app.payu.PayuService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

class ShopController implements EndpointsProvider {

    private PayuService payuService;
    private ProductRepository productRepository;
    private OrderRepository orderRepository;

    ShopController(PayuService payuService, ProductRepository productRepository, OrderRepository orderRepository) {
        this.payuService = payuService;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    private MessageHandler<Object> getAll = (info, body) -> productRepository.getAll();

    private MessageHandler<Object> getOne = (info, body) -> {
        Optional<Product> optionalProduct = productRepository.getOne(info.getPathParams().get("id"));
        if (optionalProduct.isPresent()) {
            return optionalProduct.get();
        }
        return new ResponseInfo(ResponseStatus.NOT_FOUND);
    };


    private MessageHandler<Product> insertProduct = (info, body) -> {
        body._id = UUID.randomUUID().toString();
        productRepository.insert(body);
        return body;
    };

    private MessageHandler<OrderRequest> order = (info, body) -> {

        PayuBuyer payuBuyer = new PayuBuyer();
        payuBuyer.email = body.buyer.email;
        payuBuyer.phone = body.buyer.phone;
        payuBuyer.firstName = body.buyer.firstName;
        payuBuyer.lastName = body.buyer.lastName;

        PayuProduct payuProduct = new PayuProduct();
        payuProduct.quantity = 1;
        payuProduct.unitPrice = body.product.price;
        payuProduct.name = body.product.name;

        List<PayuProduct> products = new ArrayList<>();
        products.add(payuProduct);

        String gsId = UUID.randomUUID().toString();

        PayuOrderResponse payuOrderResponse = payuService.placeOrder(products, payuBuyer,
                "https://gs.piekoszek.pl/gs/orderStatus",
                "https://gs.piekoszek.pl/kupiono.html?id=" + gsId,
                gsId
        );

        OrderResponse orderResponse = new OrderResponse();

        OrderStatus orderStatus = new OrderStatus();
        orderStatus._id = payuOrderResponse.extOrderId;
        orderStatus.payuId = payuOrderResponse.orderId;
        orderStatus.product = body.product;

        orderRepository.insert(orderStatus);

        orderResponse.redirectUrl = payuOrderResponse.redirectUri;
        return orderResponse;
    };

    private MessageHandler<StatusUpdateRequest> updateStatus = (info, body) -> {
        orderRepository.updateStatus(body.order.extOrderId, body.order.status);
        return new ResponseInfo(ResponseStatus.OK);
    };

    private MessageHandler<Object> getStatus = (info, body) -> orderRepository.get(info.getPathParams().get("id"));

    private MessageHandler<Object> addSample = (info, body) -> {

        productRepository.insert(new Product(UUID.randomUUID().toString(), "gar.jpg", "Gar", 12000));
        productRepository.insert(new Product(UUID.randomUUID().toString(), "bimber.jpg", "Bimber", 136000));
        productRepository.insert(new Product(UUID.randomUUID().toString(), "stodola.jpg", "Stodoła", 600000));
        productRepository.insert(new Product(UUID.randomUUID().toString(), "balwan.jpg", "Bałwan", 5000));
        productRepository.insert(new Product(UUID.randomUUID().toString(), "pajac.jpg", "Pajac", 9000));
        productRepository.insert(new Product(UUID.randomUUID().toString(), "zigolo.jpg", "Boski żigolo", 600000000));
        productRepository.insert(new Product(UUID.randomUUID().toString(), "baranina.jpg", "Baranina", 3100000));
        productRepository.insert(new Product(UUID.randomUUID().toString(), "kogut.jpg", "Kogut", 25000));
        productRepository.insert(new Product(UUID.randomUUID().toString(), "suka.jpg", "Suka", 100000000));
        productRepository.insert(new Product(UUID.randomUUID().toString(), "dentka.jpg", "Dentka", 7000));
        productRepository.insert(new Product(UUID.randomUUID().toString(), "lozko.jpg", "Łóżko", 130000));

        return new ResponseInfo(ResponseStatus.OK);
    };

    @Override
    public EndpointInfo[] endpoints() {
        return new EndpointInfo[]{
                new EndpointInfo("GET", "/gs/product", getAll, Object.class),
                new EndpointInfo("GET", "/gs/product/:id", getOne, Object.class),
                new EndpointInfo("POST", "/gs/product", insertProduct, Product.class),
                new EndpointInfo("POST", "/gs/order", order, OrderRequest.class),
                new EndpointInfo("POST", "/gs/orderStatus", updateStatus, StatusUpdateRequest.class),
                new EndpointInfo("GET", "/gs/orderStatus/:id", getStatus, Object.class),
                new EndpointInfo("POST", "/gs/sample", addSample, Object.class),
        };
    }

}
