package com.example.entity;

import org.springframework.stereotype.Component;

/**
 * Created by Sam on 11/4/2016.
 */
@Component
public class PaymentResponse {

    private String paymentId;

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
}
