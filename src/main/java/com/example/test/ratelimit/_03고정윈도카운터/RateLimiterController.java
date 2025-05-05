package com.example.test.ratelimit._03고정윈도카운터;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@RestController
//@RequestMapping("/api")
public class RateLimiterController {

    // 🧠 고정 윈도우 카운터 (Fixed Window Counter)
    // - 요청을 일정 시간 단위(window)로 나누고, 각 구간 내에서 요청 수를 카운팅하여 제한합니다.
    // - 예: 1초에 최대 5번 허용 → 1초 지나면 카운터 초기화

    // ✅ 장점:
    // - 구조가 간단하고 구현이 쉬움
    // - 메모리 효율이 좋다
    // - 윈도가 닫히는 시점에 카운터를 초기화하는 방식은 특정한 트래픽패턴을 처리하기에 적합하다.
    // ❌ 단점:
    // - 경계 구간(예: 0.99초와 1.01초)에 몰린 요청은 실제보다 많이 허용되는 "버스트" 발생 가능

    private final FixedWindowRateLimiter rateLimiter = new FixedWindowRateLimiter(5, 1000); // 1초에 5회까지 허용

    @GetMapping("/fixed")
    public ResponseEntity<String> request() {
        if (rateLimiter.allowRequest()) {
            return ResponseEntity.ok("✅ 요청 허용됨 (Fixed Window)");
        } else {
            return ResponseEntity.status(429).body("❌ 너무 많은 요청입니다 (Fixed Window)");
        }
    }
}

