package com.sergio.executors;

import java.util.UUID;
import java.util.concurrent.*;

/**
 * This class shows how we can use interruption for cancellation.
 *
 * <b>Based on the example Listening 7.5 Concurrency In Practice</b>
 */
public class InterruptionExample {

    private static class MyTasker extends Thread {
        private final BlockingQueue<String> bq;

        MyTasker(BlockingQueue<String> bq) {
            this.bq = bq;
        }

        @Override
        public void run() {
            try {
                while(!Thread.currentThread().isInterrupted()) { // check Thread.currentThread.isInterrupted()
                    bq.put(generateUUID()); // this method check Thread.currentThread.isInterrupted() too
                }
            } catch (InterruptedException e) {
                System.out.println("in catch, is interrupted? -> " + isInterrupted());
            }
        }

        /**
         * helper method
         *
         * @return generated UUID
         */
        private String generateUUID(){return UUID.randomUUID().toString();}

        /**
         * cancel current task.
         */
        private void cancel(){
            // blocking libraries like BlockingQueue can be disrupted interrupting the thread which run.
            interrupt(); // this interruption shoot InterruptedException in BlockingQueue#put
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
