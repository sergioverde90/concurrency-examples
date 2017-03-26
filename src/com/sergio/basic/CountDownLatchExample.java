package com.sergio.basic;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Sergio on 26/03/2017.
 *
 * {@link java.util.concurrent.CountDownLatch} example
 */
public class CountDownLatchExample {

    public static void main(String[] args) {
        // latches wait for events[CiP 5.5.1]
        // Once the latch reaches the terminal state
        // it remains open forever.
        final CountDownLatch latch = new CountDownLatch(2);
        Thread t1 = new Thread(() -> {
            try {Thread.sleep(6000);} catch (InterruptedException e) {}
            latch.countDown();
        });
        Thread t2 = new Thread(() -> {
            try {Thread.sleep(3000);} catch (InterruptedException e) {}
            latch.countDown();
        });
        t1.start();
        t2.start();
        long start = System.nanoTime();
        try {latch.await();} catch (InterruptedException e) {/*allow thread to exit*/}
        long end = System.nanoTime();
        System.out.println(end - start +" ns");
    }

}
