import java.util.Random;


public class Test12 {
	private Random rand = new Random();
	int rand7() {  
		return rand.nextInt() % 7 + 1;  
	}  
	 //一个7进制的数 划分成10等分   
	
}

class T {
	int i;
	static int j;
	{
		i = 1;
	}
	static {
		j = 2;
	}
}
