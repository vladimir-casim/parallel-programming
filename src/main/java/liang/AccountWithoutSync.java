package liang;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Vladimir on 25.09.2017.
 */
public class AccountWithoutSync {

    private static Account account = new Account();

    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();

        for (int i = 0; i < 100; i++) {
            executor.execute(new AddAPennyTask());
        }

        executor.shutdown();
        // Wait until all tasks are finished
        while (!executor.isTerminated()) { }
        System.out.println("What is balance? " + account.getBalance());
    }

    private static class AddAPennyTask implements Runnable {

        @Override
        public void run() {
            account.deposit(1);
        }
    }

    private static class Account {
        private double balance = 0;

        public double getBalance() {
            return balance;
        }

        public synchronized void deposit(double amount) {
            double newBalance = balance + amount;

            // This delay is deliberately added to magnify the
            // data-corruption problem and make it easy to see
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            balance = newBalance;
        }
    }
}

