package com.example.test.multithread.completableFuture.boot;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api")
public class AggregationController {

    private final ApiAggregationService service;

    public AggregationController(ApiAggregationService service) {
        this.service = service; // final 필드 초기화
    }

    // GET /api/aggregate 요청 처리
    @GetMapping("/aggregate")
    public CompletableFuture<String> getAggregate() {
        // 세 개의 외부 API를 비동기로 병렬 호출
        CompletableFuture<String> a = service.getA();
        CompletableFuture<String> b = service.getB();
        CompletableFuture<String> c = service.getC();

        // allOf: 모든 작업이 완료될 때까지 대기
        return CompletableFuture.allOf(a, b, c)
                .thenApply(v -> {
                    try {
                        // 각 CompletableFuture에서 결과 꺼내기 (get은 블로킹이지만, 이미 완료됨)
                        return "결과: " + a.get() + ", " + b.get() + ", " + c.get();
                    } catch (Exception e) {
                        // 하나라도 예외 발생 시 fallback 응답 반환
                        return "Fallback: 실패";
                    }
                });
    }
}

@Service
@RequiredArgsConstructor
class ApiAggregationService {

    // 외부 서비스 A 호출 (비동기 실행)
    @Async
    public CompletableFuture<String> getA() {
        simulateDelay(1000); // 1초 지연
        return CompletableFuture.completedFuture("응답A");
    }

    // 외부 서비스 B 호출 (비동기 실행)
    @Async
    public CompletableFuture<String> getB() {
        simulateDelay(800); // 0.8초 지연
        return CompletableFuture.completedFuture("응답B");
    }

    // 외부 서비스 C 호출 (비동기 실행 + 실패 가능성)
    @Async
    public CompletableFuture<String> getC() {
        simulateDelay(1200); // 1.2초 지연

        // 50% 확률로 실패 발생
        if (new java.util.Random().nextBoolean()) {
            throw new RuntimeException("C 실패");
        }

        return CompletableFuture.completedFuture("응답C");
    }

    // 지연 시뮬레이션
    private void simulateDelay(long millis) {
        try { Thread.sleep(millis); } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

