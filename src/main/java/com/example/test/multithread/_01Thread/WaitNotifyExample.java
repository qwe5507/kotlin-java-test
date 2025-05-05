package com.example.test.multithread._01Thread;

public class WaitNotifyExample {
    static final Object lock = new Object(); // 공통 객체: synchronized 및 wait/notify 대상

    public static void main(String[] args) {
        // A: wait()으로 대기
        Thread threadA = new Thread(() -> {
            synchronized (lock) { // 락을 획득한 상태여야 wait 가능
                try {
                    System.out.println("🔵 A: 대기 시작");
                    lock.wait(); // 락을 놓고 대기 상태로 들어감
                    System.out.println("🔵 A: 다시 실행됨");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // B: notify()로 A를 깨움
        Thread threadB = new Thread(() -> {
            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
            synchronized (lock) {
                System.out.println("🔴 B: A를 깨움 (notify)");
                lock.notify(); // wait 중인 스레드 중 하나 깨움
            }
        });

        threadA.start(); // A 먼저 실행 → wait 상태
        threadB.start(); // B가 A를 1초 후 깨움
    }
}

