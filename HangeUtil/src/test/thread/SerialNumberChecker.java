package test.thread;

public class SerialNumberChecker {
	private static CircularSet set = new CircularSet(1000);
	public static void main(String args[]) {
		for (int i = 0; i < 10; i++) {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						int serial = SerialNumberGenerator.nextSerialNumber();
						if (set.contain(serial)) {
							System.out.println("Dulpicate: " + serial);
							System.exit(0);
						}
						set.add(serial);
					}
				}
			}).start();
		}
	}
}
class SerialNumberGenerator {
	private static volatile int serialNumber = 0;
	
	public synchronized static int nextSerialNumber() {
		return serialNumber++;
	}
}
class CircularSet {
	private int[] array;
	private int len;
	private int index = 0;
	
	public CircularSet(int size) {
		len = size;
		array = new int[size];
		for (int i = 0; i < size; i++) {
			array[i] = -1;
		}
	}
	
	public synchronized void add(int i) {
		array[index] = i;
		index = ++index % len;
	}
	
	public synchronized boolean contain(int val) {
		for (int i = 0; i < len; i++) {
			if (array[i] == val) return true;
		}
		return false;
	}
}