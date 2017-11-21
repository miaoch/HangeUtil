import java.util.Arrays;

public class TestMain {

	public static void main(String[] args) {
		for (int i = 1; i < 1000000; i++) {
			if (max_min(string2intArray(String.valueOf(i))) == i) {
				System.out.println(i);
			}
		}
	}
	//字符串转int[]数组
	public static int[] string2intArray(String numstr) {
		int[] result = new int[numstr.length()];
		for (int i=0; i<numstr.length(); i++) {
			result[i] = numstr.charAt(i) - '0';
		}
		return result;
	}
	//主要的判断方法
	public static int max_min(int[] sz) {
		int max = 0, min = 0;
		Arrays.sort(sz);
		for (int i=0; i<sz.length; i++) {
			min = min * 10 + sz[i];
			max = max * 10 + sz[sz.length - i - 1];
		}
		return max - min;
	}
}