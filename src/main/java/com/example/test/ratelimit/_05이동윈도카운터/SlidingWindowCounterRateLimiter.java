package com.example.test.ratelimit._05이동윈도카운터;

public class SlidingWindowCounterRateLimiter {

    private final int maxRequests; // 허용 요청 수
    private final long windowSizeMillis; // 윈도우 크기 (보통 1000ms = 1초)

    private long currentWindow;   // 현재 윈도우 시간 (초 단위)
    private int currentCount = 0; // 현재 윈도우 요청 수
    private int prevCount = 0;    // 직전 윈도우 요청 수


    private double lastTotalRequests = 0;  // 마지막 요청 기준 총 요청 수 (헤더 계산용)
    private long lastRequestTime = 0;      // 마지막 요청 시간 (헤더 계산용)

    public SlidingWindowCounterRateLimiter(int maxRequests, long windowSizeMillis) {
        this.maxRequests = maxRequests;
        this.windowSizeMillis = windowSizeMillis;
        this.currentWindow = getWindow(System.currentTimeMillis());
    }

    public synchronized boolean allowRequest() {
        long now = System.currentTimeMillis(); // ① 현재 시간 (ms)

        long window = getWindow(now);  // ② 지금 몇 번째 윈도우인지 (초 단위)

        // ③ 현재 윈도우인지?
        if (window == currentWindow) {
            currentCount++; // 같은 윈도우니까 현재 카운트 증가
        }

        // ④ 윈도우가 1칸 넘어갔으면
        else if (window == currentWindow + 1) {
            prevCount = currentCount; // 현재 걸 이전 카운터로 옮김
            currentCount = 1;         // 현재는 새로운 윈도우니까 1로 시작
            currentWindow = window;   // 윈도우 갱신
        }

        // ⑤ 윈도우가 2칸 이상 건너뛰었으면 (시간 많이 지남)
        else {
            prevCount = 0;           // 이전 것도 필요 없음
            currentCount = 1;        // 새 윈도우 시작
            currentWindow = window;
        }

        // ⑥ 현재 시간의 윈도우 진행 비율 (예: 300ms / 1000ms = 0.3)
        double elapsedRatio = (now % windowSizeMillis) / (double) windowSizeMillis;

        // ⑦ 시간 비율로 요청 수 보간 = 이전 * (남은 시간 비율) + 현재
        double totalRequests = prevCount * (1 - elapsedRatio) + currentCount;


        // 헤더 정보 생성을 위해 저장
        lastTotalRequests = totalRequests;
        lastRequestTime = now;

        // ⑧ 총 요청 수가 한도 이하면 true
        return totalRequests <= maxRequests;
    }


    // 초 단위 윈도우를 구하는 함수
    private long getWindow(long timestamp) {
        return timestamp / windowSizeMillis;
    }


    // 🟢 남은 요청 수 계산 (0보다 작으면 0 반환)
    public int getRemaining() {
        return Math.max(0, maxRequests - (int)Math.ceil(lastTotalRequests));
    }

    // ⏱ 다시 요청할 수 있는 시간 (초 단위, 정수 반올림)
    public int getRetryAfterSeconds() {
        if (lastTotalRequests <= maxRequests) {
            return 0; // 요청 가능 상태
        }

        long elapsed = lastRequestTime % windowSizeMillis;
        long remainingMillis = windowSizeMillis - elapsed;
        return (int) Math.ceil(remainingMillis / 1000.0);
    }
}

