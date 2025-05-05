package com.example.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello-java")
    public String helloJava() {
        return "Hello from Java!";
    }

    public static void main(String[] args) {
        System.out.println("Hello from Java!");
    }
}
