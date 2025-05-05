package com.example.test.multithread._01Thread;

// 2. Runnable 인터페이스 구현하여 사용
class MyTask implements Runnable {
    private String name;
    public MyTask(String name) {
        this.name = name;
    }
    @Override
    public void run() {
        // 멀티스레드로 수행할 작업 내용
        System.out.println(name + " 실행: " + Thread.currentThread().getName());
        // 예: 작업 수행 (여기서는 이름 출력)
    }
}

public class RunnableExample {
    public static void main(String[] args) {
        Runnable task1 = new MyTask("작업1");
        Runnable task2 = new MyTask("작업2");

        Thread thread1 = new Thread(task1);       // Runnable을 담아 Thread 생성
        Thread thread2 = new Thread(task2, "T2"); // 두번째 인자로 이름 지정 가능
        thread1.start();
        thread2.start();
        // 람다 표현식을 사용한 Runnable 예시
        Thread thread = new Thread(() -> {
            System.out.println("람다 Runnable 실행: " + Thread.currentThread().getName());
        });
        thread.start();

    }

}
