package liang;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Vladimir on 25.09.2017.
 */
public class ThreadCooperation {
    private static Account account = new Account();

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new DepositTask());
        executorService.execute(new WithdrawTask());
        executorService.shutdown();
        System.out.println("Thread 1\t\tThread 2\t\tBalance");
    }

    private static class DepositTask implements Runnable {
        @Override
        public void run() {
            try { // Purposely delay it to let the withdraw method proceed
                while (true) {
                    account.deposit((Math.random() * 10) + 1);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class WithdrawTask implements  Runnable {
        @Override
        public void run() {
            while (true) {
                account.withdraw((Math.random() * 10) + 1);
            }
        }
    }

    private static class Account {
        private static Lock lock = new ReentrantLock();
        private static Condition newDepositCondition = lock.newCondition();
        private int balance = 0;

        public double getBalance() {
            return balance;
        }

        public void deposit(double amount) {
            lock.lock();
            try {
                balance += amount;
                System.out.println("Deposit " + amount + "\t\t\t\t\t" + getBalance());

                // Signal thread waiting on the condition
                newDepositCondition.signalAll();
            } finally {
                lock.unlock();
            }
        }

        public void withdraw(double amount) {
            lock.lock();
            try {
                while (balance < amount) {
                    System.out.println("\t\t\tWait for a deposit");
                    newDepositCondition.await();
                }

                balance -= amount;
                System.out.println("\t\t\tWithdraw " + amount + "\t\t" + getBalance());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

    }
}
