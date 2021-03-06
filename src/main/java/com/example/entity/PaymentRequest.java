package com.example.entity;

/**
 * Created by Sam on 11/4/2016.
 */

public class PaymentRequest {

    private String from;
    private Integer cardNumber;
    private String purchaseOrder;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Integer getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Integer cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(String purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PaymentRequest request = (PaymentRequest) o;

        if (from != null ? !from.equals(request.from) : request.from != null) return false;
        if (cardNumber != null ? !cardNumber.equals(request.cardNumber) : request.cardNumber != null) return false;
        return purchaseOrder != null ? purchaseOrder.equals(request.purchaseOrder) : request.purchaseOrder == null;

    }

    @Override
    public int hashCode() {
        int result = from != null ? from.hashCode() : 0;
        result = 31 * result + (cardNumber != null ? cardNumber.hashCode() : 0);
        result = 31 * result + (purchaseOrder != null ? purchaseOrder.hashCode() : 0);
        return result;
    }
}
