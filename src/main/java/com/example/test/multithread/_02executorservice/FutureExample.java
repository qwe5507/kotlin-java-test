package com.example.test.multithread._02executorservice;

import java.util.concurrent.*;

public class FutureExample {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Future<String> future = executor.submit(() -> {
            Thread.sleep(1000);
            return "작업 결과";
        });

        System.out.println("결과 기다리는 중...");
        String result = future.get(); // 결과가 나올 때까지 대기
        System.out.println("결과: " + result);

        executor.shutdown();
    }
}

