package com.future;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * 설명 :
 *
 * @author 김지애(Nova) / jiae.kim413@dreamus.io
 * @since 2021/06/30
 */

class FutureTest {
    private Logger logger = Logger.getGlobal();

    @Test
    void futureRunTest1() throws ExecutionException, InterruptedException {
        // 1. ExecutorService가 single thread를 생성
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // 2. submit()으로 callable을 전달하면 인자로 전달된 Callable을 수행
        // 3. executor.submit()가 호출되엇을 때 Future 객체 리턴. (아직 값이 설정되지 않음)
        Future<Integer> future = executorService.submit(() -> {
            System.out.println(LocalDateTime.now() + " Starting runnuble");
            int sum = 1 + 1;
            Thread.sleep(3000);
            return sum;
        });

        System.out.println(LocalDateTime.now() + " Waiting the task done");
        // 4. Future 객체에 어떤 값이 설정되기까지 기다림. submit()에 전달된 Callable이 어떤 값을 리턴하면 그 값을 Future에 설정
        Integer result = future.get();
        System.out.println(LocalDateTime.now() + " Result : " + result);
    }

    @Test
    void futureRunTest2() throws ExecutionException, InterruptedException {

        CompletableFuture<Integer> future = new CompletableFuture<>();
        Executors.newCachedThreadPool().submit(() -> {
            logger.info(LocalDateTime.now() + " Doing something");
            Integer sum = 1 + 1;
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                logger.info(e.getMessage());
            }
            future.complete(sum);
        });

        logger.info(LocalDateTime.now() + " Waiting the task done");
        Integer result = future.get();
        logger.info(LocalDateTime.now() + " Result : " + result);
    }

    @Test
    void futureTimeoutTest() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Future<Integer> future = executorService.submit(() -> {
            logger.info(LocalDateTime.now() + " Runnable 시작");
            int sum = 1 + 1;
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(LocalDateTime.now() + " Runnable 종료");
            return sum;
        });

        System.out.println(LocalDateTime.now() + " 일 끝날때까지 기다리는중..");
        Integer result = null;
        try {
            result = future.get(2000, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            System.out.println(LocalDateTime.now() + " 시간 초과");
            result = 0;
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("인터럽트 예외 발생: " + e.getMessage());
        }
        System.out.println(LocalDateTime.now() + " Result : " + result);
    }
}