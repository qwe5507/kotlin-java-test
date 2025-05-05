package com.example.test.multithread._01Thread;

public class InterruptExample {
    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            try {
                System.out.println("🛏️ 잠자기 시작");
                Thread.sleep(3000); // 3초간 수면
                System.out.println("🛏️ 정상 종료");
            } catch (InterruptedException e) {
                System.out.println("⚠️ 인터럽트 발생! 수면 중단");
            }
        });

        t.start();
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}

        t.interrupt(); // 수면 중인 스레드 강제로 깨움 (InterruptedException 발생)
    }
}
