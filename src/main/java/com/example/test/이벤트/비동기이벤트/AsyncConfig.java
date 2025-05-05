package com.example.test.이벤트.비동기이벤트;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync // 비동기 활성화 필수
public class AsyncConfig {
    // Executor 커스터마이징 필요 시 여기에 정의 가능
}

