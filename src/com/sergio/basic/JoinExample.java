package com.sergio.basic;

/**
 * Example usage of {@link Thread#join()}
 */
public class JoinExample {
    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {}
        });
        t.start();
        System.out.println("executing my short task...");
        myShortTask();
        System.out.println("short task executed!");
        System.out.println("waiting to 't' thread...");
        // join method can be interpreted like:
        // "the current thread can not continue until 't' thread is completed"
        t.join(); // current thread wait to 't' thread
        long time = System.currentTimeMillis() - start;
        System.out.println("finished in "+time+" seconds");
    }

    private static void myShortTask() throws InterruptedException {
        Thread.sleep(2000L);
    }
}
