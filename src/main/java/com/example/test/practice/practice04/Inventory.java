package com.example.test.practice.practice04;

import java.util.concurrent.atomic.AtomicInteger;

public class Inventory {

    private final AtomicInteger stock;

    public Inventory(int initialStock) {
        this.stock = new AtomicInteger(initialStock);
    }

    public void decrease() {
        while (!tryDecrease()) {
            // Retry until successful
        }
    }

    private boolean tryDecrease() {
        int current = stock.get();

        if (current <= 0) {
            throw new IllegalStateException("❌ 재고 없음");
        }

        return stock.compareAndSet(current, current - 1);
    }

    public int getStock() {
        return stock.get();
    }
}

