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
	 * 利用line和count从后向前组装最后结果。
	 * 如当line=10，count=9，意味着整个三角形有10行，目前正在组装第9行。
	 * 其中getLine()函数是获取第某行。先假设这个方法返回结果是正确的，不管它是如何实现。具体实现看下一个方法。
	 * 如果count=1,返回第1行的结果，递归结束。
	 * 如果count不为1，那么就返回getTriangle(line, --count).append(thisline);
	 * 也就是数学归纳法一样，获取n行的三角形 也就是n-1的三角形加上第n行
	 * 然后n-1的三角形也就是n-2的三角形加上第n-1行...直到n为1
	 * 
	 * @param line 总行数 常量不用变化
	 * @param count 第几行
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
	 * 观察得到共n行 第count行的字符串有如下特性：
	 * 先有n-count个的空格符号" "
	 * 再有count个的星号加空格"* "
	 * 因为不能使用循环语句，使用递归如下解释：
	 * getLine(asterisk, space)如果space不为0，先拼接空格符号" "，space自减，然后继续调用getLine直到space为空
	 * 接着如果asterisk不为0，先拼接空格符号"* "，asterisk自减，然后继续调用getLine直到space为空
	 * 如果asterisk和space都是0，则返回一个空字符串(因为我使用的是StringBuffer，所以是new StringBuffer())
	 * 所以调用这个函数得到的结果如下：假设asterisk为3 space为4
	 * 得到语句就是new StringBuffer().insert(0, " ").insert(0, " ").insert(0, " ").insert(0, " ")
	 * 					.insert(0, "* ").insert(0, "* ").insert(0, "* ");
	 * @param asterisk 星号的数量
	 * @param space 空格的数量
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

	//设置两个下标，一个向前扫描 一个向后扫描
	private void loop(int start, int end, int[] array) {
		if (start >= end) {//下标碰撞 递归结束
			return;
		} else if (array[end] % 2 == 0) {//先end向前扫描，如果是偶数 继续向前扫描
			loop(start, --end, array);
		} else {//先end向前扫描，如果是奇数 再start向后扫描
			if (array[start] % 2 == 0) {//如果start扫描到偶数，则替换两个下标的值，再继续扫描
				int temp = array[end];
				array[end] = array[start];
				array[start] = temp;
				loop(++start, --end, array);
			} else {//如果start扫描的奇数，继续向后扫描
				loop(++start, end, array);
			}
		}
	}
}
