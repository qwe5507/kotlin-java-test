package com.example.test.이벤트.blockingqueueevent;

// 📦 1. 이벤트 클래스
class CustomEvent {
    private final String message;

    public CustomEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}