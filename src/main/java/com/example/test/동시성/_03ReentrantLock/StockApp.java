package com.example.test.동시성._03ReentrantLock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.locks.ReentrantLock;

@SpringBootApplication
@RestController
public class StockApp {

    private int stock = 100;

    // 🔒 ReentrantLock: 명시적으로 락을 제어할 수 있는 객체
    // ✅ "Reentrant"란 같은 스레드가 여러 번 lock() 해도 deadlock이 발생하지 않음을 의미
    private final ReentrantLock lock = new ReentrantLock();

    @PostMapping("/stock/decrease")
    public String decrease() {
        // 🔐 lock() 호출 시 다른 스레드가 이 블록에 진입할 수 없음 (블로킹됨)
        lock.lock();
        try {
            if (stock > 0) {
                Thread.sleep(10); // 🔸 동시성 문제를 유도하기 위한 지연
                stock -= 1; // ✅ 이 영역은 lock에 의해 한 번에 하나의 스레드만 접근 가능
            }
            return "현재 재고: " + stock;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // ✅ 인터럽트 전파
            return "실패: 인터럽트됨";
        } finally {
            lock.unlock(); // 🔓 반드시 finally에서 unlock() 해야 데드락 방지 가능
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
