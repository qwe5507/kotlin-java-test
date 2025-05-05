package com.example.test.ratelimit._03고정윈도카운터;

public class FixedWindowRateLimiter {

    private final int maxRequests;       // 허용할 최대 요청 수
    private final long windowSizeMillis; // 윈도우 시간 (단위: 밀리초)

    private int requestCount = 0;        // 현재 윈도우 내 요청 횟수
    private long windowStart;            // 현재 윈도우의 시작 시각

    public FixedWindowRateLimiter(int maxRequests, long windowSizeMillis) {
        this.maxRequests = maxRequests;
        this.windowSizeMillis = windowSizeMillis;
        this.windowStart = System.currentTimeMillis(); // 윈도우 시작 시간 초기화
    }

    /**
     * 현재 요청이 윈도우 범위 내에서 허용 가능한지 판단
     */
    public synchronized boolean allowRequest() {
        long now = System.currentTimeMillis(); // 현재 시간 가져오기

        // 현재 시간이 윈도우 범위 내에 있을 경우
        if (now - windowStart < windowSizeMillis) {
            if (requestCount < maxRequests) {
                requestCount++; // 허용 → 요청 수 증가
                return true;
            } else {
                return false;   // 한도 초과 → 거부
            }
        } else {
            // 새로운 윈도우로 전환 (기존 윈도우 만료됨)
            windowStart = now;
            requestCount = 1; // 첫 요청 처리
            return true;
        }
    }
}
