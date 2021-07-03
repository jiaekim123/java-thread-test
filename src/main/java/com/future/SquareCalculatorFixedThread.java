package com.future;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author 김지애(Nova) / jiae.kim413@dreamus.io
 * @since 2021/07/03
 */
public class SquareCalculatorFixedThread {
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    public Future<Integer> calculate(Integer input) {
        return executorService.submit(() -> {
            Thread.sleep(1000);
            return input * input;
        });
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
