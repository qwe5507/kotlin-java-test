package com.example.test

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication(scanBasePackages = ["com.example.test.비동기_부트"])
class TestApplication

fun main(args: Array<String>) {
	runApplication<TestApplication>(*args)
}
