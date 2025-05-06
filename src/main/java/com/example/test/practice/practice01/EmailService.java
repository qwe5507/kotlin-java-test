package com.example.test.practice.practice01;

public class EmailService {
    public void send(String email) throws Exception {
        if (Math.random() < 0.5) throw new Exception("Email send failed");
        System.out.println("Email sent to: " + email);
    }
}
