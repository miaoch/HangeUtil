package test.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AttemptLocking {
	private Lock lock = new ReentrantLock();
	
	public void test() {
		/*try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}*///实验效果不明显 就在这里加上休眠语句
		boolean captured = lock.tryLock();
		try {
			System.out.println("tryLock()：" + captured);
		} finally {
			if (captured) {
				lock.unlock();
			}
		}
		try {
			captured = lock.tryLock(2, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			System.out.println("tryLock(2, TimeUnit.SECONDS)：" + captured);
			if (captured) {
				lock.unlock();
			}
		}
	}
	
	public static void main(String args[]) {
		final AttemptLocking at = new AttemptLocking();
		at.test();
		new Thread() {
			{setDaemon(true);}//非后台线程
			public void run() {
				at.lock.lock();//抢锁 
				System.out.println("抢到锁啦！");//上一句会阻塞直到抢到锁
			}
		}.start();
		Thread.yield();//给子线程一个机会 不过也不是一定会给它机会
		at.test();
	}
}
