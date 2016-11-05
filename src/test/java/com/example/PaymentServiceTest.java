package com.example;

import com.example.entity.PaymentRequest;
import com.example.entity.PaymentResponse;
import com.example.exceptions.CardNotAcceptedException;
import com.example.exceptions.DuplicatePaymentException;
import com.example.exceptions.PayBumReliabilityException;
import com.example.service.PaymentService;
import com.example.service.PaymentServiceImpl;
import com.example.service.ReliabilityService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Mockito.when;

/**
 * Created by Sam on 11/4/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class PaymentServiceTest {

    @Mock
    private ReliabilityService reliabilityService;

    @InjectMocks
    private PaymentService paymentService = new PaymentServiceImpl();

    @Before
    public void beforeTests() {
        MockitoAnnotations.initMocks(this);
        paymentService.init();
    }

    @Test
    public void testSuccessfulPayment() {
        when(reliabilityService.isReliable()).thenReturn(true);
        PaymentRequest request = new PaymentRequest();
        request.setPurchaseOrder("purchase-1");
        request.setFrom("Sam");
        request.setCardNumber(2);

        PaymentResponse response = paymentService.makePayment(request);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getPaymentId());
    }

    @Test(expected = CardNotAcceptedException.class)
    public void testOddCardPayment() {
        when(reliabilityService.isReliable()).thenReturn(true);
        PaymentRequest request = new PaymentRequest();
        request.setPurchaseOrder("purchase-2");
        request.setFrom("Sam");
        request.setCardNumber(1);

        // should throw exception since card is odd
        paymentService.makePayment(request);
        Assert.fail();
    }

    @Test(expected = DuplicatePaymentException.class)
    public void testDuplicatePayment() {
        when(reliabilityService.isReliable()).thenReturn(true);
        PaymentRequest request = new PaymentRequest();
        request.setPurchaseOrder("purchase-3");
        request.setFrom("Sam");
        request.setCardNumber(4);

        PaymentResponse response = paymentService.makePayment(request);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getPaymentId());
        // should throw exception since payment has already been made
        paymentService.makePayment(request);
        Assert.fail();
    }

    @Test(expected = PayBumReliabilityException.class)
    public void testReliabilityException() {
        when(reliabilityService.isReliable()).thenReturn(false);
        PaymentRequest request = new PaymentRequest();
        request.setPurchaseOrder("purchase-4");
        request.setFrom("Sam");
        request.setCardNumber(4);

        // should throw exception due to relability service returning false
        paymentService.makePayment(request);
        Assert.fail();
    }
}
