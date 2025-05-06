package com.example.test.practice.practice04;

public class SettlementProcessor {

    private final ProcessGuard processGuard = new ProcessGuard();

    public void process() {
        if (!processGuard.tryProcess()) return;

        // ✅ 실제 처리 책임은 명확히 분리
        performSettlement();
    }

    private void performSettlement() {
        System.out.println("✅ 정산 실행");
        // 정산 비즈니스 로직
    }
}