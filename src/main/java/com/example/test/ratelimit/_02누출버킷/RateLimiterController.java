package com.example.test.ratelimit._02누출버킷;

import com.example.test.ratelimit._01토큰버킷.TokenBucketRateLimiter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
//@RequestMapping("/api")
public class RateLimiterController {
    // Leaky Bucket 알고리즘
    // ⛲ 일정 간격마다 요청을 1개씩 처리 (버킷에서 물이 일정하게 새듯이)
    // 📥 요청은 큐에 저장되며, 큐가 가득 차면 더 이상 받지 않고 거절함 (429 응답)
    // ✅ 장점: 네트워크나 시스템 자원을 일정하게 보호할 수 있음
    // ❌ 단점: 단기간에 요청이 많을 경우 대기 시간이 길어지거나 바로 거절될 수 있음
    private final LeakyBucketRateLimiter rateLimiter = new LeakyBucketRateLimiter(10, 1); // capacity 10, 5 tokens/sec

    @GetMapping("/leaky")
    public ResponseEntity<String> request() {
        if (rateLimiter.tryAddRequest()) {
            return ResponseEntity.ok("✅ 요청 허용됨 (Leaky)");
        } else {
            return ResponseEntity.status(429).body("❌ 너무 많은 요청입니다 (Leaky)");
        }
    }
}
