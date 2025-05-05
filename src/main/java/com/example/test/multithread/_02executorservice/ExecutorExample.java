package com.example.test.multithread._02executorservice;

// 3. ExecutorService (스레드풀) 사용 예시
import java.util.concurrent.*;

public class ExecutorExample {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3); // 스레드 3개 풀
        try {
            for (int i = 1; i <= 5; i++) {
                int taskId = i;
                executor.execute(() -> {
                    // 스레드 풀의 한 스레드에서 실행될 작업 (Runnable)
                    String threadName = Thread.currentThread().getName();
                    System.out.println("Task " + taskId + " running on " + threadName);
                    // 예: 실제 작업 처리 로직 수행
                });
            }
        } finally {
            executor.shutdown(); // 작업 제출 완료 후 스레드 풀 종료 처리
        }
    }
}
