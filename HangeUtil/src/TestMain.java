public class TestMain {//继承

	public static void main(String[] args) {
		new Test();
	}
}
class Test extends Father{
	int i = 1;
	Test() {
		//隐藏super(); 第一步
		System.out.println("第三步构造器初始化：i=" + i);
		i = 3;
	}
	{
		System.out.println("第二步字段处初始化：i=" + i);
		i = 2;
	}
}
class Father {
	Father () {
		System.out.println("father!");
	}
}