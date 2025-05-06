package com.example.test.practice.practice03;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final SimpleCircuitBreaker circuitBreaker = new SimpleCircuitBreaker(3, 5000); // 3회 실패 → 5초 차단

    public String pay() {
        return circuitBreaker.call(
                this::callExternalSystem,
                () -> "[Fallback] 결제 시스템 점검 중"
        );
    }

    private String callExternalSystem() {
        if (Math.random() < 0.7) {
            throw new RuntimeException("외부 시스템 오류");
        }
        return "결제 성공!";
    }
}

