package com.example.test.동시성._01동시성x;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
@RestController
public class StockApp {

    // 🔴 공유 자원: 재고 수량 (초기값: 100)
    private int stock = 100;

    @PostMapping("/stock/decrease")
    public String decrease() {
        if (stock > 0) {
            try {
                Thread.sleep(10); // 🔸 동시성 문제 유발용 지연
            } catch (InterruptedException ignored) {}
            stock -= 1; // 🔥 동시성 문제 발생 지점
            return "재고 감소됨. 현재 재고: " + stock;
        } else {
            return "재고 부족";
        }
    }

    @GetMapping("/stock")
    public int getStock() {
        return stock;
    }

    public static void main(String[] args) {
        SpringApplication.run(StockApp.class, args);
    }
}
