package pl.piekoszek.backend.payu;

import java.util.List;

public interface PayuService {

    PayuOrderResponse placeOrder(List<PayuProduct> products,
                                 PayuBuyer payuBuyer,
                                 String notifyUrl,
                                 String continueUrl,
                                 String gsId);

}
