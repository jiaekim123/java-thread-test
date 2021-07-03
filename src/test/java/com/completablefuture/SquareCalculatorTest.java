package com.completablefuture;


import org.junit.jupiter.api.Test;

import java.util.concurrent.*;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 설명 :
 *
 * @author 김지애(Nova) / jiae.kim413@dreamus.io
 * @since 2021/07/03
 */
class SquareCalculatorTest {
    private final Logger logger = Logger.getGlobal();

    @Test
    void CompletableFuture_SquareCalculator_테스트() throws ExecutionException, InterruptedException {
        SquareCalculator squareCalculator = new SquareCalculator();
        Future<String> completableFuture = squareCalculator.calculateAsync();
        System.out.println("실행 중...");
        String result = completableFuture.get();
        assertEquals("Hello", result);
    }

    // 이미 값을 알고 있는 경우 thread를 만들지 않고 completableFuture.completedFuture로 사용 가능
    @Test
    void CompletableFuture_테스트() throws ExecutionException, InterruptedException {
        Future<String> completableFuture = CompletableFuture.completedFuture("Hello");
        System.out.println("실행 중...");
        String result = completableFuture.get();
        assertEquals("Hello", result);
    }

    @Test
    void CompletableFuture_cancle_메서드_테스트() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = new CompletableFuture<>();
        Executors.newCachedThreadPool().submit(() -> {
            Thread.sleep(2000);
            future.cancel(false);
            return null;
        });

        String result = null;
        try {
            result = future.get();
        } catch (CancellationException e) {
            e.printStackTrace();
            result = "Canceled!";
        }
        logger.info(result);
    }

    // supplyAsync: 직접 스레드를 생성하지 않고 async로 처리 (리턴 값 있음)
    @Test
    void CompletableFuture_supplyAsync_리턴_있음() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "future example");
        logger.info("get(): " + future.get());
    }

    @Test
    void CompletableFuture_supplyAsync_리턴_있음_sleep() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            logger.info("Starting...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Finished works";
        });
        logger.info("get(): " + future.get());
    }

    // runAsync: 직접 스레드를 생성하지 않고 async로 처리 (리턴 값 없음)
    @Test
    void CompletableFuture_runAsync_리턴_없음() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> logger.info("futture example"));
        logger.info("get(): " + future.get());
    }

    @Test
    void CompletableFuture_Async_에러_핸들링() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            String name = null;
            if (name == null) {
                throw new RuntimeException("Computation error!");
            }
            return "Hello, " + name;
        }).handle((s, t) -> s != null ? s : "Hello, Stranger!");
        logger.info(future.get());
    }

    // supplyAsync는 어떤 작업이 처리되면 그 결과를 가지고 다른 작업도 수행할 수 있도록 구현
    // thenApply: 인자와 리턴 값이 있는 람다 수행 (여기서 인자는 supplyAsync에서 리턴되는 값)
    @Test
    void CompletableFuture_Async_thenApply_인자_리턴값_있음() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Future1");
        CompletableFuture<String> future2 = future1.thenApply(s -> s + " + Future2");

        logger.info("future1.get(): " + future1.get());
        logger.info("future2.get(): " + future2.get());
    }

    @Test
    void CompletableFuture_Async_thenApply2_인자_리턴값_있음() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture
                .supplyAsync(() -> "Future1")
                .thenApply(s -> s + " + Future2");

        logger.info("future.get(): " + future.get());
    }

    @Test
    void CompletableFuture_Async_thenApply3_인자_리턴값_있음() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture
                .supplyAsync(() -> "Future1")
                .thenApply(s -> s + " + Future2")
                .thenApply(s -> s + " + Future3");

        logger.info("future.get(): " + future.get());
    }

    // thenAccept: 리턴 값이 없는 작업 수행 (인자는 있지만 리턴은 없는 경우)
    @Test
    void CompletableFuture_Async_thenAccept_인자_있고_리턴값_없음() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future1 = CompletableFuture
                .supplyAsync(() -> "Hello");
        CompletableFuture<Void> future2 = future1
                .thenAccept(s -> logger.info(s + " World"));
        logger.info("future1.get() :" + future1.get());
        logger.info("future2.get() :" + future2.get());
    }

    // thenCompose: 여러 작업을 순차적으로 수행
    // 메서드 체이닝처럼 CompletableFuture를 하나의 CompletableFuture로 만들어주는 역할을 한다.
    // 첫번째 CompletableFuture의 결과가 리턴되면 그 결과를 두 번째 CompletableFuture로 전달하며, 순차적으로 작업이 처리된다.
    @Test
    void CompletableFuture_Async_thenCompose_순차_처리() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture
                .supplyAsync(() -> "Hello")
                .thenCompose(s -> CompletableFuture.supplyAsync(() -> s + " World"));
        logger.info(future.get());
    }

    @Test
    void CompletableFuture_Async_thenCompose_순차_처리2() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture
                .supplyAsync(() -> "Hello")
                .thenCompose(s -> CompletableFuture.supplyAsync(() -> s + " World"))
                .thenCompose(s -> CompletableFuture.supplyAsync(() -> s + " Java"));
        logger.info(future.get());
    }

    // thenCombine: 여러 작업을 동시에 수행
    // thenCompose가 여러개의 CompletableFuture를 순차적으로 처리한다면, thenCombine은 여러개의 CompletableFuture를 병렬로 처리
    // 병렬로 모든 처리를 완료한 뒤에 그 결과를 하나로 합칠 수 있음.

    @Test
    void CompletableFuture_Async_thenCombine() {
        CompletableFuture<String> future1 = CompletableFuture
                .supplyAsync(() -> "Future1")
                .thenApply((s) -> {
                    logger.info("Starting future1");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return s + "!";
                });

        CompletableFuture<String> future2 = CompletableFuture
                .supplyAsync(() -> "Future2")
                .thenApply((s) -> {
                    logger.info("Starting future2");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return s + "!";
                });
        future1.thenCombine(future2, ((s1, s2) -> s1 + " + " + s2))
                .thenAccept((s) -> logger.info(s));
    }

    // thenApply vs thenApplyAsync
    // thenApply 대신 thenApplyAsync를 사용하면 다른 쓰레드에서 동작하도록 만들 수 있음.

    @Test
    void CompletableFuture_Async_thenCombine_thenApplyAsync() {
        CompletableFuture<String> future1 = CompletableFuture
                .supplyAsync(() -> "Future1")
                .thenApplyAsync((s) -> {
                    logger.info("Starting future1");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return s + "!";
                });

        CompletableFuture<String> future2 = CompletableFuture
                .supplyAsync(() -> "Future2")
                .thenApplyAsync((s) -> {
                    logger.info("Starting future2");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return s + "!";
                });
        future1.thenCombine(future2, ((s1, s2) -> s1 + " + " + s2))
                .thenAccept((s) -> logger.info(s));
    }

    // anyOf: 여러개의 CompletableFuture에서 가장 빨리 처리되는 1개의 결과만을 가져오는 메서드
    // (3개의 future는 모두 실행되지만 thenAccept에 전달되는 것이 하나임)
    @Test
    void CompletableFuture_anyOf() {
        CompletableFuture<String> future1 = CompletableFuture
                .supplyAsync(() -> {
                    logger.info("starting future1");
                    return "Future1";
                });
        CompletableFuture<String> future2 = CompletableFuture
                .supplyAsync(() -> {
                    logger.info("starting future2");
                    return "Future2";
                });
        CompletableFuture<String> future3 = CompletableFuture
                .supplyAsync(() -> {
                    logger.info("starting future3");
                    return "Future3";
                });
        CompletableFuture.anyOf(future1, future2, future3)
                .thenAccept(s -> logger.info("Result: " + s));
    }
}