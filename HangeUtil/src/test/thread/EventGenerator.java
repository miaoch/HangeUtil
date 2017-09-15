package test.thread;

import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

abstract class IntGenerator {
	//volatile关键字保持可视性。这个后面会讨论
	//简单理解就是避免编译器优化：直接在该线程中保存了该值的副本，而不考虑其他线程改变了这个值。
	//所以volatile每次都会去初始对象中去查找。效率不高就是它的代价。
	private volatile boolean canceled;//默认false
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
	private int currentValue;//默认0
	private Lock lock = new ReentrantLock();
	@Override
	public int next() {
		lock.lock();//如果已经被人抢走了，就会阻塞直到别的线程unlock()
		try {
			currentValue++;
			Thread.yield();
			currentValue++;
			return currentValue;
		} finally {
			lock.unlock();
			//必须在return的临界点解锁。否则依旧会导致错误
			//错误原因解析：如果执行unlock()后没有马上返回，此时被线程2抢去了锁修改了currentValue的值
			//然后线程1再返回currentValue就可能出现奇数
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
