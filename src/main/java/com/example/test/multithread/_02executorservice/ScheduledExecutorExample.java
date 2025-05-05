package com.example.test.multithread._02executorservice;

import java.util.concurrent.*;

public class ScheduledExecutorExample {
    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable task = () -> System.out.println("주기적 작업: " + System.currentTimeMillis());

        // 1초 후 첫 실행, 2초 간격 반복
        scheduler.scheduleAtFixedRate(task, 1, 2, TimeUnit.SECONDS);
    }
}
