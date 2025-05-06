package com.example.test.practice01;
import com.example.test.practice.practice01.InventoryManager;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

public class InventoryManagerTest {

    @Test
    void 재고는_음수가_되면_안된다() throws InterruptedException {
        // given
        int initialStock = 10;
        InventoryManager inventory = new InventoryManager(initialStock);

        // when
        int threadCount = 20; // 20개의 쓰레드가 동시에 재고 감소 시도
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        int[] successCount = {0};

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                if (inventory.decreaseStock()) {
                    synchronized (successCount) {
                        successCount[0]++;
                    }
                }
                latch.countDown();
            });
        }

        latch.await();

        // then
        assertThat(inventory.getStock()).isGreaterThanOrEqualTo(0);
        assertThat(successCount[0]).isEqualTo(initialStock);
    }
}
