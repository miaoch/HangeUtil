public class TestMain {//�̳�

	public static void main(String[] args) {
		new Test();
	}
}
class Test extends Father{
	int i = 1;
	Test() {
		//����super(); ��һ��
		System.out.println("��������������ʼ����i=" + i);
		i = 3;
	}
	{
		System.out.println("�ڶ����ֶδ���ʼ����i=" + i);
		i = 2;
	}
}
class Father {
	Father () {
		System.out.println("father!");
	}
}