package test.thread;

import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

abstract class IntGenerator {
	//volatile�ؼ��ֱ��ֿ����ԡ�������������
	//�������Ǳ���������Ż���ֱ���ڸ��߳��б����˸�ֵ�ĸ������������������̸߳ı������ֵ��
	//����volatileÿ�ζ���ȥ��ʼ������ȥ���ҡ�Ч�ʲ��߾������Ĵ��ۡ�
	private volatile boolean canceled;//Ĭ��false
	public abstract int next();
	public void cancel() {canceled = true;}
	public boolean isCanceled() {return canceled;}
}
class EventChecker implements Runnable {
	private IntGenerator generator;
	public EventChecker(IntGenerator generator) {
		this.generator = generator;
	}
	@Override
	public void run() {
		while (!generator.isCanceled()) {
			int i = generator.next();
			if (i % 2 != 0) {
				System.out.printf("%d is not even\n", i);
				generator.cancel();
			}
		}
	}
}
public class EventGenerator extends IntGenerator {
	private int currentValue;//Ĭ��0
	private Lock lock = new ReentrantLock();
	@Override
	public int next() {
		lock.lock();//����Ѿ����������ˣ��ͻ�����ֱ������߳�unlock()
		try {
			currentValue++;
			Thread.yield();
			currentValue++;
			return currentValue;
		} finally {
			lock.unlock();
			//������return���ٽ��������������ɻᵼ�´���
			//����ԭ����������ִ��unlock()��û�����Ϸ��أ���ʱ���߳�2��ȥ�����޸���currentValue��ֵ
			//Ȼ���߳�1�ٷ���currentValue�Ϳ��ܳ�������
		}
	}
	public static void main(String args[]) {
		EventGenerator generator = new EventGenerator();
		ExecutorService exec = Executors.newCachedThreadPool();
		for (int i = 0; i < 10; i++) {
			exec.execute(new Thread(new EventChecker(generator)));
		}
		exec.shutdown();
	}
}
