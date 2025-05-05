package com.example.test.이벤트.동기이벤트;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

// 이벤트 리스너
@Component
public class NotificationEventListener {

    @EventListener
    public void handleOrderCompleted(OrderCompletedEvent event) {
        System.out.println("📢 주문 완료 알림 전송: " + event.getOrderId());
    }
}

