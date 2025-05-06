package com.example.test.이벤트.동기이벤트;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

// 이벤트 발행 예시
@Service
public class OrderService {
    private final ApplicationEventPublisher publisher;

    public OrderService(final ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void completeOrder(Long orderId) {
        System.out.println("✅ 주문 처리 완료: " + orderId);
        publisher.publishEvent(new OrderCompletedEvent(orderId)); // 동기 호출
    }
}

