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
		}*///ʵ��Ч�������� ������������������
		boolean captured = lock.tryLock();
		try {
			System.out.println("tryLock()��" + captured);
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
			System.out.println("tryLock(2, TimeUnit.SECONDS)��" + captured);
			if (captured) {
				lock.unlock();
			}
		}
	}
	
	public static void main(String args[]) {
		final AttemptLocking at = new AttemptLocking();
		at.test();
		new Thread() {
			{setDaemon(true);}//�Ǻ�̨�߳�
			public void run() {
				at.lock.lock();//���� 
				System.out.println("����������");//��һ�������ֱ��������
			}
		}.start();
		Thread.yield();//�����߳�һ������ ����Ҳ����һ�����������
		at.test();
	}
}
