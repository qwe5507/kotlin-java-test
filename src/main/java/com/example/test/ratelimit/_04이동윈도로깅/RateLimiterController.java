package com.example.test.ratelimit._04이동윈도로깅;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@RestController
//@RequestMapping("/api")
public class RateLimiterController {

    // 📌 이동 윈도우 로깅 (Sliding Window Log) 알고리즘
    // - 요청 시간(timestamp)을 모두 저장하고, 최근 N초 이내의 요청 수만 계산하여 제한
    // - 예: 1초 안에 10개 이하만 허용 → 요청 시마다 log에서 1초 초과된 항목은 제거하고 size 확인

    // ✅ 장점
    // - 실제 발생한 요청만 반영 → 정확도 가장 높음
    // - 경계 문제 없음 → 버스트도 정확히 제한 가능

    // ❌ 단점
    // - 요청 하나당 로그 저장 필요 → 메모리 사용량이 요청 수에 비례
    // - 요청이 거부되도 저장하기때문에 메모리사용량 높다
    // - TPS가 높을수록 GC 부담 및 성능 저하 가능성 있음

    private final SlidingWindowLogRateLimiter rateLimiter = new SlidingWindowLogRateLimiter(10, 1000); // 1초간 10개 허용

    @GetMapping("/sliding-log")
    public ResponseEntity<String> request() {
        if (rateLimiter.allowRequest()) {
            return ResponseEntity.ok("✅ 요청 허용됨 (Sliding Log)");
        } else {
            return ResponseEntity.status(429).body("❌ 너무 많은 요청입니다 (Sliding Log)");
        }
    }
}

