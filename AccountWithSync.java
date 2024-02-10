import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class AccountWithSync {
	private static Account userAccount = new Account();
	public static void main(String[] args) {
		ExecutorService executor = Executors.newCachedThreadPool();
		
		for(int i = 0; i < 100; i++) {
			executor.execute(new AddAPenny());
		}
		executor.shutdown();
		while(!executor.isShutdown()) {
		}
		System.out.println("What is balance? " + userAccount.getBalance());
	}
	private static class AddAPenny implements Runnable{
		public void run() {
			synchronized(userAccount) {
				userAccount.deposit(1);
			}
		}
	}
	
	private static class Account{
		private static Lock lock = new ReentrantLock(true); // create a lock with fairness set to true, so that the longest waiting thread will get the lock,here ReentrantLock is a class that implements the Lock interface and provides the same mutual exclusion and memory visibility semantics as synchronized, other methods like ReentrantLock(boolean fair), ReentrantLock() and ReentrantLock(boolean fair, Condition condition) are also available along with the methods of Lock interface like lock(), unlock(), tryLock(), newCondition() etc.
		private int balance = 0;
		public int getBalance() {
			return balance;
		}
		
		public void deposit(int amount) {
			lock.lock();            // begin the lock process
			try {
				int newBalance = balance + amount;
				balance = newBalance;
			}
			finally {
				lock.unlock(); // end it to give other threads a chance
			}
			
		}
		
	}
}
