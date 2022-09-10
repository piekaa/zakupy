package pl.piekoszek.app.payu;

import pl.piekoszek.backend.http.client.HttpRequestSender;
import pl.piekoszek.backend.http.client.HttpResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class PayuServiceImpl implements PayuService {

    private HttpRequestSender httpRequestSender;
    private String accessToken = "";
    private long tokenExpireTime = 0;

    PayuServiceImpl(HttpRequestSender httpRequestSender) {
        this.httpRequestSender = httpRequestSender;
    }

    public PayuOrderResponse placeOrder(List<PayuProduct> products, PayuBuyer payuBuyer, String notifyUrl, String continueUrl, String gsId) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token());

        PayuOrder payuOrder = new PayuOrder();
        payuOrder.continueUrl = continueUrl;
        payuOrder.notifyUrl = notifyUrl;
        payuOrder.products = products;
        payuOrder.payuBuyer = payuBuyer;
        payuOrder.totalAmount = products
                .stream()
                .mapToInt(product -> product.unitPrice * product.quantity)
                .reduce(0, Integer::sum);
        payuOrder.extOrderId = gsId;
        HttpResponse<PayuOrderResponse> orderResponse = httpRequestSender.post("api/v2_1/orders", headers, payuOrder, PayuOrderResponse.class);
        return orderResponse.body;
    }

    private String token() {
        long now = System.currentTimeMillis();
        if (now >= tokenExpireTime + 60) {
            HttpResponse<PayuAuthResponse> payuAuthResponseHttpResponse = httpRequestSender.postRawText("pl/standard/user/oauth/authorize",
                    "grant_type=client_credentials&client_id=386112&client_secret=272fc5277e07b27682e83da8ccd4e9b8",
                    PayuAuthResponse.class);
            if (!payuAuthResponseHttpResponse.successfulStatus) {
                throw new IllegalStateException("Request failed with status: "
                                                + payuAuthResponseHttpResponse.statusCode + " "
                                                + payuAuthResponseHttpResponse.statusText + ", body: "
                                                + payuAuthResponseHttpResponse.bodyMap.get("_raw_body_text"));
            }
            tokenExpireTime = now + payuAuthResponseHttpResponse.body.expires_in;
            accessToken = payuAuthResponseHttpResponse.body.access_token;
        }
        return accessToken;
    }
}
