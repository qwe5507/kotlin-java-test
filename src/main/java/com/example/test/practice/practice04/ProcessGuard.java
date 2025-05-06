package com.example.test.practice.practice04;

import java.util.concurrent.atomic.AtomicBoolean;

class ProcessGuard {
    private final AtomicBoolean processed = new AtomicBoolean(false);

    public boolean tryProcess() {
        return processed.compareAndSet(false, true);
    }
}
