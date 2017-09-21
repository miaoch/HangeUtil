package test2;

public enum Singleton {  
	INSTANCE;
	public static void main(String[] args) { 
		if (System.out.printf("hello") == null) { 
			System.out.print("hello"); 
		} else { 
			System.out.print("world"); 
		} 
	} 
}