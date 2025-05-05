package com.example.test.multithread.completableFuture;

import java.util.concurrent.*;
import java.util.*;

public class CompletableFutureAllExample {

    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        CompletableFuture<String> api1 = CompletableFuture.supplyAsync(() -> callApi("API-1", 1000), executor);
        CompletableFuture<String> api2 = CompletableFuture.supplyAsync(() -> callApi("API-2", 800), executor);
        CompletableFuture<String> api3 = CompletableFuture.supplyAsync(() -> callApi("API-3", 1200), executor);

        // 모든 작업 완료까지 기다림
        CompletableFuture<Void> all = CompletableFuture.allOf(api1, api2, api3);

        // 결과 합산 후 평균 계산
        CompletableFuture<String> result = all.thenApply(v -> {
            try {
                int len1 = api1.get().length();
                int len2 = api2.get().length();
                int len3 = api3.get().length();
                int avg = (len1 + len2 + len3) / 3;
                return "평균 응답 길이: " + avg;
            } catch (Exception e) {
                throw new RuntimeException("응답 처리 실패", e);
            }
        }).exceptionally(e -> "Fallback: 처리 실패 - " + e.getMessage());

        System.out.println(result.get());
        executor.shutdown();
    }

    public static String callApi(String name, int delayMillis) {
        try {
            Thread.sleep(delayMillis);
            if (new Random().nextInt(5) < 2) {
                throw new RuntimeException(name + " 실패");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return name + " 응답";
    }
}

