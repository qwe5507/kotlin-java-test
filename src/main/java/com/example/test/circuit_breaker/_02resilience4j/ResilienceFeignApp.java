//package com.example.test.circuit_breaker._02resilience4j;
//
//import io.github.resilience4j.circuitbreaker.*;
//import io.github.resilience4j.retry.*;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.openfeign.*;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.Duration;
//import java.util.function.Supplier;
//
//@SpringBootApplication
//@RestController
//@EnableFeignClients // 🔹 Spring Cloud Feign 클라이언트 사용 활성화 (인터페이스 기반 HTTP 호출 가능)
//public class ResilienceFeignApp {
//
//    private final CircuitBreaker circuitBreaker;
//    private final Retry retry;
//    private final ExternalApiClient externalApiClient;
//
//    // 🔹 생성자에서 resilience4j 설정 구성
//    public ResilienceFeignApp(ExternalApiClient externalApiClient) {
//        // 🔸 Circuit Breaker 설정 구성
//        CircuitBreakerConfig cbConfig = CircuitBreakerConfig.custom()
//                .failureRateThreshold(50) // 🔸 실패율이 50%를 넘으면 circuit을 OPEN 상태로 전환
//                .waitDurationInOpenState(Duration.ofSeconds(10)) // 🔸 OPEN 상태 유지 시간 (10초)
//                .slidingWindowSize(5) // 🔸 실패율 계산을 위한 최근 요청 개수 (슬라이딩 윈도우)
//                .permittedNumberOfCallsInHalfOpenState(2) // 🔸 HALF_OPEN 상태에서 허용할 요청 수
//                .build();
//        this.circuitBreaker = CircuitBreaker.of("externalApi", cbConfig); // 🔸 명시적으로 생성 (등록만, 자동 등록 아님)
//
//        // 🔸 Retry 설정 구성
//        RetryConfig retryConfig = RetryConfig.custom()
//                .maxAttempts(3) // 🔸 최대 3회까지 재시도
//                .waitDuration(Duration.ofMillis(500)) // 🔸 각 재시도 간 대기 시간 500ms
//                .retryExceptions(RuntimeException.class) // 🔸 재시도 대상 예외 유형 설정
//                .build();
//        this.retry = Retry.of("externalApiRetry", retryConfig);
//
//        this.externalApiClient = externalApiClient;
//    }
//
//    // 🔸 실제 요청 핸들러: CircuitBreaker + Retry + Fallback 조합
//    @GetMapping("/api/feign-data")
//    public String callFeign(HttpServletResponse response) {
//        // 🔸 현재 CircuitBreaker 상태를 응답 헤더에 담아 클라이언트에 전달
//        response.setHeader("X-CircuitBreaker-State", circuitBreaker.getState().name());
//
//        // 🔸 외부 API 호출을 CircuitBreaker + Retry 로 감싸기
//        Supplier<String> decorated = CircuitBreaker
//                .decorateSupplier(circuitBreaker,
//                        Retry.decorateSupplier(retry, externalApiClient::getData)
//                );
//
//        try {
//            return decorated.get(); // 🔸 정상적으로 호출되면 바로 결과 반환
//        } catch (Exception e) {
//            return fallback(e); // 🔸 예외 발생 시 fallback 처리
//        }
//    }
//
//    // 🔸 Fallback 로직: 장애 시 대체 응답 제공
//    public String fallback(Throwable t) {
//        return "Feign fallback: " + t.getMessage();
//    }
//
//    public static void main(String[] args) {
//        SpringApplication.run(ResilienceFeignApp.class, args);
//    }
//
//    // 🔸 외부 API 클라이언트 인터페이스 정의 (Feign 사용)
//    @FeignClient(name = "externalApiClient", url = "https://httpstat.us/503") // 🔸 항상 503 응답을 반환하는 테스트용 endpoint
//    public interface ExternalApiClient {
//        @GetMapping
//        String getData(); // 🔸 단순한 GET 요청 인터페이스
//    }
//}
//
