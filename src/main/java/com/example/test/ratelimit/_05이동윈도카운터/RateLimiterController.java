package com.example.test.ratelimit._05이동윈도카운터;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RateLimiterController {

    // 📌 Sliding Window Counter (이동 윈도우 카운터) 알고리즘
    // - 고정 윈도우 카운터 알고리즘과 이동윈도 로깅 알고리즘을 결합한 것
    // - 현재 윈도우와 직전 윈도우의 요청 수를 시간 비율로 보간(가중합)하여 계산합니다
    // - 윈도우가 경계에 걸쳐있더라도 부드러운 제한이 가능함

    // ✅ 장점:
    // - 메모리 효율적 (두 개의 카운터만 유지)
    // - 고정 윈도우보다 훨씬 자연스러운 제한이 가능 (버스트 방지에 효과적)

    // ❌ 단점:
    // - 로직이 비교적 복잡
    // - 초/밀리초 단위 시간 계산이 필요하며, 정밀 제어가 필요할 경우 약간의 오차 존재 (40억개중 0.003%라고함)

    private final SlidingWindowCounterRateLimiter rateLimiter =
            new SlidingWindowCounterRateLimiter(10, 1000); // 1초당 10회 허용

    @GetMapping("/sliding-counter")
    public ResponseEntity<String> slidingCounter() {
        boolean allowed = rateLimiter.allowRequest();

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-RateLimit-Limit", "10");
        headers.add("X-RateLimit-Remaining", String.valueOf(rateLimiter.getRemaining()));
        headers.add("Retry-After", String.valueOf(rateLimiter.getRetryAfterSeconds()));

        if (allowed) {
            return ResponseEntity.ok()
                    .headers(headers)
                    .body("✅ 요청 허용됨 (Sliding Counter)");
        } else {
            return ResponseEntity.status(429)
                    .headers(headers)
                    .body("❌ 너무 많은 요청입니다 (Sliding Counter)");
        }
    }
}
