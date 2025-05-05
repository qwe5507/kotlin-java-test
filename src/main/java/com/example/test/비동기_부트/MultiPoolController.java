package com.example.test.비동기_부트;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class MultiPoolController {

    @Autowired private ServiceA serviceA;
    @Autowired private ServiceB serviceB;

    @GetMapping("/multi-async")
    public List<String> runAsyncTasks() throws Exception {
        CompletableFuture<String> a = serviceA.execute();
        CompletableFuture<String> b = serviceB.execute();

        return CompletableFuture.allOf(a, b)
                .thenApply(v -> List.of(a.join(), b.join()))
                .get();
    }
}
