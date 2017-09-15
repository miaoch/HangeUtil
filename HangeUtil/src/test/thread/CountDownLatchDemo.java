package test.thread;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountDownLatchDemo {
	static final int SIZE = 100;
	public static void main(String args[]) throws Exception {
		ExecutorService exec = Executors.newCachedThreadPool();
		CountDownLatch latch = new CountDownLatch(SIZE);
		for (int i=0; i<10; i++) {
			exec.execute(new WaitingTask(latch));
		}
		for (int i=0; i<SIZE; i++) {
			exec.execute(new TaskPortion(latch));
		}
		System.out.println("Launched all tasks");
		exec.shutdown();
	}
}
class TaskPortion implements Runnable {
	private static int counter = 1;
	private static Random rand = new Random(47);
	private final int id = counter++;
	private final CountDownLatch latch;
	public TaskPortion(CountDownLatch latch) {
		this.latch = latch;
	}
	@Override
	public void run() {
		try {
			Thread.sleep(rand.nextInt(2000));//模拟一个任务需要时间去完成
			System.out.println(this + "completed");
			latch.countDown();
		} catch (InterruptedException e) {}
	}
	public String toString() {
		return String.format("%-3d ", id);
	}
}
class WaitingTask implements Runnable {
	private static int counter = 1;
	private final int id = counter++;
	private final CountDownLatch latch;
	public WaitingTask(CountDownLatch latch) {
		this.latch = latch;
	}
	@Override
	public void run() {
		try {
			latch.await();
			System.out.println("Latch barrier passed for " + this);
		} catch (InterruptedException e) {
			System.out.println(this + "interrupted");
		}
	}
	public String toString() {
		return String.format("WaitingTask %-3d ", id);
	}
}

















