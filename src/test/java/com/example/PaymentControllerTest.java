package com.example;

import com.example.controller.ErrorHandler;
import com.example.controller.PaymentController;
import com.example.entity.PaymentRequest;
import com.example.entity.PaymentResponse;
import com.example.exceptions.CardNotAcceptedException;
import com.example.exceptions.DuplicatePaymentException;
import com.example.exceptions.PayBumReliabilityException;
import com.example.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Sam on 11/4/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PayBumApplication.class, PaymentController.class})
public class PaymentControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();

    @Mock
    private PaymentService mockPaymentService;

    @InjectMocks
    private PaymentController paymentController;

    @Before
    public void beforeTests() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController)
                .setControllerAdvice(new ErrorHandler())
                .build();
    }

    @Test
    public void testSuccessfulPayment() throws Exception {
        PaymentRequest request = new PaymentRequest();
        request.setCardNumber(2);
        request.setFrom("Sam");
        request.setPurchaseOrder("purchase-1");

        PaymentResponse response = new PaymentResponse();
        response.setPaymentId("payment-1");

        when(mockPaymentService.makePayment(any())).thenReturn(response);

        mockMvc.perform(post("/payments").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.paymentId", is("payment-1")));

    }

    @Test
    public void testBadCardPayment() throws Exception {
        PaymentRequest request = new PaymentRequest();
        request.setCardNumber(1);
        request.setFrom("Sam");
        request.setPurchaseOrder("purchase-2");

        CardNotAcceptedException e = new CardNotAcceptedException("Bad card");

        when(mockPaymentService.makePayment(any())).thenThrow(e);

        mockMvc.perform(post("/payments").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(request)))
                .andExpect(status().isPaymentRequired());
    }

    @Test
    public void testDuplicatePayment() throws Exception {
        PaymentRequest request = new PaymentRequest();
        request.setCardNumber(2);
        request.setFrom("Sam");
        request.setPurchaseOrder("purchase-3");

        DuplicatePaymentException e = new DuplicatePaymentException("Duplicate payment", "some-existing-purchase-id");

        when(mockPaymentService.makePayment(any())).thenThrow(e);

        mockMvc.perform(post("/payments").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }

    @Test
    public void testPayBumReliability() throws Exception {
        PaymentRequest request = new PaymentRequest();
        request.setCardNumber(2);
        request.setFrom("Sam");
        request.setPurchaseOrder("purchase-4");

        PayBumReliabilityException e = new PayBumReliabilityException("Random failure!!!!");

        when(mockPaymentService.makePayment(any())).thenThrow(e);

        mockMvc.perform(post("/payments").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(mapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

}
