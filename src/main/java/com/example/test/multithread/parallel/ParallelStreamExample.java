package com.example.test.multithread.parallel;

import java.util.*;
import java.util.stream.*;

// 내부적으로 ForkJoinPool.commonPool()을 사용
// 병렬 스트림을 사용하여 작업을 여러 스레드에서 동시에 처리
// 병렬 스트림은 데이터의 양이 많을 때 성능을 향상시킬 수 있음
// 단, 작은 데이터셋에서는 오히려 성능이 저하될 수 있음
public class ParallelStreamExample {
    public static void main(String[] args) {
        List<Integer> numbers = IntStream.rangeClosed(1, 10_000).boxed().toList();

        long start = System.currentTimeMillis();
        int sum = numbers.parallelStream()
                .mapToInt(n -> n * n)
                .sum();
        long end = System.currentTimeMillis();

        System.out.println("제곱합: " + sum);
        System.out.println("실행 시간: " + (end - start) + "ms");
    }
}

