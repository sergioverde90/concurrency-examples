package com.sergio.basic;

/**
 * Created by Sergio on 05/04/2017.
 *
 * Example of HijackedSignal when {@link Object#notify()} is called
 * instead {@link Object#notifyAll()}
 */
public class HijackedSignalExample {

    static class A {
        volatile boolean flag = false;
    }

    public static void main(String[] args) throws InterruptedException {
        final A a = new A();
        Runnable r =  () -> {
            synchronized (a){
                while(!a.flag){
                    System.out.println("Thread "+Thread.currentThread().getName()+" waiting...");
                    try {a.wait();} catch (InterruptedException e) {} // lock will be released here
                }
                System.out.println("finally thread "+Thread.currentThread().getName());
            }
        };
        Thread t1 = new Thread(r);
        t1.setName("t1");
        Thread t2 = new Thread(r);
        t2.setName("t2");
        t1.start();
        t2.start();
        Thread.sleep(5000L);
        a.flag = true;
        synchronized (a) {
            // two thread waiting on different predicates,
            // but only one thread will wake up
            // DON'T DO THIS! CALL Object#notifyAll instead
            a.notify();
        }
    }

}
