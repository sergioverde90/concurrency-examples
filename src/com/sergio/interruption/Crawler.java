package com.sergio.interruption;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Stream;

/**
 * Created by Sergio on 17/03/2017.
 *
 * This class implements a desktop crawler
 *
 */
public class Crawler implements Runnable {

    final BlockingQueue<Path> queue;
    final Path initialPath;

    public Crawler(BlockingQueue<Path> queue, Path initialPath) {
        this.queue = queue;
        this.initialPath = initialPath;
    }

    @Override
    public void run() {
        try {
            crawl(initialPath);
        } catch (InterruptedException e) {
            System.err.println("interrupted");
            // swallow this exception only can be addressed
            // by own thread. Are us the own thread? No, then keep
            // the interruption status to 'propagate' to higher callers.
            // Thread.currentThread().interrupt();
        } catch (IOException e) {
            /** allow thread to exit */
        }
    }

    public Object[] get(){
        return queue.toArray();
    }

    private void crawl(Path initialPath) throws InterruptedException, IOException {
        DirectoryStream<Path> directory = Files.newDirectoryStream(initialPath);
        for (Path p : directory) {
            if(Files.isDirectory(p)) {
                crawl(p);
            }else {
                queue.put(p);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        Crawler indexer = new Crawler(new LinkedBlockingQueue<>(), Paths.get("D:\\"));
        Thread t = new Thread(indexer);
        t.start();
        Thread.sleep(3000);
        t.interrupt();
        Object[] ob = indexer.get();
        Stream.of(ob).forEach(System.out::println);
    }
}
