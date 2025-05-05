package com.example.test.동시성._02syncronized;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
public class StockApp {

    // 🔸 공유 자원: 재고 수량
    private int stock = 100;

    // 🔒 동기화된 재고 감소 메서드
    private synchronized void decreaseStock() {
        if (stock > 0) {
            try {
                Thread.sleep(10); // 🔹 지연을 둬서 race condition 유도
            } catch (InterruptedException ignored) {}
            stock -= 1; // 🔹 여러 스레드가 동시에 진입하지 못함
        }
    }

    @PostMapping("/stock/decrease")
    public String decrease() {
        decreaseStock(); // 🔹 내부적으로 synchronized 보호
        return "현재 재고: " + stock;
    }

    @GetMapping("/stock")
    public int getStock() {
        return stock;
    }

    public static void main(String[] args) {
        SpringApplication.run(StockApp.class, args);
    }
}
