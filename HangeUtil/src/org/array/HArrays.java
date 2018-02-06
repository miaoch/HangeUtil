package org.array;

import java.util.Arrays;

public class HArrays {
	public static String toString(Object[] array) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (Object arr : array) {
			if (arr.getClass().isArray()) {
				Class<?> clazz = arr.getClass();
				if (clazz == boolean[].class) {
					sb.append(Arrays.toString((boolean[]) arr) + ", ");
				} else if (clazz == byte[].class) {
					sb.append(Arrays.toString((byte[]) arr) + ", ");
				} else if (clazz == short[].class) {
					sb.append(Arrays.toString((short[]) arr) + ", ");
				} else if (clazz == char[].class) {
					sb.append(Arrays.toString((char[]) arr) + ", ");
				} else if (clazz == int[].class) {
					sb.append(Arrays.toString((int[]) arr) + ", ");
				} else if (clazz == long[].class) {
					sb.append(Arrays.toString((long[]) arr) + ", ");
				} else if (clazz == float[].class) {
					sb.append(Arrays.toString((float[]) arr) + ", ");
				} else if (clazz == double[].class) {
					sb.append(Arrays.toString((double[]) arr) + ", ");
				} else {
					sb.append(toString((Object[]) arr) + ", ");
				}
			} else {
				sb.append(arr.toString() + ", ");
			}
		}
		if (sb.length() > 2) {
			sb.delete(sb.length() - 2, sb.length());
		}
		return sb.append("]").toString();
	}
	
	public static int[] getRandom(int[] array, int count) {
		if (array == null || array.length <= count || count < 0) return array;
		int size = array.length;
		for (int i = 0; i < size; i++) {
			int random = (int) (Math.random() * size); // [0,size)
			int temp = array[i];
			array[i] = array[random];
			array[random] = temp;
		}
		int[] result = new int[count];
		int random = (int) (Math.random() * (size - count)); // [0,size - count)
		System.arraycopy(array, random, result, 0, count);
		return result;
	}
	
	public static void main(String args[]) throws Exception {
		int[] array = new int[]{1,2,3,4,5,6,7,8,9,10};
		System.out.println(Arrays.toString(getRandom(array, 5)));
	}
}
