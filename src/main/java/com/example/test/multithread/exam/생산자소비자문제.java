package com.example.test.multithread.exam;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class 생산자소비자문제 {

    public static void main(String[] args) {
        // 🎯 문제 설명:
        // - 생산자 스레드는 데이터를 생성하여 버퍼(큐)에 넣는다.
        // - 소비자 스레드는 버퍼에서 데이터를 꺼내서 처리한다.
        // - 버퍼가 가득 차면 생산자는 대기, 비어 있으면 소비자는 대기해야 한다.
        // - 이 과정을 동기화하여 동시성 문제 없이 구현하라.

        // 🔐 공유 버퍼 역할을 하는 BlockingQueue (스레드 안전 + 자동 대기/해제 기능 제공)
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5); // 버퍼 용량: 5

        // 🧵 생산자 스레드
        Thread producer = new Thread(() -> {
            for (int i = 0; i < 10; i++) { // 총 10개의 데이터를 생산
                try {
                    queue.put(i); // 큐가 가득 차면 자동으로 대기 (blocking)
                    System.out.println("🔵 생산자: " + i + " 생산");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // 인터럽트 발생 시 복구
                }
            }
        });

        // 🧵 소비자 스레드
        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 10; i++) { // 총 10개의 데이터를 소비
                try {
                    int value = queue.take(); // 큐가 비어 있으면 자동으로 대기 (blocking)
                    System.out.println("🟢 소비자: " + value + " 소비");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // 🛠 두 스레드 시작
        producer.start();
        consumer.start();
    }
}

