package com.sergio.executors;

import java.util.UUID;
import java.util.concurrent.*;

/**
 * Created by Sergio on 14/03/2017.
 */
public class InterruptionExample {

    private static class MyTasker extends Thread {
        private final BlockingQueue<String> bq;

        MyTasker(BlockingQueue<String> bq) {
            this.bq = bq;
        }

        /**
         * This method will be called by MyTasker thread, that is:
         * <ul>
         *  <li> Thread.currentThread().getId(); <i>// this thread id</i></li>
         *  <li> getId(); <i>// this thread id</i></li>
         * </ul>
         */
        @Override
        public void run() {
            System.out.println("------------------------------");
            System.out.println("run method thread information:");
            System.out.println("current thread -> "+Thread.currentThread().getId()); // caller thread id (main thread)
            System.out.println("this.getId() -> " + this.getId()); // MyTasker thread id
            System.out.println("------------------------------");
            while(!Thread.currentThread().isInterrupted()){
                try {
                    bq.put(generateUUID());
                } catch (InterruptedException e) {
                    System.out.println("interrupted catch!");
                    // preserve interruption status
                    // Thread.currentThread().interrupt();
                }
            }
        }

        private String generateUUID(){return UUID.randomUUID().toString();}

        /**
         * This method is called by the main thread (or current thread),
         * that is:
         * Thread.currentThread().getId(); // caller thread id
         * getId(); // this thread id
         */
        private void cancel(){
            System.out.println(Thread.currentThread().getId()); // caller thread id (main thread)
            System.out.println(this.getId()); // MyTasker thread id
            interrupt();
        }
    }

    public static void main(String[] args) {
        MyTasker t = new MyTasker(new LinkedBlockingQueue<String>());
        t.start();
        // cancel will be called after 1,5 seg
        try {Thread.sleep(1500L);} catch (InterruptedException e) {}
        t.cancel();
    }

}
