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
	//��ߵĲ��������Ǹ��࣬���Խ�������͸���
	public static void  f(B b){
		System.out.println("����");
	}
	//��ߵĲ���������
	public static void  f( A a){
		System.out.println("����");
	}


}
class A extends B{
}
class B {
}