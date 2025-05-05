package com.example.test.동시성._04CAS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
@RestController
public class StockApp {

    // 🔴 공유 자원: 재고 수량 (초기값: 100)
    // ✅ AtomicInteger: 내부적으로 Compare-And-Swap(CAS) 방식으로 구현된 클래스
    // ✅ CAS는 기대한 값과 현재 값이 같을 경우에만 값을 원자적으로 변경함
    private final AtomicInteger stock = new AtomicInteger(100);

    @PostMapping("/stock/decrease")
    public String decrease() {
        while (true) {
            // 1. 현재 재고 값 읽기 (원자적 아님 – 다른 스레드가 바꿀 수 있음)
            int current = stock.get();

            // 2. 재고 부족 시 바로 리턴
            if (current <= 0) {
                return "재고 부족";
            }

            try {
                // ❗ 일부러 약간의 지연을 줘서 경쟁 상황 유도
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "인터럽트 발생";
            }

            // 3. compareAndSet(expected, updated)
            //    → 현재 값이 내가 읽은 current와 같으면 updated로 변경
            //    → 이 과정은 **JVM 내부에서 atomic하게 수행됨**
            boolean updated = stock.compareAndSet(current, current - 1);

            if (updated) {
                // ✅ 감소 성공한 경우 → 재고 1개 줄고, 요청 처리 성공
                return "재고 감소됨. 현재 재고: " + (current - 1);
            }

            // ❌ 실패한 경우 (다른 스레드가 이미 값을 바꿨음)
            //    → 다시 get()해서 최신 값으로 재시도해야 함
            //    → 이 구조 덕분에 결국 모든 요청은 정확히 1번씩만 감소됨

            try {
                Thread.sleep(1); // ✅ busy wait 방지용 백오프
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "인터럽트 발생";
            }
        }
    }

    @GetMapping("/stock")
    public int getStock() {
        return stock.get();
    }

    public static void main(String[] args) {
        SpringApplication.run(StockApp.class, args);
    }
}

