package test.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class TestThread implements Runnable {
	private static int i = 1;
	private int id = i++;
	public void run() {
		if (id % 5 == 0) {
			throw new RuntimeException(id + "");
		}
	}
	
	public static void main(String args[]) {
		
		ExecutorService exec = Executors.newCachedThreadPool(new DealFactory());
		for (int i = 0; i < 50; i++) {
			exec.execute(new TestThread());
		}
	}
}
class DealFactory implements ThreadFactory {
	@Override //newThread 工厂的流水线
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r);
		//通过setUncaughtExceptionHandler为线程指定异常捕获器。即安装零件
		t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				System.out.println(t + "发生线程异常啦! 异常信息:" + e.getMessage());
			}
		});
		return t;
	}
}
