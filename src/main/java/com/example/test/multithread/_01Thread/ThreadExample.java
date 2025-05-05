package com.example.test.multithread._01Thread;

// 1. Thread 클래스를 상속하여 사용
class MyThread extends Thread {
    @Override
    public void run() {
        // 새로운 스레드에서 실행할 코드
        System.out.println("Thread 실행: " + Thread.currentThread().getName());
        // 예: 작업 수행 (여기서는 단순 출력)
    }
}

public class ThreadExample {
    public static void main(String[] args) {
        Thread t = new MyThread();   // 스레드 객체 생성
        t.start();  // start() 호출 시 별도 스레드에서 run() 실행
    }
}