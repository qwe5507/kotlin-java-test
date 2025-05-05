package com.example.test.circuit_breaker._01직접구현;

public class CircuitBreaker {

    // 서킷 브레이커의 3가지 상태 정의: 닫힘, 열림, 반쯤 열림
    enum State { CLOSED, OPEN, HALF_OPEN }

    // 현재 상태: 기본은 CLOSED 상태
    private State state = State.CLOSED;

    // 연속 실패 횟수 카운트
    private int failureCount = 0;

    // 실패 허용 임계값 (넘으면 OPEN 상태로 전환)
    private final int failureThreshold = 3;

    // OPEN 상태일 때 얼마나 기다릴 것인가 (ms 기준)
    private final long openTimeout = 5000; // 5초

    // 마지막 실패 발생 시간 기록 (OPEN 상태 지속 시간 판단용)
    private long lastFailureTime = 0;

    /**
     * 요청을 허용할 수 있는 상태인지 확인하는 메서드
     * - OPEN 상태일 경우, timeout이 지나야 HALF_OPEN으로 전이
     * - CLOSED or HALF_OPEN 상태에서는 요청 허용
     */
    public synchronized boolean allowRequest() {
        if (state == State.OPEN) {
            // OPEN 상태 유지 시간이 지났으면 HALF_OPEN으로 상태 전환
            if (System.currentTimeMillis() - lastFailureTime > openTimeout) {
                state = State.HALF_OPEN;
                return true;
            }
            // 아직 timeout이 안 지났다면 요청 차단
            return false;
        }
        // CLOSED 또는 HALF_OPEN이면 요청 허용
        return true;
    }

    /**
     * 요청 성공 시 호출
     * - HALF_OPEN 상태였다면, 성공했으므로 다시 CLOSED 상태로 전환
     * - 실패 카운트 초기화
     */
    public synchronized void recordSuccess() {
        if (state == State.HALF_OPEN) {
            state = State.CLOSED; // 정상 회복 판단
        }
        failureCount = 0; // 실패 카운트 초기화
    }

    /**
     * 요청 실패 시 호출
     * - 실패 카운트 증가
     * - 실패 카운트가 임계값을 초과하면 OPEN 상태로 전환
     */
    public synchronized void recordFailure() {
        failureCount++;
        lastFailureTime = System.currentTimeMillis(); // 실패 시간 기록
        if (failureCount >= failureThreshold) {
            state = State.OPEN; // OPEN 상태로 전환 (일시적으로 차단)
        }
    }
}

