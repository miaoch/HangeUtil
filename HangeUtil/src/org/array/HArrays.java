package org.array;

public class HArrays {
	public static String toString(Object[] array) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (Object arr : array) {
			if (arr.getClass().isArray()) {
				Class<?> clazz = arr.getClass();
				if (clazz == boolean[].class) {
					sb.append(toString((boolean[]) arr) + ", ");
				} else if (clazz == byte[].class) {
					sb.append(toString((byte[]) arr) + ", ");
				} else if (clazz == short[].class) {
					sb.append(toString((short[]) arr) + ", ");
				} else if (clazz == char[].class) {
					sb.append(toString((char[]) arr) + ", ");
				} else if (clazz == int[].class) {
					sb.append(toString((int[]) arr) + ", ");
				} else if (clazz == long[].class) {
					sb.append(toString((long[]) arr) + ", ");
				} else if (clazz == float[].class) {
					sb.append(toString((float[]) arr) + ", ");
				} else if (clazz == double[].class) {
					sb.append(toString((double[]) arr) + ", ");
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
	
	public static String toString(boolean[] array) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (Object arr : array) {
			if (arr.getClass().isArray()) {
				//基本数组下，不可能存放其他类型是数组
				sb.append(toString((boolean[]) arr) + ", ");
			} else {
				sb.append(arr + ", ");
			}
		}
		if (sb.length() > 2) {
			sb.delete(sb.length() - 2, sb.length());
		}
		return sb.append("]").toString();
	}
	
	public static String toString(byte[] array) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (Object arr : array) {
			if (arr.getClass().isArray()) {
				//基本数组下，不可能存放其他类型是数组
				sb.append(toString((byte[]) arr) + ", ");
			} else {
				sb.append(arr + ", ");
			}
		}
		if (sb.length() > 2) {
			sb.delete(sb.length() - 2, sb.length());
		}
		return sb.append("]").toString();
	}
	
	public static String toString(char[] array) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (Object arr : array) {
			if (arr.getClass().isArray()) {
				//基本数组下，不可能存放其他类型是数组
				sb.append(toString((char[]) arr) + ", ");
			} else {
				sb.append(arr + ", ");
			}
		}
		if (sb.length() > 2) {
			sb.delete(sb.length() - 2, sb.length());
		}
		return sb.append("]").toString();
	}
	
	public static String toString(short[] array) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (Object arr : array) {
			if (arr.getClass().isArray()) {
				//基本数组下，不可能存放其他类型是数组
				sb.append(toString((short[]) arr) + ", ");
			} else {
				sb.append(arr + ", ");
			}
		}
		if (sb.length() > 2) {
			sb.delete(sb.length() - 2, sb.length());
		}
		return sb.append("]").toString();
	}
	
	public static String toString(int[] array) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (Object arr : array) {
			if (arr.getClass().isArray()) {
				//基本数组下，不可能存放其他类型是数组
				sb.append(toString((int[]) arr) + ", ");
			} else {
				sb.append(arr + ", ");
			}
		}
		if (sb.length() > 2) {
			sb.delete(sb.length() - 2, sb.length());
		}
		return sb.append("]").toString();
	}
	
	public static String toString(long[] array) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (Object arr : array) {
			if (arr.getClass().isArray()) {
				//基本数组下，不可能存放其他类型是数组
				sb.append(toString((long[]) arr) + ", ");
			} else {
				sb.append(arr + ", ");
			}
		}
		if (sb.length() > 2) {
			sb.delete(sb.length() - 2, sb.length());
		}
		return sb.append("]").toString();
	}
	
	public static String toString(float[] array) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (Object arr : array) {
			if (arr.getClass().isArray()) {
				//基本数组下，不可能存放其他类型是数组
				sb.append(toString((float[]) arr) + ", ");
			} else {
				sb.append(arr + ", ");
			}
		}
		if (sb.length() > 2) {
			sb.delete(sb.length() - 2, sb.length());
		}
		return sb.append("]").toString();
	}
	
	public static String toString(double[] array) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (Object arr : array) {
			if (arr.getClass().isArray()) {
				//基本数组下，不可能存放其他类型是数组
				sb.append(toString((double[]) arr) + ", ");
			} else {
				sb.append(arr + ", ");
			}
		}
		if (sb.length() > 2) {
			sb.delete(sb.length() - 2, sb.length());
		}
		return sb.append("]").toString();
	}
	
	public static void main(String args[]) throws Exception {
		int[][] i = new int[][]{{1,2,},{3,4,},{5,6,},};
		Object[][] i2 = new Object[][]{new String[]{"1","2",},
				new Integer[]{3,4,},
				new Float[]{5.5f,6.6f,},
				new Integer[][]{new Integer[]{3,4,},new Integer[]{5,6,}},};
		int[][][] i3 = new int[][][]{
				{{1,-1},{2,-2},},{{3,-3},{4,-4},},{{5,-5},{6,-6},},
				};
		System.out.println(HArrays.toString(i));
		System.out.println(HArrays.toString(i2));
		System.out.println(HArrays.toString(i3));
	}
}
