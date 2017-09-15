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
		/*Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				System.out.println(t + "�����߳��쳣��! �쳣��Ϣ:" + e.getMessage());
			}
		});
		ExecutorService exec = Executors.newCachedThreadPool();
		for (int i = 0; i < 50; i++) {
			exec.execute(new TestThread());
		}*/
	}
}
class DealFactory implements ThreadFactory {
	@Override //newThread ��������ˮ��
	public Thread newThread(Runnable r) {
		Thread t = new Thread(r);
		//ͨ��setUncaughtExceptionHandlerΪ�߳�ָ���쳣������������װ���
		t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				System.out.println(t + "�����߳��쳣��! �쳣��Ϣ:" + e.getMessage());
			}
		});
		return t;
	}
}
