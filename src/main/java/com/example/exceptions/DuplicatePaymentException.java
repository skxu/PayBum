package com.example.exceptions;

/**
 * Created by Sam on 11/4/2016.
 */
public class DuplicatePaymentException extends RuntimeException {

    private String paymentId;

    public DuplicatePaymentException(String message, String paymentId) {
        super(message);
        this.paymentId = paymentId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
}
