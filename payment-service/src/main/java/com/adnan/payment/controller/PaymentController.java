package com.adnan.payment.controller;

import com.adnan.payment.model.CheckoutItem;
import com.adnan.payment.common.StripeResponse;
import com.adnan.payment.service.OrderClientService;
import com.adnan.payment.service.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
//import com.stripe.net.StripeResponse;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentController.class);
    
    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private OrderClientService orderClientService;
    
    @PostMapping("/create-checkout-session")
    public ResponseEntity<StripeResponse> checkoutList(@RequestParam UUID trackingId) throws StripeException {
        LOGGER.info("Fetching order items (trackingId = {}) using FeignClient", trackingId);
        List<CheckoutItem> checkoutItems = orderClientService.getPaymentOrderItemsByTrackingId(trackingId);
        if (checkoutItems == null) {
            LOGGER.error("Order (trackingId = {}) not found", trackingId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (checkoutItems.isEmpty()) {
            LOGGER.error("Unable to get checkout items for order (trackingId = {}), Try again", trackingId);
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }
        LOGGER.info("Creating checkout session for {}", checkoutItems);
        Session session = paymentService.createSession(checkoutItems);
        StripeResponse stripeResponse = new StripeResponse(session.getId());
        return new ResponseEntity<>(stripeResponse, HttpStatus.OK);
    }
    
}
