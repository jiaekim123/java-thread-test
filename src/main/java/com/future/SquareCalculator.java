package com.future;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 설명: https://www.baeldung.com/java-future
 *
 * @author 김지애(Nova) / jiae.kim413@dreamus.io
 * @since 2021/07/03
 */
public class SquareCalculator {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

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
