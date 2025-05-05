package com.example.test.multithread;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchWorkerExample {

    public static void main(String[] args) throws InterruptedException {
        int numberOfTasks = 5;
        CountDownLatch latch = new CountDownLatch(numberOfTasks); // 5개의 작업이 끝나기를 기다림

        for (int i = 1; i <= numberOfTasks; i++) {
            int taskId = i; // 람다 캡처를 위한 final 변수
            new Thread(() -> {
                try {
                    System.out.println("🛠 작업 " + taskId + " 시작");
                    Thread.sleep((long) (Math.random() * 2000)); // 작업 수행 (랜덤 지연)
                    System.out.println("✅ 작업 " + taskId + " 완료");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown(); // 작업 하나 완료되었음을 알림
                }
            }).start();
        }

        // 🔒 모든 작업이 끝날 때까지 대기
        latch.await();

        // 🔓 모든 작업 완료 후 실행
        System.out.println("🎉 모든 작업 완료! 메인 처리 시작");
    }
}
