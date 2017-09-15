package test.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Interrupting2 {
	public static void main(String args[]) throws InterruptedException {
		final BlockedMutex b = new BlockedMutex();
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("�߳�������");
				b.f();
				System.out.println("�߳�ִ�����");
			}
		});
		t.start();
		Thread.sleep(1000);
		System.out.println("�����ж�");
		t.interrupt();//�ֶ��ж�
	}
}
class BlockedMutex {
	private Lock lock = new ReentrantLock();//������ǿ��ñ��жϵ�
	
	public BlockedMutex() {
		lock.lock();//���ü���
	}
	public void f() {
		try {
			lock.lockInterruptibly();//lockInterruptibly���Ա��ж�
			System.out.println("�õ����ˣ���ʵ���ﲻ���ܱ�ִ��");
		} catch (InterruptedException e) {
			System.out.println("���ж���");
		}
	}
}