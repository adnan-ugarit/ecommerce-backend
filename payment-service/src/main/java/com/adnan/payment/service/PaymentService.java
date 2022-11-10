package com.adnan.payment.service;

import com.adnan.payment.model.CheckoutItem;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    
    @Value("${baseURL}")
    private String baseURL;
    
    @Value("${STRIPE_SECRET_KEY}")
    private String apiKey;
    
    SessionCreateParams.LineItem.PriceData createPriceData(CheckoutItem checkoutItem) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("usd")
                .setUnitAmount((long)(checkoutItem.getProductPrice() * 100))
                .setProductData(
                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName(checkoutItem.getProductName())
                                //.addImage(checkoutItemDto.getImageUrl())
                                .build())
                .build();
    }
    
    SessionCreateParams.LineItem createSessionLineItem(CheckoutItem checkoutItem) {
        return SessionCreateParams.LineItem.builder()
                .setPriceData(createPriceData(checkoutItem))
                .setQuantity(Long.parseLong(String.valueOf(checkoutItem.getQuantity())))
                .build();
    }
    
    public Session createSession(List<CheckoutItem> checkoutItems) throws StripeException {

        String successURL = baseURL + "/payment/success";
        String failedURL = baseURL + "/payment/failed";
        
        Stripe.apiKey = apiKey;
        
        List<SessionCreateParams.LineItem> sessionItemsList = new ArrayList<SessionCreateParams.LineItem>();
        for (CheckoutItem checkoutItem : checkoutItems) {
            sessionItemsList.add(createSessionLineItem(checkoutItem));
        }
        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCancelUrl(failedURL)
                .addAllLineItem(sessionItemsList)
                .setSuccessUrl(successURL)
                .build();
        return Session.create(params);
    }
    
}
