package com.example.test.multithread.completableFuture;

import java.util.concurrent.*;

public class CompletableFutureDetailExample {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        CompletableFuture<String> call1 = CompletableFuture.supplyAsync(() -> {
            delay(1000);
            return "응답1";
        }, executor);

        CompletableFuture<String> call2 = CompletableFuture.supplyAsync(() -> {
            delay(1500);
            return "응답2";
        }, executor);

        // 두 응답 병렬로 받고 조합
        CompletableFuture<String> combined = call1.thenCombine(call2, (res1, res2) -> res1 + " + " + res2);

        // 완료 후 후속 처리
        combined.thenAccept(result -> System.out.println("최종 결과: " + result))
                .exceptionally(e -> {
                    System.err.println("에러 발생: " + e.getMessage());
                    return null;
                });

        // 메인 스레드 대기
        combined.get();
        executor.shutdown();
    }

    private static void delay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {}
    }
}


