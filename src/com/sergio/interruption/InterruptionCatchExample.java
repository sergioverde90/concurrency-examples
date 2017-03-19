package com.sergio.interruption;

/**
 * This class shows what happens if we swallow InterruptedException and
 * not recover the interruption status or propagate exception.
 *
 * There are two options for handle {@link InterruptedException}:
 * <ul>
 *     <li>Propagate {@link InterruptedException} if it is possible, making your method
 *      interruptible too</li>
 *     <li>
 *         Restore the interruption status calling {@code Thread.currentThread().interrupt()}
 *     </li>
 * </ul>
 *
 * <p>
 *     <h3>References:</h3>
 *     <ul>
 *         <li>Concurrency in Practice 7.1.3 Responding to interruptio</li>
 *         <li><a href="http://www.javaspecialists.eu/archive/Issue056.html">InterruptionException JavaSpecialist</a></li>
 *     </ul>
 * </p>
 */
public class InterruptionCatchExample {
    private static class InterruptionIssue extends Thread {
        @Override
        public void run() {
            try {
                while(true){
                    Thread.sleep(1000);
                    boolean exited = false;
                    for (int i = 0; i < 10 && !exited; i++) {
                        try {
                            Thread.sleep(100);
                        }catch (InterruptedException inner){
                            System.out.println("interrupting for loop...");
                            // if we do not recover the interrupted status
                            // the while loop never will be informed
                            // that the thread has been interrupted
                            // FIXME: Uncomment for a correct behavior
                            // FIXME: swallow exception, DON'T DO THIS!! :(
                            // Thread.currentThread().interrupt();
                            System.out.println("for loop interrupted!");
                            exited = true;
                        }
                    }
                }
            } catch (InterruptedException ex) {
                System.out.println("interrupting while loop...");
                Thread.currentThread().interrupt();
                System.out.println("while loop has been interrupted");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        InterruptionIssue issue = new InterruptionIssue();
        issue.start();

        Thread.sleep(4000L);

        issue.interrupt();
    }

}
