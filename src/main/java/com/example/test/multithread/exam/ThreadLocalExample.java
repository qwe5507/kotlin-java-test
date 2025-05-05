package com.example.test.multithread.exam;

public class ThreadLocalExample {
    public static void main(String[] args) {
        Runnable task = () -> {
            String threadName = Thread.currentThread().getName();

            // 👤 스레드 이름에 따라 사용자 ID 세팅
            if (threadName.equals("Thread-0")) {
                UserContext.set("userA");
            } else if (threadName.equals("Thread-1")) {
                UserContext.set("userB");
            } else {
                UserContext.set("userC");
            }

            // ✅ 현재 스레드에서 저장한 userId를 꺼내서 출력
            System.out.println("[" + threadName + "] userId: " + UserContext.get());

            // ✅ 작업 후에는 꼭 ThreadLocal 값 제거
            UserContext.clear();
        };

        // 🔁 3개의 스레드 생성 및 실행
        for (int i = 0; i < 3; i++) {
            new Thread(task).start();
        }
    }
}

