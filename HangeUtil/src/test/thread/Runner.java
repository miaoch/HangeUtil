package test.thread;

public class Runner implements Runnable {
	
	private static int i = 1;
	private int id = i++;
	public void run() {
		throw new RuntimeException(id + "");
	}
	
	public static void main(String args[]) {
		try {
			new Thread(new Runner()).start();
			new Thread(new Runner()).start();
		} catch(Exception e) {
			System.out.println("error!");
		}
	}
}
