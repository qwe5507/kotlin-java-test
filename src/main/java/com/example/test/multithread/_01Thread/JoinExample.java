package com.example.test.multithread._01Thread;

public class JoinExample {
    public static void main(String[] args) throws InterruptedException {
        Thread worker = new Thread(() -> {
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            System.out.println("🟢 작업 스레드 완료");
        });

        worker.start(); // 작업 스레드 시작
        worker.join();  // worker가 끝날 때까지 main 스레드 대기

        System.out.println("🟢 메인 종료"); // 항상 worker 이후 실행됨
    }
}

