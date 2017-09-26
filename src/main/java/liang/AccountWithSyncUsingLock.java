package liang;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Vladimir on 25.09.2017.
 */
public class AccountWithSyncUsingLock {

    private static Account account = new Account();

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < 100; i++) {
            executorService.execute(new AddAPennyTask());
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
        }

        System.out.println("Balance: " + account.getBalance());
    }

    private static class AddAPennyTask implements Runnable {

        @Override
        public void run() {
            account.deposit(1);
        }
    }

    public static class Account {
        private static Lock lock = new ReentrantLock();
        private double balance;

        public double getBalance() {
            return balance;
        }

        public void deposit(double amount) {
            lock.lock(); // Acquire the lock

            try {
                double newBalance = balance + amount;
                Thread.sleep(5);
                balance = newBalance;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

        }
    }
}
