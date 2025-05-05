package com.example.test.ratelimit._04이동윈도로깅;

import java.util.LinkedList;
import java.util.Queue;

public class SlidingWindowLogRateLimiter {

    private final int maxRequests;         // 허용 요청 수
    private final long windowSizeMillis;   // 윈도우 크기 (예: 1000ms = 1초)

    // 요청 시간 로그를 저장하는 큐 (FIFO)
    private final Queue<Long> requestLog = new LinkedList<>();

    public SlidingWindowLogRateLimiter(int maxRequests, long windowSizeMillis) {
        this.maxRequests = maxRequests;
        this.windowSizeMillis = windowSizeMillis;
    }

    /**
     * 현재 요청이 윈도우 내에서 허용 가능한지 검사
     */
    public synchronized boolean allowRequest() {
        long now = System.currentTimeMillis(); // 현재 시간

        // 🔁 오래된 요청 로그 제거 (윈도우 바깥으로 나간 것)
        while (!requestLog.isEmpty() && now - requestLog.peek() > windowSizeMillis) {
            requestLog.poll(); // 맨 앞 로그 제거
        }

        requestLog.add(now); // ✅ 요청이 허용되든 안 되든 기록은 남긴다

        if (requestLog.size() <= maxRequests) {
            return true; // 허용
        } else {
            return false; // 거부
        }
    }
}

