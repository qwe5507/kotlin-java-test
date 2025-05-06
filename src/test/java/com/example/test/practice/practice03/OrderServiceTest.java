package com.example.test.practice.practice03;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Test
    void 동시에_여러명_주문해도_재고는_정확히_차감된다() throws InterruptedException {
        int threadCount = 20;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger();

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    orderService.createOrder("prod-1", 1);
                    successCount.incrementAndGet();
                } catch (OutOfStockException ignored) {
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        assertEquals(10, successCount.get());
        assertEquals(0, orderService.getStock("prod-1"));
    }
}

