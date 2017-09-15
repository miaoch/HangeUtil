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
		Thread.sleep(5000);//ִ��5��
		exec.shutdownNow();//�ж������̣߳������sleep֮ǰ����sleep���ӡcatch�е���Ϣ�������sleep֮��������˳�ѭ��
		//������sleep֮���жϵĿ����ԱȽ�С��ʱ��ܶ��ݣ����Ի������ǻ��ӡcatch�е���Ϣ
	}
}
class Light {
	private boolean isLight = false;//��ʼ״̬Ϊ��
	
	public synchronized void turnOn() {
		System.out.println("Ҫ������");
		isLight = true;//����
		notifyAll();//���������߳����������Ѿ��ı���
	}
	
	public synchronized void turnOff() {
		System.out.println("�ص���");
		isLight = false;//�ص�
		notifyAll();//���������߳����������Ѿ��ı���
	}
	
	public synchronized void waitForOn() throws InterruptedException {
		//ע��������while������if,ԭ����3
		//1. �����ж���ͬ�����ܵ��߳��ڵȴ������ͷţ�������������Ҳ����Ѿ���ǰ����̹߳��ˣ���ʱ��Ӧ�����¹���
		//2. �������������̵߳����������˸ı䡣�����е��������͵��߳���������صƣ�������������������ֱ��ȥ�ˣ���ҲӦ�����¹���
		//3. ��������û�иı䣬�����̵߳�����notifyAll()������Ϊ�˻����������񣬴�ʱ��ҲӦ�����¹���
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
				light.turnOn();//�������ȿ��ƣ���Ϊ��ʼ״̬��û����
				light.waitForOff();
			}
			System.out.println("���Ʊ��ж��� �˳�ѭ��");
		} catch (InterruptedException e) {
			System.out.println("���Ʊ��ж��� catch");
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
				light.waitForOn();//�������ȵȴ����ƣ���Ϊ��ʼ״̬���ǹص�
				light.turnOff();
			}
			System.out.println("�صƱ��ж��� �˳�ѭ��");
		} catch (InterruptedException e) {
			System.out.println("�صƱ��ж��� catch");
		}
	}
}