package test.thread;

import java.util.List;
import java.util.concurrent.*;

public class ExchangerDemo {
	public static void main(String args[]) {
		Exchanger<List<Integer>> changer = new Exchanger<List<Integer>>();
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(new ExchangerProducer(changer));
		exec.execute(new ExchangerConsumer(changer));
		exec.shutdown();
	}
}

class ExchangerProducer implements Runnable {
	//������Ϳ����ڱ�����ʱ��remove����Ϊ�ǽ����������������ߵ�����һ�㶼��������һ��
	private List<Integer> holder = new CopyOnWriteArrayList<>();
	private int begin = 0;
	private Exchanger<List<Integer>> changer;
	public ExchangerProducer(Exchanger<List<Integer>> changer) {
		this.changer = changer;
	}
	@Override	
	public void run() {
		try {
			while (!Thread.interrupted()) {
				for (int i=begin; i<begin+10; i++) {
					holder.add(i);
				}
				begin = begin + 10;
				System.out.println("producer ǰ " + holder);
				holder = changer.exchange(holder);
				System.out.println("producer �� " + holder);
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
class ExchangerConsumer implements Runnable {
	private List<Integer> holder = new CopyOnWriteArrayList<>();//������Ϳ����ڱ�����ʱ��remove
	private Exchanger<List<Integer>> changer;
	public ExchangerConsumer(Exchanger<List<Integer>> changer) {
		this.changer = changer;
	}
	@Override	
	public void run() {
		try {
			while (!Thread.interrupted()) {
				System.out.println("consumer ǰ " + holder);
				holder = changer.exchange(holder);
				System.out.println("consumer �� " + holder);
				for (Integer i : holder) {
					//holder.remove(i);//ע�͵����Կ�����������
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}