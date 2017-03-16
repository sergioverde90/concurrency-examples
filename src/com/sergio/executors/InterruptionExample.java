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
         *
         * <ul>
         *  <li> Thread.currentThread().getId(); <i>// this thread id</i></li>
         *  <li> getId(); <i>// this thread id</i></li>
         * </ul>
         *
         * BlockingQueue will be interrupted when this thread is interrupted in cancel method
         */
        @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    bq.put(generateUUID());
                } catch (InterruptedException e) {
                    System.out.println("in catch, is interrupted? -> " + isInterrupted());
                    // preserve interruption status
                    // FIXME: when I call isInterrupted() often returns false and never finish
                    // FIXME: why isInterrupted() returns false?????
                    // FIXME: based on: Concurrency In Practice Listing 7.5
                    Thread.currentThread().interrupt();
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
            interrupt(); // this interruption shoot InterruptedException in BlockingQueue#put
            // Is BlockingQueue#put (Thread.interrupt()) recovering interruption status??
            System.out.println("in cancel, is interrupted? -> " + isInterrupted());
        }
    }

    public static void main(String[] args) {
        MyTasker t = new MyTasker(new LinkedBlockingQueue<String>());
        t.start();
        // timeout after 1,5 seg
        try {Thread.sleep(1500L);} catch (InterruptedException e) {}
        t.cancel();
    }

}
