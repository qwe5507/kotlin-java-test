package com.example.test.multithread.exam;

/**
 * ✅ 사용자 ID를 스레드별로 안전하게 저장하고 꺼내는 클래스
 * ThreadLocal을 사용해 스레드 간 데이터 격리를 구현함
 */
public class UserContext {
    // ThreadLocal 선언: 각 스레드마다 별도로 값을 저장할 수 있음
    private static final ThreadLocal<String> userIdHolder = new ThreadLocal<>();

    public static void set(String userId) {
        userIdHolder.set(userId); // 현재 스레드에만 userId 저장
    }

    public static String get() {
        return userIdHolder.get(); // 현재 스레드의 userId 반환
    }

    public static void clear() {
        userIdHolder.remove(); // 메모리 누수 방지 (명시적으로 제거)
    }
}
