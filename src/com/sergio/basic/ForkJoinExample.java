package com.sergio.concurrency;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Example of {@link ForkJoinPool} with {@link RecursiveTask}.
 *
 * Count all elements in {@link List} of {@link Integer} in parallel
 */
public class ForkJoinExample extends RecursiveTask<Integer> {

    private static final int THRESHOLD = 10;
    private final List<Integer> toCount;

    public ForkJoinExample(List<Integer> toCount) {
        this.toCount = toCount;
    }

    @Override
    protected Integer compute() {

        // if task is small enough then handles it
        if(toCount.size() < THRESHOLD) {
            Integer accumulator = 0;
            for (Integer value : toCount) {
                accumulator += value;
            }
            return accumulator;
        }

        // split current task in smaller tasks
        List<Integer> left = toCount.subList(0, toCount.size() / 2);
        List<Integer> right = toCount.subList(toCount.size() / 2, toCount.size());
        ForkJoinExample leftTask = new ForkJoinExample(left);
        ForkJoinExample rightTask = new ForkJoinExample(right);

        leftTask.fork();                                 // throw left computation
        Integer futureComputation = rightTask.compute(); // computation in another thread
        Integer currentComputation = leftTask.join();    // await to left task
        return futureComputation + currentComputation;
    }

    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool();
        Integer result = pool.invoke(new ForkJoinExample(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9)));
        System.out.println("result = " + result);
    }
}
