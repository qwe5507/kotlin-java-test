package com.example.test.이벤트.동기이벤트;

// 이벤트 정의 (POJO여도 무방함)
public class OrderCompletedEvent {
    private final Long orderId;
    public OrderCompletedEvent(Long orderId) {
        this.orderId = orderId;
    }
    public Long getOrderId() {
        return orderId;
    }
}
