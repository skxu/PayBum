package com.example.controller;

import com.example.entity.PaymentResponse;
import com.example.exceptions.CardNotAcceptedException;
import com.example.exceptions.DuplicatePaymentException;
import com.example.exceptions.PayBumReliabilityException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Created by Sam on 11/4/2016.
 */
@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler({ CardNotAcceptedException.class })
    protected ResponseEntity<Object> handleInvalidCard(RuntimeException e, WebRequest request) {
        return handleExceptionInternal(e, null, null, HttpStatus.PAYMENT_REQUIRED, request);
    }

    @ExceptionHandler({ DuplicatePaymentException.class })
    protected ResponseEntity<Object> handleDuplicatePayment(RuntimeException e, WebRequest request) {
        DuplicatePaymentException exception = (DuplicatePaymentException) e;
        PaymentResponse response = new PaymentResponse();
        response.setPaymentId(exception.getPaymentId());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        return handleExceptionInternal(e, response, headers, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler({PayBumReliabilityException.class })
    protected ResponseEntity<Object> handlePayBumReliabilityException(RuntimeException e, WebRequest request) {
        return handleExceptionInternal(e, null, null, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }


}