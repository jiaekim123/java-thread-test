package com.forkjoin;

import java.util.concurrent.RecursiveTask;

/**
 * 설명 : https://www.baeldung.com/java-future
 *
 * @author 김지애(Nova) / jiae.kim413@dreamus.io
 * @since 2021/07/03
 */
public class FactorialSquareCalculator extends RecursiveTask<Integer> {
    private Integer num;

    public FactorialSquareCalculator(Integer num) {
        this.num = num;
    }

    @Override
    protected Integer compute() {
        if (num <= 1) {
            return num;
        }

        FactorialSquareCalculator calculator = new FactorialSquareCalculator(num - 1);
        calculator.fork();
        return num * num + calculator.join();
    }
}
