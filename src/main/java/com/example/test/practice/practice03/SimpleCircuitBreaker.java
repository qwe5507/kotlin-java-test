package com.example.test.practice.practice03;

import java.util.function.Supplier;

public class SimpleCircuitBreaker {

    private final int failureThreshold;
    private final long resetTimeoutMillis;

    private CircuitBreakerState state = CircuitBreakerState.CLOSED;
    private int failureCount = 0;
    private long lastFailureTime = 0;

    public SimpleCircuitBreaker(int failureThreshold, long resetTimeoutMillis) {
        this.failureThreshold = failureThreshold;
        this.resetTimeoutMillis = resetTimeoutMillis;
    }

    public synchronized <T> T call(Supplier<T> action, Supplier<T> fallback) {
        long now = System.currentTimeMillis();

        if (state == CircuitBreakerState.OPEN) {
            if (now - lastFailureTime >= resetTimeoutMillis) {
                state = CircuitBreakerState.HALF_OPEN;
            } else {
                return fallback.get(); // 즉시 fallback
            }
        }

        try {
            T result = action.get();
            onSuccess();
            return result;
        } catch (Exception e) {
            onFailure();
            return fallback.get(); // 실패 시 fallback
        }
    }

    private void onSuccess() {
        failureCount = 0;
        state = CircuitBreakerState.CLOSED;
    }

    private void onFailure() {
        failureCount++;
        lastFailureTime = System.currentTimeMillis();

        if (failureCount >= failureThreshold) {
            state = CircuitBreakerState.OPEN;
        }
    }
}

