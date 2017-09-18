package test2;

public class Test{
	public static void main(String[] args){
		B b=new A();
		B b2=new B();
		A a2=new A();
		f(b);
		f(b2);
		f(a2);
	}
	//这边的参数可以是父类，可以接受子类和父类
	public static void  f(B b){
		System.out.println("父类");
	}
	//这边的参数是子类
	public static void  f( A a){
		System.out.println("子类");
	}


}
class A extends B{
}
class B {
}