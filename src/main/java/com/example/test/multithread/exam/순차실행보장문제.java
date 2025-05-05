package com.example.test.multithread.exam;

import java.util.concurrent.CountDownLatch;

public class 순차실행보장문제 {
    public static void main(String[] args) {
        // 🔹 문제: A → B → C 순서로 반드시 출력되도록 구현
        //        실행 순서가 바뀌면 실패로 간주

        // 🧠 CountDownLatch: 스레드 간 순서 제어에 사용
        // A가 끝나면 B가 시작 가능하게 하려면 → latchAB.await()
        // B가 끝나면 C가 시작 가능하게 하려면 → latchBC.await()

        // A → B 동기화용 래치 (1개가 countDown되면 B 시작)
        CountDownLatch latchAB = new CountDownLatch(1);

        // B → C 동기화용 래치 (1개가 countDown되면 C 시작)
        CountDownLatch latchBC = new CountDownLatch(1);

        // 🔹 스레드 A: "A"를 출력한 뒤 latchAB.countDown()
        Thread threadA = new Thread(() -> {
            System.out.println("A");         // A 출력
            latchAB.countDown();            // B에게 "나 끝났어" 신호
        });

        // 🔹 스레드 B: A가 끝날 때까지 기다렸다가 "B" 출력
        Thread threadB = new Thread(() -> {
            try {
                latchAB.await();            // A가 끝날 때까지 대기
                System.out.println("B");    // B 출력
                latchBC.countDown();        // C에게 "나 끝났어" 신호
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // 🔹 스레드 C: B가 끝날 때까지 기다렸다가 "C" 출력
        Thread threadC = new Thread(() -> {
            try {
                latchBC.await();            // B가 끝날 때까지 대기
                System.out.println("C");    // C 출력
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // 스레드 시작 순서는 상관없음 (latch가 순서를 보장함)
        threadC.start(); // C는 latchBC.await()에서 대기
        threadB.start(); // B는 latchAB.await()에서 대기
        threadA.start(); // A는 바로 출력 후 countDown()
    }
}
