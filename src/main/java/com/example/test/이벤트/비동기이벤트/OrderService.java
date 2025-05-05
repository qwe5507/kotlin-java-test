package com.example.test.이벤트.비동기이벤트;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final ApplicationEventPublisher eventPublisher;

    public OrderService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void completeOrder(Long orderId) {
        System.out.println("[서비스] 주문 완료 처리 로직 실행 중...");
        eventPublisher.publishEvent(new OrderCompletedEvent(orderId));
        System.out.println("[서비스] 주문 완료 이벤트 발행됨");
    }
}

