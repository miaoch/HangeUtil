package test.thread;

public class LiftOff implements Runnable {
	private static int taskCount = 0;
	private final int id;
	private int countDown;
	private long current;
	public LiftOff(int countDown, long time) {
		this.countDown = countDown;
		current = time;
		id = taskCount++;
	}
	private String status() {
		return "#" + id + "(" + 
				(countDown > 0 ? countDown : "Liftoff") + ")";
	}
	@Override
	public void run() {
		Thread.currentThread().setPriority(id);
		while (countDown-- > 0) {
			System.out.println(Thread.currentThread() + status());
			Thread.yield();//建议cpu调度其他线程。
		}
	}
}
