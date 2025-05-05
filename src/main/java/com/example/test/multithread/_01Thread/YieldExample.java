package com.example.test.multithread._01Thread;

public class YieldExample {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                System.out.println("🔵 A: " + i);
                Thread.yield(); // CPU 사용권을 다른 스레드에게 양보
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                System.out.println("🔴 B: " + i);
            }
        });

        t1.start();
        t2.start();
    }
}
