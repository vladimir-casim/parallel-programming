package liang;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Vladimir on 26.09.2017.
 */
public class ConsumerProducerUsingBlockingQueue {
    private static ArrayBlockingQueue<Integer> buffer = new ArrayBlockingQueue<>(2);

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(new ProducerTask());
        executorService.execute(new ConsumerTask());
        executorService.shutdown();
    }

    private static class ProducerTask implements Runnable {

        @Override
        public void run() {
            try {
                int i = 1;
                while (true) {
                    System.out.println("Producer writes " + i);
                    buffer.put(i++);
                    Thread.sleep((int)(Math.random() * 10000));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class ConsumerTask implements Runnable {

        @Override
        public void run() {
            try {
                while (true) {
                    System.out.println("Consumer reads " + buffer.take());
                    Thread.sleep((int)(Math.random() * 10000));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
