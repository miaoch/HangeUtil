package test.thread;

import java.lang.reflect.Array;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemaphoreDemo {
	private static final int poolsize = 3;
	public static void main(String args[]) throws Exception {
		Pool<TestObj> pool = new Pool<TestObj>(TestObj.class, poolsize);
		ExecutorService exec = Executors.newCachedThreadPool();
		for (int i=0; i<poolsize; i++) {
			exec.execute(new Hold<TestObj>(pool, (i + 1) * 1000));
		} 
		for (int i=0; i<poolsize; i++) {
			exec.execute(new Hold<TestObj>(pool, 500));
		}
		exec.shutdown();
	}
}
class Hold<T> implements Runnable {
	private static int count;
	private final int id = ++count;
	private final Pool<T> pool;
	private final long time;
	public Hold(Pool<T> pool, long time) {
		this.pool = pool;
		this.time = time;
	}
	public void run() {
		try {
			T task = pool.checkOut();
			System.out.println("Thread" + id + " get: " + task);
			Thread.sleep(time);
			System.out.println("Thread" + id + " release: " + task);
			pool.checkIn(task);
			System.out.println("Thread" + id + " release success!");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
class TestObj {
	private static int count;
	private final int id = ++count;
	public int getId() {
		return id;
	}
	public String toString() {
		return "resourceId:" + id;
	}
}
class Pool<T> {
	private Semaphore available;
	private int size;
	private T[] data;
	private boolean[] checkOut;
	
	@SuppressWarnings("unchecked")
	public Pool(Class<T> clazz, int size) throws InstantiationException, IllegalAccessException {
		available = new Semaphore(size, true);
		this.size = size;
		checkOut = new boolean[size];
		data = (T[]) Array.newInstance(clazz, size);
		for (int i=0; i<size; i++) {
			data[i] = clazz.newInstance();
		}
	}
	
	public T checkOut() throws InterruptedException {
		available.acquire();//请求一个许可证，如果没有则等待。
		return getItem();
	}
	
	public boolean checkIn(T x) throws InterruptedException {
		if (releaseItem(x)) {
			available.release();//释放一个许可证
			return true;
		}
		return false;
	}

	private synchronized T getItem() {
		for (int i=0; i<size; i++) {
			if (!checkOut[i]) {
				checkOut[i] = true;
				return (T) data[i];
			}
		}
		return null;
	}
	
	private synchronized boolean releaseItem(T x) {
		for (int i=0; i<size; i++) {
			if (data[i] == x) {
				if (checkOut[i]) {
					checkOut[i] = false;
					return true;
				}
				return false;
			}
		}
		return false;
	}
}