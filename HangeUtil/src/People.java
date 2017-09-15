
public class People extends Animal {
	private People(){}
	private static People p;
	public static People getInstance() {
		if (p == null) {
			synchronized (People.class) {
				if (p == null) {
					p = new People();
				}
			}
		}
		return p;
	}
}
class Animal {
	String type = "¶¯Îï";
	Animal() {
		run();
	}
	void run() {
		System.out.println("Animal " + type + " run");
	}
}
