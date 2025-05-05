package com.example.test.이벤트.blockingqueueevent;

// 🧪 3. 테스트 실행
public class BlockingQueueEventMain {
    public static void main(String[] args) throws InterruptedException {
        EventDispatcher dispatcher = new EventDispatcher();
        dispatcher.start(); // 소비자 스레드 시작

        // 이벤트 발행
        dispatcher.publish(new CustomEvent("회원가입 완료"));
        dispatcher.publish(new CustomEvent("결제 성공"));
        dispatcher.publish(new CustomEvent("알림 전송"));

        // 메인 스레드 대기
        Thread.sleep(1000);
    }
}
