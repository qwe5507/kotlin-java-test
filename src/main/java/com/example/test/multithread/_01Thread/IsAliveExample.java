package com.example.test.multithread._01Thread;

public class IsAliveExample {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> {
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
        });

        t.start();
        System.out.println("⏳ 실행 중? " + t.isAlive()); // true 예상
        t.join(); // 끝날 때까지 대기
        System.out.println("✅ 종료됨? " + t.isAlive()); // false 예상
    }
}
