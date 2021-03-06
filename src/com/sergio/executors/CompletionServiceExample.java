package com.sergio.executors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Sergio on 01/03/2017.
 */
public class CompletionServiceExample {

    private static class Image {
        private final long time;
        private Image(long time) {
            this.time = time;
        }
        private String download(){
            System.out.println("ETA "+time / 1000 +"/seconds ...");
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return time + " image was donwloaded successfully";
        }
    }

    public static void main(String[] args) {
        List<Image> images = new ArrayList<>();
        images.add(new Image(6000));
        images.add(new Image(3000));
        images.add(new Image(1000));

        CompletionService<String> completionService = new ExecutorCompletionService<>(
                Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2));

        images.forEach(i -> completionService.submit(i::download));
        
        long start = System.nanoTime();
        images.forEach(i -> {
            try {
                Future<String> future = completionService.take();
                String downloaded = future.get();
                System.out.println(downloaded);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (ExecutionException ee){
                throw new RuntimeException(ee.getCause());
            }
        });
        long end = System.nanoTime();
        System.out.println("Finished all downloads in " + (end-start) +" / ns");
    }
}
