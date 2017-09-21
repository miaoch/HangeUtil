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
	//这个类型可以在遍历的时候，remove。因为是交换对象，所以生产者的类型一般都和消费者一致
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
				System.out.println("producer 前 " + holder);
				holder = changer.exchange(holder);
				System.out.println("producer 后 " + holder);
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
class ExchangerConsumer implements Runnable {
	private List<Integer> holder = new CopyOnWriteArrayList<>();//这个类型可以在遍历的时候，remove
	private Exchanger<List<Integer>> changer;
	public ExchangerConsumer(Exchanger<List<Integer>> changer) {
		this.changer = changer;
	}
	@Override	
	public void run() {
		try {
			while (!Thread.interrupted()) {
				System.out.println("consumer 前 " + holder);
				holder = changer.exchange(holder);
				System.out.println("consumer 后 " + holder);
				for (Integer i : holder) {
					//holder.remove(i);//注释掉可以看到交换场景
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}