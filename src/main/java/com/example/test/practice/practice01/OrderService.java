package com.example.test.practice.practice01;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OrderService {
    private final InventoryManager inventoryManager = new InventoryManager(100);
    private final ExecutorService orderExecutor = Executors.newFixedThreadPool(10);
    private final ScheduledExecutorService retryExecutor = Executors.newScheduledThreadPool(5);

    private final EmailService emailService = new EmailService();

    public void placeOrder(String userId) {
        orderExecutor.submit(() -> {
            if (inventoryManager.decreaseStock()) {
                System.out.println("Order placed by: " + userId);
                publishOrderCreatedEvent(userId + "@test.com");
            } else {
                System.out.println("Order failed - out of stock for user: " + userId);
            }
        });
    }

    private void publishOrderCreatedEvent(String email) {
        retryExecutor.execute(() -> trySendEmail(email, 3));
    }

    private void trySendEmail(String email, int retries) {
        try {
            emailService.send(email);
        } catch (Exception e) {
            if (retries > 1) {
                retryExecutor.schedule(() -> trySendEmail(email, retries - 1), 2, TimeUnit.SECONDS);
            } else {
                System.out.println("Email send failed after retries: " + email);
            }
        }
    }

    public void shutdown() {
        orderExecutor.shutdown();
        retryExecutor.shutdown();
    }

    public static void main(String[] args) throws InterruptedException {
        OrderService orderService = new OrderService();
        for (int i = 0; i < 100; i++) {
            orderService.placeOrder("user" + i);
        }

        Thread.sleep(10000); // wait for all tasks to complete
        orderService.shutdown();
        System.out.println("Remaining stock: " + orderService.inventoryManager.getStock());
    }
}
