package test.thread;


public class MyTest {
	private int i;
	public /*synchronized*/ int getI() {
		return i;
	}
	public synchronized void test() {
		i++;
		i++;
	}
	
	public static void main(String args[]) throws Exception {
		final MyTest test = new MyTest();
		new Thread() {
			public void run() {
				while (true) {
					int ii = test.getI();
					if (ii % 2 != 0) {
						System.out.println(ii);
						System.exit(0);
					}
				}
			}
		}.start();
		while (true) {
			test.test();
		}
	}
}
