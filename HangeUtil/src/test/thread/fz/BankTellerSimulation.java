package test.thread.fz;

import java.util.*;
import java.util.concurrent.*;

public class BankTellerSimulation {
	private static final int MAX_LINE_SIZE = 50;
	private static final int ADJUSTMENT_PERIOD = 1000;
	public static void main(String args[]) throws Exception {
		ExecutorService exec = Executors.newCachedThreadPool();
		CustomerLine customers = new CustomerLine(MAX_LINE_SIZE);
		exec.execute(new CustomerGenerator(customers));
		exec.execute(new TellerManager(exec, customers, ADJUSTMENT_PERIOD));
		System.in.read();
		exec.shutdownNow();
	}
}
//顾客
class Customer {
	private final int serviceTime;//此值模拟用户的服务时间
	public Customer(int st) {
		serviceTime = st;
	}
	public int getServiceTime() {
		return serviceTime;
	}
	public String toString() {
		return "[" + serviceTime + "]";
	}
}
//排号队伍
class CustomerLine extends ArrayBlockingQueue<Customer> {
	public CustomerLine(int capacity) {
		super(capacity);
	}
	public String toString() {
		if (isEmpty()) {
			return "[Empty]";
		}
		StringBuilder result = new StringBuilder();
		for (Customer cus : this) {
			result.append(cus);
		}
		return result.toString();
	}
}
//模拟有顾客来办业务
class CustomerGenerator implements Runnable {
	private static Random random = new Random(47);
	private CustomerLine customers;
	public CustomerGenerator(CustomerLine cl) {
		customers = cl;
	}
	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				Thread.sleep(random.nextInt(300));
				//不定期的来客户,其服务时间也不定
				customers.put(new Customer(random.nextInt(1000)));
			}
		} catch (InterruptedException e) {
			System.out.println("CustomerGenerator 程序中断");
		}
		System.out.println("CustomerGenerator 程序停止");
	}
}
//出纳员服务线程
class Teller implements Runnable, Comparable<Teller>{
	private static int counter = 0;
	private final int id = counter++;
	
	private int customerServed = 0;//已服务人数
	private CustomerLine customers;
	private boolean servingCustomerLine = true;
	public Teller(CustomerLine cl) {
		customers = cl;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				Customer customer = customers.take();//呼叫客户，如果没有则阻塞
				Thread.sleep(customer.getServiceTime());//模拟进行服务
				//在服务期间TellerManager线程可能判断出此时不再需要那么多人了，servingCustomerLine被置为false
				synchronized (this) {
					customerServed++;//服务人数加1
					//如果此时TellerManager线程已经允许我们释放自己，那么我们就wait()。
					//用while是因为唤醒的可能是别人而不是自己
					while (!servingCustomerLine)
						wait();//wait期间TellerManager线程可能又将其设置为true
				}
			}
		} catch (InterruptedException e) {
			System.out.println("Teller 程序中断");
		}
		System.out.println("Teller 程序停止");
		
	}
	//没人来了就等待，或者说去做别的事情去
	public synchronized void doSomethingElse() {
		//customerServed = 0;//书上有这句代码，我觉得没有必要，因为讲道理是可以累积的
		servingCustomerLine = false;//将这个出纳员的状态调整为休息状态
		//吃饭聊天打豆豆...爱干嘛干嘛
	}
	//有人来了就开始服务
	public synchronized void serveCustomerLine() {
		assert !servingCustomerLine : "服务已经在运行！:" + this;
		servingCustomerLine = true;
		notifyAll();
	}
	//用于线程资源合理分配，干活多的出纳员优先出队去干别的事情。比较期间不允许对customerServed进行修改，所以是synchronized的。
	public synchronized int compareTo(Teller o) {
		return customerServed - o.customerServed;
	}
	//出纳员编号
	public String toString() {
		return "T" + id + "(" + customerServed + ")";
	}
}

//出纳员控制系统
class TellerManager implements Runnable {
	private ExecutorService exec;
	private CustomerLine customers;
	//了解PriorityQueue的应该知道，当poll或者offer一个值时，它并不会遍历所有的队列值
	//此处书上使用PriorityQueue是不对的，因为customerServed在中途进行了变化，为了使结果正确我将重写poll方法。
	//private PriorityQueue<Teller> workingTellers = new PriorityQueue<Teller>();
	private PriorityQueue<Teller> workingTellers = new MyPriorityQueue<Teller>();
	//这个队列也不能这么写，也应该选择最小的出来,不过因为这个队列里的customerServed不会变化，所以可以直接使用PriorityQueue
	//其实就算改成PriorityQueue也不是特别严谨，要注意到customerServed++基本上是在放入队列后发生的。不过最多差1位，关系不是很大
	//private Queue<Teller> tellerDoingOhterThings = new LinkedList<Teller>();
	private Queue<Teller> tellerDoingOhterThings = new PriorityQueue<Teller>();
	private int adjustmentPeriod;//调整时间间隔
	public TellerManager(ExecutorService e,
			CustomerLine cl, int adjustmentPeriod) {
		exec = e;
		customers = cl;
		this.adjustmentPeriod = adjustmentPeriod;
		newOneTeller();
	}
	public void adjustTellerNumber() {
		//如果顾客太多了，就增加一个出纳员
		if (customers.size() / workingTellers.size() > 2) {
			if (tellerDoingOhterThings.size() > 0) {
				Teller teller = tellerDoingOhterThings.poll();//选出一个最小值
				teller.serveCustomerLine();//从空闲队列中取出一个出纳员进行服务，必须持有teller的锁
				workingTellers.offer(teller);
				return;
			}
			newOneTeller();
			return;
		}
		//如果工作的人太多了，就释放一个出纳员
		if (workingTellers.size() > 1 && customers.size() / workingTellers.size() < 2) {
			reassingOneTeller();
		}
		//如果没有要服务的客户，则递减到一个出纳员
		if (customers.size() == 0) {
			while (workingTellers.size() > 1) {
				reassingOneTeller();
			}
		}
	}
	//分配一个出纳员
	private void newOneTeller() {
		Teller teller = new Teller(customers);
		exec.execute(teller);
		workingTellers.add(teller);
	}
	//释放一个出纳员
	private void reassingOneTeller() {
		Teller teller = workingTellers.poll();//使用了重写的方法，选出了一个最大值
		teller.doSomethingElse();//必须持有teller的锁
		tellerDoingOhterThings.offer(teller);
	}
	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				Thread.sleep(adjustmentPeriod);
				adjustTellerNumber();//因为只有一个线程，所以不需要加锁
				System.out.print(customers + " {工作中: ");
				for (Teller teller : workingTellers) {
					System.out.print(teller + " ");
				}
				System.out.print(", 休息中: ");
				for (Teller teller : tellerDoingOhterThings) {
					System.out.print(teller + " ");
				}
				System.out.println("}");
			}
		} catch (InterruptedException e) {
			System.out.println("TellerManager 程序中断");
		}
		System.out.println("TellerManager 程序结束");
	}
}

//修改poll方法的优先级队列
class MyPriorityQueue<T> extends PriorityQueue<T> {
	@Override
	public T poll() {
		if (isEmpty()) return null;
		T[] data = (T[]) toArray();
		T max = data[0];
		for (int i = 1; i < data.length; i++) {
			if (((Comparable<T>) max).compareTo(data[i]) < 0) {
				max = data[i];
			}
		}
		remove(max);
		return max;
	}
}



