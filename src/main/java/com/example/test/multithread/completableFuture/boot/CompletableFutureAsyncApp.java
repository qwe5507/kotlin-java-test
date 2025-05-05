package com.example.test.multithread.completableFuture.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

@SpringBootApplication
@EnableAsync // @Async 비동기 메서드 사용을 위한 설정
public class CompletableFutureAsyncApp {
    public static void main(String[] args) {
        SpringApplication.run(CompletableFutureAsyncApp.class, args);
    }

    // 비동기 작업을 실행할 스레드풀 정의
    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor exec = new ThreadPoolTaskExecutor();
        exec.setCorePoolSize(3); // 동시에 실행 가능한 최소 스레드 수
        exec.setMaxPoolSize(3);  // 동시에 실행 가능한 최대 스레드 수
        exec.setQueueCapacity(10); // 대기 큐 크기 (초과 시 거절 정책 적용)
        exec.setThreadNamePrefix("async-task-"); // 스레드 이름 접두어
        exec.initialize();
        return exec;
    }
}


