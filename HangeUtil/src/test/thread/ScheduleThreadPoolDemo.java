package test.thread;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ScheduleThreadPoolDemo {
	private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss SSS");
	public static void main(String args[]) {
		ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(5);
		exec.schedule(new Mission(), 1000, TimeUnit.MILLISECONDS);
		//����Ǹ�����һ�ο�ʼʱ��ȥ�ӳ�5000����
		exec.scheduleAtFixedRate(new Mission(), 2000, 1000, TimeUnit.MILLISECONDS);
		//����Ǹ�����һ�ν���ʱ��ȥ�ӳ�5000����
		exec.scheduleWithFixedDelay(new Mission(), 3000, 1000, TimeUnit.MILLISECONDS);
		
		//�÷�һ�� ��ʵ��ͬһ�ֶ���
		//ScheduledExecutorService exec2 = Executors.newScheduledThreadPool(5);
		//exec2.schedule(command, delay, unit)
		//exec2.scheduleAtFixedRate(command, initialDelay, period, unit)
		//exec2.scheduleWithFixedDelay(command, initialDelay, delay, unit)
	}
	static class Mission implements Runnable {
		private static int count = 0;
		private final int id = ++count;
		@Override
		public void run() {
			System.out.println(id + " run in:" + 
					sdf.format(new Date(System.currentTimeMillis())));
		}
	}
}
