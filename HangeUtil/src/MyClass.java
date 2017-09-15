import java.util.Arrays;
public class MyClass implements RecLabInterface {

	public static void main(String args[]) {
		MyClass my = new MyClass();
		
		int[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		my.oddBeforeEven(array);
		System.out.println(Arrays.toString(array));
		
		int size = 10;
		my.printTriangle(size);
	}
	
	@Override
	public void printTriangle(int size) {
		StringBuffer triangle = getTriangle(size, size);
		System.out.println(triangle);
	}

	@Override
	public void oddBeforeEven(int[] anArray) {
		loop(0, anArray.length - 1, anArray);
	}

	/**
	 * ����line��count�Ӻ���ǰ��װ�������
	 * �統line=10��count=9����ζ��������������10�У�Ŀǰ������װ��9�С�
	 * ����getLine()�����ǻ�ȡ��ĳ�С��ȼ�������������ؽ������ȷ�ģ������������ʵ�֡�����ʵ�ֿ���һ��������
	 * ���count=1,���ص�1�еĽ�����ݹ������
	 * ���count��Ϊ1����ô�ͷ���getTriangle(line, --count).append(thisline);
	 * Ҳ������ѧ���ɷ�һ������ȡn�е������� Ҳ����n-1�������μ��ϵ�n��
	 * Ȼ��n-1��������Ҳ����n-2�������μ��ϵ�n-1��...ֱ��nΪ1
	 * 
	 * @param line ������ �������ñ仯
	 * @param count �ڼ���
	 * @return
	 */
	private StringBuffer getTriangle(final int line, int count) {
		StringBuffer thisline = getLine(count, line - count).append("\n");
		if (count > 1) {
			return getTriangle(line, --count).append(thisline);
		} else {
			return thisline;
		}
	}
	
	/**
	 * �۲�õ���n�� ��count�е��ַ������������ԣ�
	 * ����n-count���Ŀո����" "
	 * ����count�����Ǻżӿո�"* "
	 * ��Ϊ����ʹ��ѭ����䣬ʹ�õݹ����½��ͣ�
	 * getLine(asterisk, space)���space��Ϊ0����ƴ�ӿո����" "��space�Լ���Ȼ���������getLineֱ��spaceΪ��
	 * �������asterisk��Ϊ0����ƴ�ӿո����"* "��asterisk�Լ���Ȼ���������getLineֱ��spaceΪ��
	 * ���asterisk��space����0���򷵻�һ�����ַ���(��Ϊ��ʹ�õ���StringBuffer��������new StringBuffer())
	 * ���Ե�����������õ��Ľ�����£�����asteriskΪ3 spaceΪ4
	 * �õ�������new StringBuffer().insert(0, " ").insert(0, " ").insert(0, " ").insert(0, " ")
	 * 					.insert(0, "* ").insert(0, "* ").insert(0, "* ");
	 * @param asterisk �Ǻŵ�����
	 * @param space �ո������
	 * @return
	 */
	private StringBuffer getLine(int asterisk, int space) {
		if (asterisk > 0) {
			return getLine(--asterisk, space).append("* ");
		} else if (space > 0) {
			return getLine(asterisk, --space).append(" ");
		} else {
			return new StringBuffer();
		}
	}

	//���������±꣬һ����ǰɨ�� һ�����ɨ��
	private void loop(int start, int end, int[] array) {
		if (start >= end) {//�±���ײ �ݹ����
			return;
		} else if (array[end] % 2 == 0) {//��end��ǰɨ�裬�����ż�� ������ǰɨ��
			loop(start, --end, array);
		} else {//��end��ǰɨ�裬��������� ��start���ɨ��
			if (array[start] % 2 == 0) {//���startɨ�赽ż�������滻�����±��ֵ���ټ���ɨ��
				int temp = array[end];
				array[end] = array[start];
				array[start] = temp;
				loop(++start, --end, array);
			} else {//���startɨ����������������ɨ��
				loop(++start, end, array);
			}
		}
	}
}
