package com.bitforce.tuteme.service;

import java.util.HashMap;
import java.util.Map;
import static spark.Spark.post;
import static spark.Spark.port;
import com.google.gson.Gson;
import com.stripe.Stripe;
import com.stripe.model.billingportal.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PaymentService {
    private static Gson gson = new Gson();

    public static void GetPayment() {
        port(4242);
        Stripe.apiKey = "sk_test_51Ij62ESDEHDb4u6zRCfvkJ0kCatsHE8iwWD5QJ2oSbhYUAraa4Hk91Ny8LBWFTD1zXcyfqCnVQ0OCup3irjsGWfT00nObf2x7B";

        post("/create-checkout-session", (request, response) -> {
            response.type("application/json");

            SessionCreateParams params =
                    SessionCreateParams.builder()
                            .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                            .setMode(SessionCreateParams.Mode.PAYMENT)
                            .setSuccessUrl("https://example.com/success")
                            .setCancelUrl("https://example.com/cancel")
                            .addLineItem(
                                    SessionCreateParams.LineItem.builder()
                                            .setQuantity(1L)
                                            .setPriceData(
                                                    SessionCreateParams.LineItem.PriceData.builder()
                                                            .setCurrency("usd")
                                                            .setUnitAmount(2000L)
                                                            .setProductData(
                                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                            .setName("Subject Payment")
                                                                            .build())
                                                            .build())
                                            .build())
                            .build();

            Session session = Session.create((Map<String, Object>) params);

            Map<String, String> responseData = new HashMap();
            responseData.put("id", session.getId());

            return gson.toJson(responseData);
        });
    }
}
