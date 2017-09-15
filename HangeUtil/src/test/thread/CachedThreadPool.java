package test.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CachedThreadPool {
	public static void main(String args[]) {
		long time = System.currentTimeMillis();
		//ExecutorService exec = Executors.newCachedThreadPool();
		//ExecutorService exec = Executors.newFixedThreadPool(10);
		ExecutorService exec = Executors.newScheduledThreadPool(10);
		for (int i = 0; i < 5; i++) {
			exec.execute(new LiftOff(10, time));
		}
		exec.shutdown();
	}
}
