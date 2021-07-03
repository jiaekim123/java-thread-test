package com.future;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 설명 :
 *
 * @author 김지애(Nova) / jiae.kim413@dreamus.io
 * @since 2021/07/03
 */
class SquareCalculatorTest {
    @Test
    void Future_SquareCalculator_정상호출() throws InterruptedException, ExecutionException {
        Future<Integer> future = new SquareCalculator().calculate(10);

        while (!future.isDone()) {
            System.out.println("계산중..");
            Thread.sleep(300);
        }

        Integer result = future.get();
        System.out.println(result);
    }

    @Test
    void Future_SquareCalculator_타임아웃호출_정상() throws InterruptedException, ExecutionException {
        Future<Integer> future = new SquareCalculator().calculate(10);

        Integer result = 0;
        try {
            result = future.get(10000, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            System.out.println("타임아웃 발생");
        }
        System.out.println(result);
    }

    @Test
    void Future_SquareCalculator_타임아웃_호출_발생() throws InterruptedException, ExecutionException {
        Future<Integer> future = new SquareCalculator().calculate(10);

        Integer result = 0;
        try {
            result = future.get(1000, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            System.out.println("타임아웃 발생");
        }
        System.out.println(result);
    }

    @Test
    void Future_SquareCalculator_취소() throws InterruptedException, ExecutionException {
        Future<Integer> future = new SquareCalculator().calculate(10);
        boolean canceled = future.cancel(true);
        System.out.println(canceled);
    }

    @Test
    void Future_SquareCalculator_MultiThread_ThreadPool_Test() throws InterruptedException, ExecutionException {
        SquareCalculator squareCalculator = new SquareCalculator();

        Future<Integer> future1 = squareCalculator.calculate(10);
        Future<Integer> future2 = squareCalculator.calculate(100);

        while (!(future1.isDone() && future2.isDone())) {
            System.out.println(
                    String.format("future1 is %s and future2 is %s",
                            future1.isDone() ? "done" : "not done",
                            future2.isDone() ? "done" : "not done")
            );
            Thread.sleep(1000);
        }
        Integer result1 = future1.get();
        Integer result2 = future2.get();

        System.out.println(result1 + " and " + result2);

        squareCalculator.shutdown();
    }

    @Test
    void Future_SquareCalculator_MultiThread_FixedThreadPool_Test() throws InterruptedException, ExecutionException {
        SquareCalculatorFixedThread squareCalculator = new SquareCalculatorFixedThread();

        Future<Integer> future1 = squareCalculator.calculate(10);
        Future<Integer> future2 = squareCalculator.calculate(100);

        while (!(future1.isDone() && future2.isDone())) {
            System.out.println(
                    String.format("future1 is %s and future2 is %s",
                            future1.isDone() ? "done" : "not done",
                            future2.isDone() ? "done" : "not done")
            );
            Thread.sleep(1000);
        }
        Integer result1 = future1.get();
        Integer result2 = future2.get();

        System.out.println(result1 + " and " + result2);

        squareCalculator.shutdown();
    }
}