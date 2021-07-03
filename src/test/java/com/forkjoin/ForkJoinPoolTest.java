package com.forkjoin;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author 김지애(Nova) / jiae.kim413@dreamus.io
 * @since 2021/07/03
 */
public class ForkJoinPoolTest {

    @Test
    void ForkJoinPool_RecursiveAction_테스트_리턴값이_없는_경우() throws InterruptedException {
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);

        MyRecursiveAction recursiveAction = new MyRecursiveAction(128);
        forkJoinPool.invoke(recursiveAction);

        forkJoinPool.awaitTermination(5, TimeUnit.SECONDS);
    }

    @Test
    void ForkJoinPool_RecursiveTask_테스트_리턴값이_있는_경우() throws InterruptedException {
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);

        MyRecursiveTask recursiveTask = new MyRecursiveTask(128);
        long mergedResult = forkJoinPool.invoke(recursiveTask);
        System.out.println("mergedResult = " + mergedResult);

        forkJoinPool.awaitTermination(5, TimeUnit.SECONDS);
    }

    @Test
    void ForkJoinPool_RecursiveTask_비동기_테스트_리턴값이_있는_경우() throws InterruptedException, ExecutionException {
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);
        MyRecursiveTask recursiveTask = new MyRecursiveTask(128);
        Future<Long> future = forkJoinPool.submit(recursiveTask);

        System.out.println("계산중..");
        System.out.println("mergedResult = " + future.get());

        forkJoinPool.awaitTermination(5, TimeUnit.SECONDS);
    }

    @Test
    void FactorialSqureCalculator_ForkJoin_테스트() {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        FactorialSquareCalculator calculator = new FactorialSquareCalculator(10);
        forkJoinPool.execute(calculator);
    }
}
