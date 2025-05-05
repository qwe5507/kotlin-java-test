package com.example.test.동시성._05ConcurrentHashMap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
@RestController
public class UserLockApp {

    // ✅ 사용자 ID → 처리 중 여부 플래그
    private final Map<String, Boolean> inProgress = new ConcurrentHashMap<>();

    @PostMapping("/process/{userId}")
    public String process(@PathVariable String userId) {
        // 1. putIfAbsent → 이전 값이 null이면 처리 시작, 그렇지 않으면 중복
        Boolean previous = inProgress.putIfAbsent(userId, true);

        if (previous != null) {
            return "이미 처리 중입니다. 중복 요청 차단됨.";
        }

        try {
            // ✅ 실제 처리 로직 (예: 포인트 차감, DB 저장 등)
            Thread.sleep(1000); // 처리 시간 시뮬레이션
            return "처리 완료";
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "처리 실패 (인터럽트)";
        } finally {
            // 2. 처리 완료 후 플래그 제거
            inProgress.remove(userId);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(UserLockApp.class, args);
    }
}
