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
//�˿�
class Customer {
	private final int serviceTime;//��ֵģ���û��ķ���ʱ��
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
//�źŶ���
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
//ģ���й˿�����ҵ��
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
				//�����ڵ����ͻ�,�����ʱ��Ҳ����
				customers.put(new Customer(random.nextInt(1000)));
			}
		} catch (InterruptedException e) {
			System.out.println("CustomerGenerator �����ж�");
		}
		System.out.println("CustomerGenerator ����ֹͣ");
	}
}
//����Ա�����߳�
class Teller implements Runnable, Comparable<Teller>{
	private static int counter = 0;
	private final int id = counter++;
	
	private int customerServed = 0;//�ѷ�������
	private CustomerLine customers;
	private boolean servingCustomerLine = true;
	public Teller(CustomerLine cl) {
		customers = cl;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				Customer customer = customers.take();//���пͻ������û��������
				Thread.sleep(customer.getServiceTime());//ģ����з���
				//�ڷ����ڼ�TellerManager�߳̿����жϳ���ʱ������Ҫ��ô�����ˣ�servingCustomerLine����Ϊfalse
				synchronized (this) {
					customerServed++;//����������1
					//�����ʱTellerManager�߳��Ѿ����������ͷ��Լ�����ô���Ǿ�wait()��
					//��while����Ϊ���ѵĿ����Ǳ��˶������Լ�
					while (!servingCustomerLine)
						wait();//wait�ڼ�TellerManager�߳̿����ֽ�������Ϊtrue
				}
			}
		} catch (InterruptedException e) {
			System.out.println("Teller �����ж�");
		}
		System.out.println("Teller ����ֹͣ");
		
	}
	//û�����˾͵ȴ�������˵ȥ���������ȥ
	public synchronized void doSomethingElse() {
		//customerServed = 0;//�����������룬�Ҿ���û�б�Ҫ����Ϊ�������ǿ����ۻ���
		servingCustomerLine = false;//���������Ա��״̬����Ϊ��Ϣ״̬
		//�Է�����򶹶�...���������
	}
	//�������˾Ϳ�ʼ����
	public synchronized void serveCustomerLine() {
		assert !servingCustomerLine : "�����Ѿ������У�:" + this;
		servingCustomerLine = true;
		notifyAll();
	}
	//�����߳���Դ������䣬�ɻ��ĳ���Ա���ȳ���ȥ�ɱ�����顣�Ƚ��ڼ䲻�����customerServed�����޸ģ�������synchronized�ġ�
	public synchronized int compareTo(Teller o) {
		return customerServed - o.customerServed;
	}
	//����Ա���
	public String toString() {
		return "T" + id + "(" + customerServed + ")";
	}
}

//����Ա����ϵͳ
class TellerManager implements Runnable {
	private ExecutorService exec;
	private CustomerLine customers;
	//�˽�PriorityQueue��Ӧ��֪������poll����offerһ��ֵʱ����������������еĶ���ֵ
	//�˴�����ʹ��PriorityQueue�ǲ��Եģ���ΪcustomerServed����;�����˱仯��Ϊ��ʹ�����ȷ�ҽ���дpoll������
	//private PriorityQueue<Teller> workingTellers = new PriorityQueue<Teller>();
	private PriorityQueue<Teller> workingTellers = new MyPriorityQueue<Teller>();
	//�������Ҳ������ôд��ҲӦ��ѡ����С�ĳ���,������Ϊ����������customerServed����仯�����Կ���ֱ��ʹ��PriorityQueue
	//��ʵ����ĳ�PriorityQueueҲ�����ر��Ͻ���Ҫע�⵽customerServed++���������ڷ�����к����ġ���������1λ����ϵ���Ǻܴ�
	//private Queue<Teller> tellerDoingOhterThings = new LinkedList<Teller>();
	private Queue<Teller> tellerDoingOhterThings = new PriorityQueue<Teller>();
	private int adjustmentPeriod;//����ʱ����
	public TellerManager(ExecutorService e,
			CustomerLine cl, int adjustmentPeriod) {
		exec = e;
		customers = cl;
		this.adjustmentPeriod = adjustmentPeriod;
		newOneTeller();
	}
	public void adjustTellerNumber() {
		//����˿�̫���ˣ�������һ������Ա
		if (customers.size() / workingTellers.size() > 2) {
			if (tellerDoingOhterThings.size() > 0) {
				Teller teller = tellerDoingOhterThings.poll();//ѡ��һ����Сֵ
				teller.serveCustomerLine();//�ӿ��ж�����ȡ��һ������Ա���з��񣬱������teller����
				workingTellers.offer(teller);
				return;
			}
			newOneTeller();
			return;
		}
		//�����������̫���ˣ����ͷ�һ������Ա
		if (workingTellers.size() > 1 && customers.size() / workingTellers.size() < 2) {
			reassingOneTeller();
		}
		//���û��Ҫ����Ŀͻ�����ݼ���һ������Ա
		if (customers.size() == 0) {
			while (workingTellers.size() > 1) {
				reassingOneTeller();
			}
		}
	}
	//����һ������Ա
	private void newOneTeller() {
		Teller teller = new Teller(customers);
		exec.execute(teller);
		workingTellers.add(teller);
	}
	//�ͷ�һ������Ա
	private void reassingOneTeller() {
		Teller teller = workingTellers.poll();//ʹ������д�ķ�����ѡ����һ�����ֵ
		teller.doSomethingElse();//�������teller����
		tellerDoingOhterThings.offer(teller);
	}
	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				Thread.sleep(adjustmentPeriod);
				adjustTellerNumber();//��Ϊֻ��һ���̣߳����Բ���Ҫ����
				System.out.print(customers + " {������: ");
				for (Teller teller : workingTellers) {
					System.out.print(teller + " ");
				}
				System.out.print(", ��Ϣ��: ");
				for (Teller teller : tellerDoingOhterThings) {
					System.out.print(teller + " ");
				}
				System.out.println("}");
			}
		} catch (InterruptedException e) {
			System.out.println("TellerManager �����ж�");
		}
		System.out.println("TellerManager �������");
	}
}

//�޸�poll���������ȼ�����
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



