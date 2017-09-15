package org.sort;

import java.util.Arrays;

/**
 * 所有方法都可以用泛型进行提取，但是目前只是为了了解算法，所以统一使用int
 * @author Administrator
 *
 */
public class SortUtil {
	
	public static void main(String args[]) {
		int[] a2 = {5,4,3,1,2,4,5,6,4,1,2,3,5};
		insertSort(a2);
		System.out.println(Arrays.toString(a2));
	}
	
	/**
	 * 快速排序 泛型例子
	 * @param a
	 * @param low
	 * @param high
	 */
	public static<T extends Comparable<T>> void quickSortFX(T[] a, int low, int high) {
		int start = low;
		int end = high;
		T key = a[low];
		while (end > start) {
			for (;end > start; end--) {
				if (a[end].compareTo(key) < 0) {
					T temp = a[end];
					a[end] = a[start];
					a[start] = temp;
					break;
				}
			}
			for (;start < end; start++) {
				if (a[start].compareTo(key) > 0) {
					T temp = a[end];
					a[end] = a[start];
					a[start] = temp;
					break;
				}
			}
		}
		if (start > low + 1) quickSortFX(a, low, start - 1);
		if (end + 1 < high) quickSortFX(a, end+  1, high);
	}
	
	/**
	 * 快速排序
	 * @param a
	 * @param low 初始为0
	 * @param high 初始为length - 1
	 */
	public static void quickSort(int[] a, int low, int high) {
		int start = low;
		int end = high;
		int key = a[low];
		while (end > start) {
			for (;end > start; end--) {
				if(a[end] < key){
					int temp = a[end];
					a[end] = a[start];
					a[start] = temp;
					break;
				}
			}
			for (;start < end; start++) {
				if (a[start] > key) {
					int temp = a[end];
					a[end] = a[start];
					a[start] = temp;
					break;
				}
			}
		}
		if (start > low + 1) quickSort(a, low, start - 1);
		if (end + 1 < high) quickSort(a, end + 1, high);
	}
	
	/**
	 * 冒泡排序
	 * @param a
	 */
	public static void bubbleSort(int[] a) {
		int len = a.length;
		for (int i = 0; i < len - 1; i++) {
			for (int j = 0; j < len - 1 - i; j++) {
				if (a[j] > a[j + 1]) {
					int temp = a[j];
					a[j] = a[j + 1];
					a[j + 1] = temp;
				}
			}
		}
	}
	
	/**
	 * 插入排序
	 * @param a
	 */
	public static void insertSort(int[] a) {
		for (int i = 1; i < a.length; i++) {
			int key = a[i];
			int j = i;
			while (j > 0 && key < a[j - 1]) {
				a[j] = a[j - 1];
				j--;
			}
			a[j] = key; 
		}
	}

	/**
	 * 选择排序
	 * @param a
	 */
	/*public static void selectSort(int []a) {
		for (int )
	}*/
}
