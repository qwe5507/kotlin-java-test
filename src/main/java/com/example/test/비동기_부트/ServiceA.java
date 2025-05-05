package com.example.test.비동기_부트;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class ServiceA {

    @Async("poolA") // 👈 poolA 지정
    public CompletableFuture<String> execute() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return CompletableFuture.completedFuture("✅ A 작업 완료 (" + Thread.currentThread().getName() + ")");
    }
}

