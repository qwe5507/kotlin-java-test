package com.example.test.multithread;

import java.util.concurrent.*;

// RecursiveTask<T>: 결과값이 존재하는 재귀 병렬 작업 정의용 추상 클래스
public class ForkJoinSumTask extends RecursiveTask<Long> {
    private static final int THRESHOLD = 1000; // 작업을 나눌 기준
    private final long[] numbers;
    private final int start, end;

    public ForkJoinSumTask(long[] numbers, int start, int end) {
        this.numbers = numbers;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        // 작업 크기가 작으면 직접 합계 계산
        if (end - start <= THRESHOLD) {
            long sum = 0;
            for (int i = start; i < end; i++) sum += numbers[i];
            return sum;
        } else {
            // 작업 크기가 크면 좌우로 분할
            int mid = (start + end) / 2;

            // 왼쪽 절반 작업을 비동기로 fork (스레드풀에 위임)
            ForkJoinSumTask left = new ForkJoinSumTask(numbers, start, mid);
            left.fork();

            // 오른쪽은 현재 스레드가 직접 계산 (워크 스틸링 최소화)
            ForkJoinSumTask right = new ForkJoinSumTask(numbers, mid, end);
            long rightResult = right.compute();

            // 왼쪽 결과는 join()으로 기다림
            long leftResult = left.join();

            // 최종 결과 합산
            return leftResult + rightResult;
        }
    }

    public static void main(String[] args) {
        // 테스트 데이터: 1부터 10,000까지의 숫자 배열
        long[] numbers = new long[10_000];
        for (int i = 0; i < numbers.length; i++) numbers[i] = i + 1;

        // 기본 ForkJoinPool 사용
        ForkJoinPool pool = new ForkJoinPool();

        // 전체 합산 작업 생성
        ForkJoinSumTask task = new ForkJoinSumTask(numbers, 0, numbers.length);

        // 병렬 합산 실행
        long result = pool.invoke(task);
        System.out.println("총합: " + result); // 결과: 50005000
    }
}
