package com.sergio.basic;

/**
 * Created by Sergio on 05/04/2017.
 *
 * Example of HijackedSignal when {@link Object#notify()} is called
 * instead {@link Object#notifyAll()}
 */
public class HijackedSignalExample {

    static class A {
        volatile boolean p1 = false;
        volatile boolean p2 = false;
    }

    /**
     * EXPLANATION EXAMPLE: Two threads waiting to the same condition queue
     * with different condition predicate.
     *
     * This example shows the incorrect usage of {@link Object#notify()} instead
     * {@link Object#notifyAll()}.
     *
     * If unfortunately when 'p1' is set to 'true' the {@link Object#notify()} method
     * notifies to 't2' instead 't1' (under 'p1 condition), 't2' will see that its condition
     * is not true and will wait again, and 't1' never will be notified.
     *
     * Sometimes you will see this output:
     *
     * <p><i>
     *   waiting R2 <br />
     *   waiting R1 <br />
     *   wake up R2 <br />
     *   waiting R2 <br />
     *   wake up R1 <br />
     *   EXECUTED R1 <br />
     *   ... (never finish)
     * </i></p>
     *
     * @param args
     *
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        final A a = new A();
        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                synchronized (a){
                    while(!a.p1){
                        System.out.println("waiting R1");
                        try{a.wait();}catch(InterruptedException e){}
                        System.out.println("wake up R1");
                    }
                    System.err.println("EXECUTED R1");
                }
            }
        };
        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                synchronized (a){
                    while(!a.p2){
                        System.out.println("waiting R2");
                        try{a.wait();}catch(InterruptedException e){}
                        System.out.println("wake up R2");
                    }
                    System.err.println("EXECUTED R2");
                }
            }
        };

        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);
        t1.start();
        t2.start();

        Thread.sleep(500L);

        a.p1 = true;
        synchronized (a){
            a.notify();
        }

        a.p2 = true;
        synchronized (a){
            a.notify();
        }
    }

}
