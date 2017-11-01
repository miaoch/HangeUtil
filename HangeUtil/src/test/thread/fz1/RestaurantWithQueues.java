package test.thread.fz1;

import java.util.*;
import java.util.concurrent.*;

public class RestaurantWithQueues {
	public static void main(String[] args) throws InterruptedException {
		ExecutorService exec = Executors.newCachedThreadPool();
		Restaurant rest = new Restaurant(exec, 5, 2);//5个小二，两个厨师
		exec.execute(rest);//开业啦
		Thread.sleep(5000);//开十秒就关门了
		exec.shutdownNow();//所有人都赶出去，最可怕的是连厨师小二都不让干活了
	}
}
//可以假想成一份菜单，不过是一份菜的指派菜单
class Order {
	private static int counter = 0;
	private final int id = counter++;
	private final Customer customer;//顾客
	private final WaitPerson waitPerson;//服务的小二
	private final Food food;//所需的菜肴
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
//可以假想成一份成品菜肴，可以吃的那种
class Plate {
	private final Order order;//对应的菜单
	private final Food food;//可以吃的菜肴，这里用枚举类代替
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
//顾客 线程，任务是给小二下单，然后等待小二送来菜肴
class Customer implements Runnable {
	private static int counter = 0;
	private final int id = counter++;
	private final WaitPerson waitPerson;//该服务的小二
	private static Random rand = new Random(47);
	private SynchronousQueue<Plate> plateSetting =
			new SynchronousQueue<Plate>();//只有take和put同时完成后，才能进行下次take()和put()，否则任一操作都会阻塞
	public Customer(WaitPerson wp) {
		waitPerson = wp;
	}
	public void deliver(Plate p) throws InterruptedException {
		plateSetting.put(p);//从小二的手中接过菜肴(put->take 组成一个过程)
	}
	@Override
	public void run() {
		Food[] foods = Food.choose();//顾客随机选取了一组菜肴
		System.out.println(this + "下单：" + Arrays.toString(foods));
		try {
			if (!Thread.interrupted())  {
				for (Food food : foods) {
					waitPerson.placeOrder(this, food);//一个一个上
					System.out.println(this + "eating " + plateSetting.take());//等待直到 从小二的手中接过菜肴(put->take组成一个过程)。
				}
			}
		} catch (InterruptedException e) {
			System.out.println(this + "waiting 中断");
		}
		System.out.println(this + "吃完了");
	}
	public String toString() {
		return "Customer " + id + " ";
	}
}
//小二 线程，任务是拿出已经做好并且是分发给自己的菜肴，然后送给这个顾客
class WaitPerson implements Runnable {
	private static int counter = 0;
	private final int id = counter++;
	private final Restaurant restaurant;
	BlockingQueue<Plate> filledOrders = 
			new LinkedBlockingQueue<Plate>();//小二收到的已经完成的菜肴
	public WaitPerson(Restaurant r) {
		restaurant = r;
	}
	public void placeOrder(Customer cust, Food food) {
		try {
			restaurant.orders.put(new Order(cust, this, food));//添加一份新订单
		} catch (InterruptedException e) {
			System.out.println(this + " placeOrder 中断");
		}
	}
	public void run() {
		try {
			while (!Thread.interrupted()) {//压榨他！不许休息
				Plate plate = filledOrders.take();//拿出包装好的菜肴
				System.out.println(this + "received " + plate 
						+ " delivering to " + plate.getOrder().getCustomer());//准备丢给那个顾客
				plate.getOrder().getCustomer().deliver(plate);//让顾客接收这个菜肴
			}
		} catch (InterruptedException e) {
			System.out.println(this + " 中断");
		}
		System.out.println(this + " 结束");
	}
	public String toString() {
		return "WaitPerson " + id + " ";
	}
}
//厨师 线程，任务是从订单队列中拿出订单(Order)，做完(Plate)并丢给相应的小二
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
			while (!Thread.interrupted()) {//压榨他！不许休息
				Order order = restaurant.orders.take();//厨师从菜单中拿出Order，也就是订单
				Food item = order.item();//知道要做什么菜
				Thread.sleep(rand.nextInt(500));//假设花的时间不定
				Plate plate = new Plate(order, item);//成品菜肴完成了
				order.getWaitPerson().filledOrders.put(plate);//丢给这个小二
			}
		} catch (InterruptedException e) {
			System.out.println(this + " 中断");
		}
		System.out.println(this + " 结束");
	}
	public String toString() {
		return "Chef " + id + " ";
	}
}
//饭店 线程 负责接待顾客，分配小二
class Restaurant implements Runnable {
	private List<WaitPerson> waitPersons = new ArrayList<WaitPerson>();
	private List<Chef> chefs = new ArrayList<Chef>();
	private ExecutorService exec;
	private static Random rand = new Random(47);
	BlockingQueue<Order> orders = 
			new LinkedBlockingQueue<Order>();//相当于订单表
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
			while (!Thread.interrupted()) {//我就是不关门
				WaitPerson wp = waitPersons.get(rand.nextInt(waitPersons.size()));//随机选取一个小二
				Customer c = new Customer(wp);//假设来了一个客户，并把这个客户分给这个小二
				exec.execute(c);//让这个客户自行下单
				Thread.sleep(100);//模拟0.1秒来个客户
			}
		} catch (InterruptedException e) {
			System.out.println("Restaurant 中断");
		}
		System.out.println("Restaurant 结束");
	}
}
enum Food {
	南汇水蜜桃, 三林糖酱瓜, 佘山兰笋, 松江回鳃鲈, 枫泾西蹄, 城隍庙五香豆, 崇明金瓜, 南桥乳腐, 高桥松饼, 嘉定大白蒜;
	private static Random rand = new Random(47);
	public static Food[] choose() {
		Food[] allfoods = values();
		int count = allfoods.length;//10
		for (int i = 0; i < count - 1; i++) { //打乱顺序
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