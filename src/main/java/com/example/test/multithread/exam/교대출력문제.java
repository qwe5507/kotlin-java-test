package com.example.test.multithread.exam;

public class 교대출력문제 {
    // 두 스레드가 공유할 락 객체
    private static final Object lock = new Object();

    // 어떤 스레드의 차례인지 나타내는 플래그 (true면 1번 스레드 차례)
    private static boolean isOneTurn = true;

    public static void main(String[] args) {
        // 스레드1: 숫자 1을 출력
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) { // 10번 출력 반복
                synchronized (lock) { // 하나의 스레드만 lock에 접근 가능
                    while (!isOneTurn) { // 내 차례가 아니면 대기
                        try {
                            lock.wait(); // 기다림 → 다른 스레드가 notify할 때까지
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }

                    // 내 차례이므로 출력
                    System.out.println("1");

                    // 차례를 넘김: 다음은 2번 스레드 차례
                    isOneTurn = false;

                    // 대기 중인 스레드(2번)를 깨움
                    lock.notifyAll();
                }
            }
        });

        // 스레드2: 숫자 2를 출력
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 10; i++) { // 10번 출력 반복
                synchronized (lock) {
                    while (isOneTurn) { // 내 차례가 아니면 대기
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }

                    // 내 차례이므로 출력
                    System.out.println("2");

                    // 다음 차례는 1번 스레드
                    isOneTurn = true;

                    // 다른 스레드를 깨움
                    lock.notifyAll();
                }
            }
        });

        // 두 스레드 시작
        thread1.start();
        thread2.start();
    }
}
