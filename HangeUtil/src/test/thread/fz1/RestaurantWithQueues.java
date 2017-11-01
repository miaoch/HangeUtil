package test.thread.fz1;

import java.util.*;
import java.util.concurrent.*;

public class RestaurantWithQueues {
	public static void main(String[] args) throws InterruptedException {
		ExecutorService exec = Executors.newCachedThreadPool();
		Restaurant rest = new Restaurant(exec, 5, 2);//5��С����������ʦ
		exec.execute(rest);//��ҵ��
		Thread.sleep(5000);//��ʮ��͹�����
		exec.shutdownNow();//�����˶��ϳ�ȥ������µ�������ʦС�������øɻ���
	}
}
//���Լ����һ�ݲ˵���������һ�ݲ˵�ָ�ɲ˵�
class Order {
	private static int counter = 0;
	private final int id = counter++;
	private final Customer customer;//�˿�
	private final WaitPerson waitPerson;//�����С��
	private final Food food;//����Ĳ���
	public Order(Customer customer, WaitPerson waitPerson, Food food) {
		this.customer = customer;
		this.waitPerson = waitPerson;
		this.food = food;
	}
	public Food item() {
		return food;
	}
	public Customer getCustomer() {
		return customer;
	}
	public WaitPerson getWaitPerson() {
		return waitPerson;
	}
	public String toString() {
		return "Order: " + id + " item: " + food + 
				" for: " + customer + " served by: " + waitPerson;
	}
}
//���Լ����һ�ݳ�Ʒ���ȣ����ԳԵ�����
class Plate {
	private final Order order;//��Ӧ�Ĳ˵�
	private final Food food;//���ԳԵĲ��ȣ�������ö�������
	public Plate(Order order, Food food) {
		this.order = order;
		this.food = food;
	}
	public Order getOrder() {
		return order;
	}
	public Food getFood() {
		return food;
	}
	public String toString() {
		return food.toString();
	}
}
//�˿� �̣߳������Ǹ�С���µ���Ȼ��ȴ�С����������
class Customer implements Runnable {
	private static int counter = 0;
	private final int id = counter++;
	private final WaitPerson waitPerson;//�÷����С��
	private static Random rand = new Random(47);
	private SynchronousQueue<Plate> plateSetting =
			new SynchronousQueue<Plate>();//ֻ��take��putͬʱ��ɺ󣬲��ܽ����´�take()��put()��������һ������������
	public Customer(WaitPerson wp) {
		waitPerson = wp;
	}
	public void deliver(Plate p) throws InterruptedException {
		plateSetting.put(p);//��С�������нӹ�����(put->take ���һ������)
	}
	@Override
	public void run() {
		Food[] foods = Food.choose();//�˿����ѡȡ��һ�����
		System.out.println(this + "�µ���" + Arrays.toString(foods));
		try {
			if (!Thread.interrupted())  {
				for (Food food : foods) {
					waitPerson.placeOrder(this, food);//һ��һ����
					System.out.println(this + "eating " + plateSetting.take());//�ȴ�ֱ�� ��С�������нӹ�����(put->take���һ������)��
				}
			}
		} catch (InterruptedException e) {
			System.out.println(this + "waiting �ж�");
		}
		System.out.println(this + "������");
	}
	public String toString() {
		return "Customer " + id + " ";
	}
}
//С�� �̣߳��������ó��Ѿ����ò����Ƿַ����Լ��Ĳ��ȣ�Ȼ���͸�����˿�
class WaitPerson implements Runnable {
	private static int counter = 0;
	private final int id = counter++;
	private final Restaurant restaurant;
	BlockingQueue<Plate> filledOrders = 
			new LinkedBlockingQueue<Plate>();//С���յ����Ѿ���ɵĲ���
	public WaitPerson(Restaurant r) {
		restaurant = r;
	}
	public void placeOrder(Customer cust, Food food) {
		try {
			restaurant.orders.put(new Order(cust, this, food));//���һ���¶���
		} catch (InterruptedException e) {
			System.out.println(this + " placeOrder �ж�");
		}
	}
	public void run() {
		try {
			while (!Thread.interrupted()) {//ѹե����������Ϣ
				Plate plate = filledOrders.take();//�ó���װ�õĲ���
				System.out.println(this + "received " + plate 
						+ " delivering to " + plate.getOrder().getCustomer());//׼�������Ǹ��˿�
				plate.getOrder().getCustomer().deliver(plate);//�ù˿ͽ����������
			}
		} catch (InterruptedException e) {
			System.out.println(this + " �ж�");
		}
		System.out.println(this + " ����");
	}
	public String toString() {
		return "WaitPerson " + id + " ";
	}
}
//��ʦ �̣߳������ǴӶ����������ó�����(Order)������(Plate)��������Ӧ��С��
class Chef implements Runnable {
	private static int counter = 0;
	private final int id = counter++;
	private final Restaurant restaurant;
	private static Random rand = new Random(47);
	public Chef(Restaurant r) {
		restaurant = r;
	}
		
	public void run() {
		try {
			while (!Thread.interrupted()) {//ѹե����������Ϣ
				Order order = restaurant.orders.take();//��ʦ�Ӳ˵����ó�Order��Ҳ���Ƕ���
				Food item = order.item();//֪��Ҫ��ʲô��
				Thread.sleep(rand.nextInt(500));//���軨��ʱ�䲻��
				Plate plate = new Plate(order, item);//��Ʒ���������
				order.getWaitPerson().filledOrders.put(plate);//�������С��
			}
		} catch (InterruptedException e) {
			System.out.println(this + " �ж�");
		}
		System.out.println(this + " ����");
	}
	public String toString() {
		return "Chef " + id + " ";
	}
}
//���� �߳� ����Ӵ��˿ͣ�����С��
class Restaurant implements Runnable {
	private List<WaitPerson> waitPersons = new ArrayList<WaitPerson>();
	private List<Chef> chefs = new ArrayList<Chef>();
	private ExecutorService exec;
	private static Random rand = new Random(47);
	BlockingQueue<Order> orders = 
			new LinkedBlockingQueue<Order>();//�൱�ڶ�����
	public Restaurant(ExecutorService exec, int nWaitPersons, int nChefs) {
		this.exec = exec;
		for (int i = 0; i < nWaitPersons; i++) {
			WaitPerson waiter = new WaitPerson(this);
			waitPersons.add(waiter);
			exec.execute(waiter);
		}
		for (int i = 0; i < nChefs; i++) {
			Chef chef = new Chef(this);
			chefs.add(chef);
			exec.execute(chef);
		}
	}
	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {//�Ҿ��ǲ�����
				WaitPerson wp = waitPersons.get(rand.nextInt(waitPersons.size()));//���ѡȡһ��С��
				Customer c = new Customer(wp);//��������һ���ͻ�����������ͻ��ָ����С��
				exec.execute(c);//������ͻ������µ�
				Thread.sleep(100);//ģ��0.1�������ͻ�
			}
		} catch (InterruptedException e) {
			System.out.println("Restaurant �ж�");
		}
		System.out.println("Restaurant ����");
	}
}
enum Food {
	�ϻ�ˮ����, �����ǽ���, ��ɽ����, �ɽ�������, ��������, ���������㶹, �������, �����鸯, �����ɱ�, �ζ������;
	private static Random rand = new Random(47);
	public static Food[] choose() {
		Food[] allfoods = values();
		int count = allfoods.length;//10
		for (int i = 0; i < count - 1; i++) { //����˳��
			int index = rand.nextInt(count);
			Food temp = allfoods[i];
			allfoods[i] = allfoods[index];
			allfoods[index] = temp;
		}
		int need = rand.nextInt(allfoods.length / 2) + 1;
		Food[] foods = new Food[need];
		int beginIndex = rand.nextInt(count - need + 1);
		System.arraycopy(allfoods, beginIndex, foods, 0, need);
		return foods;
	}
}