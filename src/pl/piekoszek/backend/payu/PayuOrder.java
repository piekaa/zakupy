package pl.piekoszek.backend.payu;

import java.util.List;

public class PayuOrder {

    public String notifyUrl = "https://gs.piekoszek.pl/statusCallback";
    public String customerIp = "127.0.0.1";
    public String merchantPosId = "386112";
    public String description = "Samopomoc chłopska";
    public String currencyCode = "PLN";
    public int totalAmount;
    public String continueUrl = "https://gs.piekoszek.pl/kupiono.html";
    public PayuBuyer payuBuyer;
    public PayuSettings settings = new PayuSettings();
    public List<PayuProduct> products;
    public String status = "new";
    public String orderId;
    public String extOrderId;
}
