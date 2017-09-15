package test.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LightTest {
	public static void main(String args[]) throws InterruptedException {
		Light light = new Light();
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(new TurnOn(light));
		exec.execute(new TurnOff(light));
		exec.execute(new TurnOff(light));
		exec.execute(new TurnOff(light));
		exec.execute(new TurnOff(light));
		exec.execute(new TurnOff(light));
		exec.execute(new TurnOff(light));
		Thread.sleep(5000);//执行5秒
		exec.shutdownNow();//中断所有线程，如果在sleep之前或者sleep会打印catch中的信息，如果在sleep之后会正常退出循环
		//由于在sleep之后中断的可能性比较小，时间很短暂，所以基本都是会打印catch中的信息
	}
}
class Light {
	private boolean isLight = false;//初始状态为关
	
	public synchronized void turnOn() {
		System.out.println("要开灯啦");
		isLight = true;//开灯
		notifyAll();//提醒其他线程条件条件已经改变了
	}
	
	public synchronized void turnOff() {
		System.out.println("关灯啦");
		isLight = false;//关灯
		notifyAll();//提醒其他线程条件条件已经改变了
	}
	
	public synchronized void waitForOn() throws InterruptedException {
		//注意这里用while而不是if,原因有3
		//1. 可能有多种同样功能的线程在等待锁的释放，当你抢到锁后，也许灯已经被前面的线程关了，此时你应该重新挂起。
		//2. 其他任务对这个线程的条件作出了改变。比如有第三个类型的线程是随机开关灯，如果你抢到锁后，条件又变回去了，你也应该重新挂起。
		//3. 条件根本没有改变，其他线程调用了notifyAll()不过是为了唤醒其他任务，此时你也应该重新挂起。
		while (isLight == false) {
			wait();
		}
	}
	
	public synchronized void waitForOff() throws InterruptedException {
		while (isLight == false) {
			synchronized (this) {
				wait();
			}
		}
	}
	
}
class TurnOn implements Runnable {
	private Light light;
	public TurnOn(Light l) {light = l;}
	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				Thread.sleep(200);
				light.turnOn();//这里是先开灯，因为初始状态是没开灯
				light.waitForOff();
			}
			System.out.println("开灯被中断了 退出循环");
		} catch (InterruptedException e) {
			System.out.println("开灯被中断了 catch");
		}
	}
}
class TurnOff implements Runnable {
	private Light light;
	public TurnOff(Light l) {light = l;}
	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				Thread.sleep(200);
				light.waitForOn();//这里是先等待开灯，因为初始状态就是关灯
				light.turnOff();
			}
			System.out.println("关灯被中断了 退出循环");
		} catch (InterruptedException e) {
			System.out.println("关灯被中断了 catch");
		}
	}
}