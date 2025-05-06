package com.example.test.practice.practice03;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OrderService {

    // 상품 재고 저장소 (상품 ID → 재고 수량)
    private final Map<String, AtomicInteger> productStock = new ConcurrentHashMap<>();

    // 주문 저장소 (주문 ID → 주문 객체)
    private final Map<String, Order> orderStore = new ConcurrentHashMap<>();
    private final ApplicationEventPublisher eventPublisher;

    public OrderService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
        productStock.put("prod-1", new AtomicInteger(10));
    }


    public String createOrder(String productId, int quantity) {
        AtomicInteger stock = productStock.get(productId);
        if (stock == null) {
            throw new IllegalArgumentException("상품이 존재하지 않습니다.");
        }

        while (true) {
            int currentStock = stock.get();

            // 1. 재고 부족하면 예외 처리
            if (currentStock < quantity) {
                throw new OutOfStockException("재고 부족");
            }

            // 2. CAS 연산으로 재고 차감 시도 (성공하면 진행, 실패하면 while 재시도)
            boolean updated = stock.compareAndSet(currentStock, currentStock - quantity);
            if (updated) {
                // 3. 주문 생성 및 저장
                String orderId = UUID.randomUUID().toString();
                Order order = new Order(orderId, productId, quantity);
                orderStore.put(orderId, order);

                // 🔥 이벤트 발행 (비동기 처리 예정)
                eventPublisher.publishEvent(new OrderCreatedEvent(orderId, productId, quantity));
                return orderId;
            }
            // 4. 실패 → 다른 스레드가 먼저 바꿈 → 다시 시도
        }
    }

    public Order getOrder(String orderId) {
        return orderStore.get(orderId);
    }

    public int getStock(String productId) {
        AtomicInteger stock = productStock.get(productId);
        return stock != null ? stock.get() : 0;
    }
}

