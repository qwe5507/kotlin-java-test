package com.example.test.ratelimit._02누출버킷;

import java.util.LinkedList;
import java.util.Queue;

public class LeakyBucketRateLimiter {

    // 버킷(큐)의 최대 크기 (허용 가능한 최대 동시 요청 수)
    private final int capacity;

    // 요청을 하나 처리하는 데 걸리는 시간 (누수 주기), 단위: 밀리초
    private final long leakIntervalMillis;

    // 요청을 저장하는 큐 → FIFO 구조
    private final Queue<Long> queue = new LinkedList<>();

    // 마지막으로 요청이 누수(처리)된 시각
    private long lastLeakTime;

    // 생성자: 버킷 용량과 누수 간격을 초기화
    public LeakyBucketRateLimiter(int capacity, long leakIntervalMillis) {
        this.capacity = capacity;
        this.leakIntervalMillis = leakIntervalMillis;
        this.lastLeakTime = System.currentTimeMillis(); // 초기화 시점 저장
    }

    /**
     * 요청을 시도하고, 버킷에 공간이 있으면 저장 후 true 반환.
     * 공간이 없으면 false 반환 (rate limit 발생)
     */
    public synchronized boolean tryAddRequest() {
        leak(); // 누수 먼저 수행 (낡은 요청 제거)

        // 큐에 자리가 남아 있다면 현재 요청을 큐에 저장
        if (queue.size() < capacity) {
            queue.add(System.currentTimeMillis()); // 현재 요청 시간 저장
            return true; // 요청 허용
        }

        // 큐가 가득 찼다면 요청 거절
        return false;
    }

    /**
     * 일정 시간이 지난 요청들을 큐에서 제거하는 함수 (누수 동작)
     */
    private void leak() {
        long now = System.currentTimeMillis(); // 현재 시간

        // 누수 간격만큼 시간이 지났다면 큐에서 오래된 요청 제거
        while (!queue.isEmpty() && (now - lastLeakTime >= leakIntervalMillis)) {
            queue.poll(); // 가장 오래된 요청 제거 (하나씩만)
            lastLeakTime += leakIntervalMillis; // 다음 기준 시간 업데이트
        }
    }
}

