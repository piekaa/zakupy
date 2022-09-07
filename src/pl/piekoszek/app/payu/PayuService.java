package pl.piekoszek.app.payu;

import java.util.List;

public interface PayuService {

    PayuOrderResponse placeOrder(List<PayuProduct> products,
                                 PayuBuyer payuBuyer,
                                 String notifyUrl,
                                 String continueUrl,
                                 String gsId);

}
