package com.example.service;

import com.example.entity.PaymentResponse;
import com.example.entity.PaymentRequest;

/**
 * Created by Sam on 11/4/2016.
 */
public interface PaymentService {

    void init();

    PaymentResponse makePayment(PaymentRequest paymentRequest);

    PaymentResponse findOne(PaymentRequest paymentRequest);

}
