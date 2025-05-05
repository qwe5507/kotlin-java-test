package com.example.test.이벤트.blockingqueueevent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

// 🧠 2. 이벤트 디스패처 (BlockingQueue 보유)
class EventDispatcher {
    private final BlockingQueue<CustomEvent> queue = new LinkedBlockingQueue<>();

    // 이벤트 발행
    public void publish(CustomEvent event) {
        try {
            queue.put(event); // 큐가 꽉 차면 block
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // 이벤트 처리 (소비자 스레드)
    public void start() {
        Thread consumer = new Thread(() -> {
            while (true) {
                try {
                    CustomEvent event = queue.take(); // 큐가 비면 block
                    handleEvent(event);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        consumer.setDaemon(true);
        consumer.start();
    }

    private void handleEvent(CustomEvent event) {
        System.out.println("✅ 이벤트 처리됨: " + event.getMessage());
    }
}