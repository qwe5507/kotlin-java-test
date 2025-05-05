package com.example.test.circuit_breaker._01직접구현;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class CircuitBreakerController {

    private final CircuitBreaker breaker = new CircuitBreaker();

    @GetMapping("/circuit")
    public ResponseEntity<String> circuit() {
        if (!breaker.allowRequest()) {
            return ResponseEntity.status(503).body("⛔ 요청 차단됨 (서킷 오픈 상태)");
        }

        try {
            // 실제 비즈니스 로직 또는 외부 API 호출
            if (new Random().nextInt(10) < 4) {
                throw new RuntimeException("외부 시스템 실패!");
            }

            breaker.recordSuccess();
            return ResponseEntity.ok("✅ 정상 응답");
        } catch (Exception e) {
            breaker.recordFailure();
            return ResponseEntity.status(500).body("❌ 시스템 실패");
        }
    }
}

