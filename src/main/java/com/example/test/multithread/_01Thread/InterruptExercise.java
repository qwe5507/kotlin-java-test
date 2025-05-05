package com.example.test.multithread._01Thread;

/*
✅ 요구사항
- Thread 하나 생성해서 무한 루프 안에서 "🟢 로그 기록 중..."을 1초마다 출력
- Thread.sleep(1000) 사용
- 메인 스레드에서 5초 뒤 interrupt() 호출
- 스레드는 InterruptedException 발생 시 interrupt 상태 복구하고,
  isInterrupted()를 감지해 루프를 빠져나가 종료 메세지 출력

📌 interrupt() 개념 정리
- interrupt()는 스레드에게 "중단해달라"는 요청을 보내는 신호임
- sleep(), wait(), join() 같은 차단 상태일 때는 즉시 InterruptedException을 던짐
- 하지만 JVM은 예외를 던진 동시에 interrupt 상태 플래그를 false로 초기화함
  → "인터럽트 요청 있었다는 표시"가 사라지는 것임

📌 왜 상태를 다시 true로 복구해야 할까?
- 예외를 catch한 이 코드에서만 종료하면 괜찮음 (복구 불필요)
- 하지만 이 스레드가 계속 루프를 돌고 있고,
  종료 여부를 **isInterrupted()** 등으로 확인하는 구조라면
  interrupt 상태를 다시 true로 **복구하지 않으면 종료 조건이 사라짐**
- 따라서 **상위 코드(루프나 호출자)가 중단 요청을 인지할 수 있도록 복구해야 함**
*/

public class InterruptExercise {
    public static void main(String[] args) {
        Thread logger = new Thread(() -> {
            // 🔁 스레드가 중단(interrupted)되지 않은 동안 루프 수행
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    System.out.println("🟢 로그 기록 중...");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("⚠️ InterruptedException 발생");

                    /*
                    ✅ 이 시점에서 interrupt 플래그는 false로 초기화된 상태임
                    왜냐면 JVM이 "예외 던졌으니, 중단 요청은 처리됐지?"라고 간주해서 지워버림

                    ✅ 하지만 이 코드에서 루프가 종료되지 않는다면,
                    위쪽 루프 조건인 isInterrupted()는 계속 false로 평가돼서
                    무한 루프에 빠질 수 있음

                    ✅ 따라서 "중단 요청이 있었어"라는 사실을 다시 표시하기 위해
                    현재 스레드의 interrupt 상태를 true로 복구하는 것임
                    */
                    Thread.currentThread().interrupt(); // 🔄 상태 복구!
                }
            }

            // 루프 종료 후 메세지 출력
            System.out.println("🟥 안전하게 루프 종료됨");
        });

        logger.start();

        try {
            Thread.sleep(5000); // 메인 스레드가 5초 대기
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 메인 스레드도 복구 습관화
        }

        // 👇 logger 스레드에게 중단 요청 (interrupt 신호 전달)
        logger.interrupt();
    }
}


