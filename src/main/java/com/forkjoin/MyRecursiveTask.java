package com.forkjoin;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * https://codechacha.com/ko/java-fork-join-pool/
 *
 * @author 김지애(Nova) / jiae.kim413@dreamus.io
 * @since 2021/07/03
 */
public class MyRecursiveTask extends RecursiveTask<Long> {

    private long workLoad = 0;

    public MyRecursiveTask(long workLoad) {
        this.workLoad = workLoad;
    }

    @Override
    protected Long compute() {
        String threadName = Thread.currentThread().getName();

        if (this.workLoad > 16) {
            System.out.println("[" + LocalDateTime.now() + "][" + threadName + "]" + " 분할된 workLoad : " + this.workLoad);
            sleep(1000);
            List<MyRecursiveTask> subtasks = new ArrayList<>();
            subtasks.addAll(createSubTasks());

            for (MyRecursiveTask subtask : subtasks) {
                subtask.fork();
            }

            long result = 0;
            for (MyRecursiveTask subtask : subtasks) {
                result += subtask.join();
                System.out.println("[" + LocalDateTime.now() + "][" + threadName + "]" + "Receive result from subtask");
            }
            return result;
        } else {
            sleep(1000);
            System.out.println("[" + LocalDateTime.now() + "][" + threadName + "]" + " 완료된 workLoad : " + this.workLoad);
            return workLoad * 3;
        }


    }

    private List<MyRecursiveTask> createSubTasks() {
        List<MyRecursiveTask> subtasks = new ArrayList<>();

        MyRecursiveTask subtask1 = new MyRecursiveTask(this.workLoad / 2);
        MyRecursiveTask subtask2 = new MyRecursiveTask(this.workLoad / 2);

        subtasks.add(subtask1);
        subtasks.add(subtask2);

        return subtasks;
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
