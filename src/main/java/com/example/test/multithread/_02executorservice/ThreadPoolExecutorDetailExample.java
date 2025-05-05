package com.example.test.multithread._02executorservice;

import java.util.concurrent.*;

public class ThreadPoolExecutorDetailExample {
    public static void main(String[] args) {
        // ThreadPoolExecutor 직접 생성
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2,                       // corePoolSize: 최소 실행 스레드 수
                4,                       // maximumPoolSize: 최대 스레드 수
                10, TimeUnit.SECONDS,   // keepAliveTime: 유휴 스레드 유지 시간
                new LinkedBlockingQueue<>(2), // 작업 큐: 2개까지 대기 가능
                Executors.defaultThreadFactory(), // 기본 스레드 생성 방식
                new ThreadPoolExecutor.AbortPolicy() // 큐도 꽉 차면 예외 던짐
        );

        for (int i = 1; i <= 6; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("작업 " + taskId + " 시작 - " + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("작업 " + taskId + " 완료 - " + Thread.currentThread().getName());
            });
        }

        executor.shutdown();
    }
}

