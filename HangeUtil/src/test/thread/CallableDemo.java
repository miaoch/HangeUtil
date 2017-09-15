package test.thread;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class TaskWithResult implements Callable<String> {
	private static int idx;
	private int id = idx++;
	@Override
	public String call() {
		return "#" + id;
	}
}
public class CallableDemo {
	public static void main(String args[]) throws IOException {
		ExecutorService exec = Executors.newCachedThreadPool();
		Future<String> future = exec.submit(new TaskWithResult());
		exec.shutdown();
		try {
			System.out.println(future.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		System.in.read();
	}
}
