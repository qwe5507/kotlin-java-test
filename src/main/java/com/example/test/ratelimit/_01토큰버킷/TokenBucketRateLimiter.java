package com.example.test.ratelimit._01토큰버킷;

import java.util.concurrent.atomic.AtomicInteger;

public class TokenBucketRateLimiter {
    private final int capacity;                // 최대 토큰 수
    private final int refillRate;              // 초당 리필 수
    private AtomicInteger tokens;              // 현재 토큰 수
    private long lastRefillTimestamp;          // 마지막 리필 시간 (ms)

    public TokenBucketRateLimiter(int capacity, int refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.tokens = new AtomicInteger(capacity);
        this.lastRefillTimestamp = System.currentTimeMillis();
    }

    public synchronized boolean tryConsume() {
        refill();

        if (tokens.get() > 0) {
            tokens.decrementAndGet();
            return true;
        }

        return false;
    }

    private void refill() {
        // 현재 시간(밀리초)을 가져옵니다.
        long now = System.currentTimeMillis();

        // 마지막 토큰 리필 시점 이후로 얼마나 시간이 지났는지 계산합니다.
        long elapsed = now - lastRefillTimestamp;

        // 시간이 흐른 경우에만 리필을 수행합니다. (시간이 전혀 안 흘렀다면 리필할 필요 없음)
        if (elapsed > 0) {

            // 경과된 시간(ms)을 기준으로 새로 추가할 토큰 수 계산
            // 예: 2000ms 경과, refillRate = 5라면 → 2초 × 5 = 10개 추가
            int tokensToAdd = (int) (elapsed / 1000.0 * refillRate);

            // 리필할 토큰이 1개 이상일 때만 동작 (0개면 그대로 유지)
            if (tokensToAdd > 0) {

                // 현재 토큰 수 + 추가 토큰 수 계산 → 최대 버킷 용량(capacity) 이상 넘지 않게 제한
                int newTokenCount = Math.min(capacity, tokens.get() + tokensToAdd);

                // 계산된 값으로 현재 토큰 수 업데이트
                tokens.set(newTokenCount);

                // 마지막 리필 시점을 현재로 갱신
                lastRefillTimestamp = now;
            }
        }
    }
}

