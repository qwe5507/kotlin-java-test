package com.example.test.practice.practice01;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class InventoryManager {
    private final AtomicInteger stock;

    public InventoryManager(int initialStock) {
        this.stock = new AtomicInteger(initialStock);
    }

    public boolean decreaseStock() {
        while (true) {
            int current = stock.get();
            if (current <= 0) return false;
            if (stock.compareAndSet(current, current - 1)) return true;
        }
    }

    public int getStock() {
        return stock.get();
    }
}

