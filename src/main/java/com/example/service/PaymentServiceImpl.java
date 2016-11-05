package com.example.service;

import com.example.entity.PaymentResponse;
import com.example.entity.PaymentRequest;
import com.example.exceptions.CardNotAcceptedException;
import com.example.exceptions.PayBumReliabilityException;
import com.example.exceptions.DuplicatePaymentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Sam on 11/4/2016.
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    private Map<PaymentRequest, PaymentResponse> purchaseOrderToPaymentMap;

    @Autowired
    private ReliabilityService reliabilityService;

    @PostConstruct
    @Override
    public void init() {
        purchaseOrderToPaymentMap = new HashMap<>();
    }

    @Override
    public PaymentResponse makePayment(PaymentRequest paymentRequest) {
        if (reliabilityService.isReliable() == false) {
            // PayBum reliability sucks and will fail on average 9 out of 10 times!
            throw new PayBumReliabilityException("Random failure!");
        }

        PaymentResponse response = findOne(paymentRequest);
        if (response != null) {
            throw new DuplicatePaymentException("Purchase already made", response.getPaymentId());
        }

        if (paymentRequest.getCardNumber() == null || paymentRequest.getCardNumber() % 2 == 1) {
            // Card number is odd or missing
            throw new CardNotAcceptedException("Card number is missing or odd and needs to be even");
        }

        response = new PaymentResponse();
        response.setPaymentId(UUID.randomUUID().toString());
        purchaseOrderToPaymentMap.put(paymentRequest, response);
        return response;
    }

    @Override
    public PaymentResponse findOne(PaymentRequest paymentRequest) {
        return purchaseOrderToPaymentMap.get(paymentRequest);
    }
}
