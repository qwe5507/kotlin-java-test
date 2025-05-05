package com.example.test.이벤트.비동기이벤트;

public class OrderCompletedEvent {
    private final Long orderId;

    public OrderCompletedEvent(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }
}

