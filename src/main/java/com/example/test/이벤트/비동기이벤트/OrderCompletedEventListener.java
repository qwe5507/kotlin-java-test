package com.example.test.이벤트.비동기이벤트;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class OrderCompletedEventListener {

    @Async
    @EventListener
    public void handle(OrderCompletedEvent event) {
        System.out.println("[비동기 처리] 주문 완료 알림: " + event.getOrderId());

        // 예시: 슬랙, 이메일 전송 등 느린 작업
        try {
            Thread.sleep(3000); // 실제 외부 API 대기라고 가정
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("[완료] 알림 처리됨");
    }
}

