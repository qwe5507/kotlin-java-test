package com.example.test.practice.practice04;

import java.util.concurrent.atomic.AtomicReference;

enum OrderStatus {
    CREATED, PAID, SHIPPED, CANCELLED;
}

// 복잡한 객체 상태를 원자적으로 갱신할 때 사용
// 예: 객체의 상태를 직접 바꾸는 게 아니라, 아예 새 인스턴스로 교체하고 싶을 때
public class Order {
    private final AtomicReference<OrderStatus> status = new AtomicReference<>(OrderStatus.CREATED);

    public void pay() {
        if (!status.compareAndSet(OrderStatus.CREATED, OrderStatus.PAID)) {
            throw new IllegalStateException("❌ 상태 전이 불가");
        }
    }

    public OrderStatus getStatus() {
        return status.get();
    }
}
