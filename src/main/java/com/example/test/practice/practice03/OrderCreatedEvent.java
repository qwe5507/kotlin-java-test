package com.example.test.practice.practice03;

public class OrderCreatedEvent {
    private final String orderId;
    private final String productId;
    private final int quantity;

    public OrderCreatedEvent(String orderId, String productId, int quantity) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
