package com.example.test.multithread.exam;

public class 공유자원누적문제 {
    private static int count = 0;

    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object(); // 임계영역 보호용 객체
        Thread[] threads = new Thread[100];

        for (int i = 0; i < 100; i++) {
            threads[i] = new Thread(() -> {
                synchronized (lock) { // 하나의 스레드만 접근 가능
                    count++; // 동시 증가 방지
                }
            });
            threads[i].start();
        }

        // 모든 스레드가 끝날 때까지 대기
        for (Thread t : threads) t.join();

        System.out.println("최종 count 값: " + count); // 항상 100이어야 함
    }
}

