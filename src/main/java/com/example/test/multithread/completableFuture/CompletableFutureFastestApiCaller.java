package com.example.test.multithread.completableFuture;

import java.util.concurrent.*;
import java.util.*;

public class CompletableFutureFastestApiCaller {

    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // 3개의 API를 비동기로 호출
        CompletableFuture<String> apiA = CompletableFuture.supplyAsync(() -> callApi("API-A", 1200), executor);
        CompletableFuture<String> apiB = CompletableFuture.supplyAsync(() -> callApi("API-B", 800), executor);
        CompletableFuture<String> apiC = CompletableFuture.supplyAsync(() -> callApi("API-C", 1000), executor);

        // 가장 먼저 완료된 결과만 사용
        CompletableFuture<Object> fastest = CompletableFuture.anyOf(apiA, apiB, apiC)
                .exceptionally(ex -> "Fallback: 모든 API 실패");

        // 결과 반환
        System.out.println("가장 빠른 응답: " + fastest.get());

        executor.shutdown();
    }

    // API 호출 시뮬레이션
    public static String callApi(String apiName, int delayMillis) {
        try {
            Thread.sleep(delayMillis);
            if (new Random().nextBoolean()) {
                throw new RuntimeException(apiName + " 실패"); // 실패 확률 50%
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return apiName + " 응답 완료";
    }
}
