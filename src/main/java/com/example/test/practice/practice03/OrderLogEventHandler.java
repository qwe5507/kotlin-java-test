package com.example.test.practice.practice03;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class OrderLogEventHandler {

    @Async
    @EventListener
    public void handle(OrderCreatedEvent event) {
        System.out.printf("[비동기 로그] 주문 생성됨 → orderId=%s, productId=%s, quantity=%d%n",
                event.getOrderId(), event.getProductId(), event.getQuantity());

        try {
            Thread.sleep(1000); // 처리 지연 시뮬레이션
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
