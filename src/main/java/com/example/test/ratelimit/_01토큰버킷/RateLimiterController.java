package com.example.test.ratelimit._01토큰버킷;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@RestController
//@RequestMapping("/api")
public class RateLimiterController {
    // 장점
    // 1. 간단한 구현
    // 2. 메모리 사용 측면에서 효율적
    // 3. 짧은 시간에 집중되는 트래픽도 처리 가능하다. 버킷에 남은 토큰이 있기만 하면 요청은 시스템에 전달될것이다.
    // 단점
    // 버킷 크기와 토큰 공급률이라는 두개 인자를 적절하게 튜닝하는게 까다로운일이다.
    private final TokenBucketRateLimiter rateLimiter = new TokenBucketRateLimiter(10, 1); // capacity 10, 5 tokens/sec

    @GetMapping("/request")
    public ResponseEntity<String> request() {
        if (rateLimiter.tryConsume()) {
            return ResponseEntity.ok("✅ 요청 허용됨");
        } else {
            return ResponseEntity.status(429).body("❌ 너무 많은 요청입니다 (Rate Limited)");
        }
    }
}
