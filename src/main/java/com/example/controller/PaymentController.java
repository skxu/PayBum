package com.example.controller;

import com.example.entity.PaymentResponse;
import com.example.entity.PaymentRequest;
import com.example.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Sam on 11/4/2016.
 */

@RestController
@RequestMapping(value="/payments", produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @RequestMapping(method= RequestMethod.POST)
    public PaymentResponse makePayment(@RequestBody PaymentRequest paymentRequest) {
        return paymentService.makePayment(paymentRequest);
    }

}
