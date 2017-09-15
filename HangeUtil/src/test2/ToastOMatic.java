package test2;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

class ToastQueue extends LinkedBlockingQueue<Toast> {}
public class ToastOMatic {
	public static void main(String args[]) throws Exception {
		ToastQueue dryQueue = new ToastQueue(),
				butteredQueue = new ToastQueue(),
				finishedQueue = new ToastQueue();
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(new Toaster(dryQueue));
		exec.execute(new Jammer(butteredQueue, finishedQueue));
		exec.execute(new Butterer(dryQueue, butteredQueue));
		Thread.sleep(5000);
		exec.shutdownNow();//�ж������߳�
	}
}
class Toast {
	public enum Status {
		DRY, BUTTERED, JAMMED
	}
	private Status status = Status.DRY;
	private final int id;
	public Toast(int id) {
		this.id = id;
	}
	public void butter() {
		status = Status.BUTTERED;
	}
	public void jam() {
		status = Status.JAMMED;
	}
	public Status getStatus() {
		return status;
	}
	public int getId() {
		return id;
	}
	public String toString() {
		return "Toast " + id + ": " + status;
	}
}
//������˾������
class Toaster implements Runnable {
	private ToastQueue dryQueue;
	private int count = 0;
	private Random random = new Random(47);
	public Toaster(ToastQueue tq) {
		dryQueue = tq;
	}
	public void run() {
		try {
			while (!Thread.interrupted()) {
				Thread.sleep(100 + random.nextInt(500));
				Toast t = new Toast(count++);
				System.out.println(t);
				dryQueue.put(t);//���ܻ�await() ���ܻ�signal()
			}
		} catch (InterruptedException e) {
			System.out.println("Toaster Interrupted");
		}
		System.out.println("Toaster off");
	}
}
//ˢ���͵�����
class Butterer implements Runnable {
	private ToastQueue dryQueue, butteredQueue;//�Ӹ���Ķ����У��ó��������Ѿ�ˢ�û��͵Ķ�����
	private Random random = new Random(47);
	public Butterer(ToastQueue dry, ToastQueue buttered) {
		dryQueue = dry;
		butteredQueue = buttered;
	}
	public void run() {
		try {
			while (!Thread.interrupted()) {
				Toast t = dryQueue.take();//���ܻ�await() ���ܻ�signal()
				t.butter();
				System.out.println(t);
				butteredQueue.put(t);//���ܻ�await() ���ܻ�signal()
			}
		} catch (InterruptedException e) {
			System.out.println("Butterer Interrupted");
		}
		System.out.println("Butterer off");
	}
}
//ˢ����������
class Jammer implements Runnable {
	private ToastQueue butteredQueue, finishedQueue;//��ˢ�û��͵Ķ����У��ó��������Ѿ���Ʒ�Ķ�����
	private Random random = new Random(47);
	public Jammer(ToastQueue buttered, ToastQueue finished) {
		finishedQueue = finished;
		butteredQueue = buttered;
	}
	public void run() {
		try {
			while (!Thread.interrupted()) {
				Toast t = butteredQueue.take();//���ܻ�await() ���ܻ�signal()
				t.jam();
				System.out.println(t);
				finishedQueue.put(t);//���ܻ�await() ���ܻ�signal()
			}
		} catch (InterruptedException e) {
			System.out.println("Butterer Interrupted");
		}
		System.out.println("Butterer off");
	}
}










