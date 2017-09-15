package test.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadLocalTest {
	private static ThreadLocal<Integer> value = new ThreadLocal<Integer>() {
		protected synchronized Integer initialValue() {
			return 0;
		}
	};
	private static ThreadLocal<MyClass> myClass = new ThreadLocal<MyClass>() {
		protected synchronized MyClass initialValue() {
			return new MyClass();
		}
	};
	public static void main(String args[]) {
		//使用CachedThreadPool可能导致线程重用而出现值递增
		//ExecutorService exec = Executors.newCachedThreadPool();
		ExecutorService exec = Executors.newFixedThreadPool(5);
		for (int i = 0; i < 10; i++) {//线程数量不超过10，避免线程重用
			exec.execute(new Runnable() {
				@Override
				public void run() {
					int i = 0;
					while (i++ < 1000) {
						value.set(value.get() + 1 );
						MyClass mc = myClass.get();
						mc.i = --mc.i;
						mc.i = --mc.i;
					}
					System.out.println(Thread.currentThread() + " " + value.get() + "  " + myClass.get().i);
				}
			});
		}
	}
}
class MyClass {
	int i;
	String s;
}