package test.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Interrupting2 {
	public static void main(String args[]) throws InterruptedException {
		final BlockedMutex b = new BlockedMutex();
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("线程请求锁");
				b.f();
				System.out.println("线程执行完毕");
			}
		});
		t.start();
		Thread.sleep(1000);
		System.out.println("发送中断");
		t.interrupt();//手动中断
	}
}
class BlockedMutex {
	private Lock lock = new ReentrantLock();//这个锁是可用被中断的
	
	public BlockedMutex() {
		lock.lock();//永久加锁
	}
	public void f() {
		try {
			lock.lockInterruptibly();//lockInterruptibly可以被中断
			System.out.println("得到锁了，其实这里不可能被执行");
		} catch (InterruptedException e) {
			System.out.println("被中断了");
		}
	}
}