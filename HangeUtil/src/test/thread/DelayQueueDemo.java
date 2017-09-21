package test.thread;

import java.util.*;
import java.util.concurrent.*;

class DelayedTask implements Runnable, Delayed {
	private final long delay;
	private final long runtime;
	private final int id;
	public DelayedTask(int id, long delay) {
		this.id = id;
		this.delay = delay;
		this.runtime = System.currentTimeMillis() + delay;
	}

	@Override
	public int compareTo(Delayed o) {
		DelayedTask t = (DelayedTask) o;
		long result = this.runtime -  t.runtime;
		if (result > 0) return 1;
		else if (result < 0) return -1;
		else return 0;
	}
	@Override
	public long getDelay(TimeUnit unit) {
		return unit.convert(runtime - System.currentTimeMillis(),
				TimeUnit.MILLISECONDS);
	}
	@Override
	public void run() {
		System.out.println(id + " run! delay=" + delay);
	}
}
class DelayedTaskConsumer implements Runnable {
	private DelayQueue<DelayedTask> q;
	public DelayedTaskConsumer(DelayQueue<DelayedTask> q) {
		this.q = q;
	}
	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				q.take().run();//只是run 没有start
			}
		} catch (InterruptedException e) {}
		finally {
			System.out.println("finish!");
		}
	}
}
public class DelayQueueDemo {
	public static void main(String[] args) throws Exception {
		Random rand = new Random(47);
		ExecutorService exec = Executors.newCachedThreadPool();
		DelayQueue<DelayedTask> queue =  new DelayQueue<DelayedTask>();
		for (int i=0; i<20; i++) {
			queue.put(new DelayedTask(i + 1, rand.nextInt(5000)));
		}
		exec.execute(new DelayedTaskConsumer(queue));
		Thread.sleep(5000);
		exec.shutdownNow();
	}
}
