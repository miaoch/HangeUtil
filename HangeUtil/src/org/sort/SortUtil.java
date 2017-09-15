package org.sort;

import java.util.Arrays;

/**
 * ���з����������÷��ͽ�����ȡ������Ŀǰֻ��Ϊ���˽��㷨������ͳһʹ��int
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
	 * �������� ��������
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
	 * ��������
	 * @param a
	 * @param low ��ʼΪ0
	 * @param high ��ʼΪlength - 1
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
	 * ð������
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
	 * ��������
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
	 * ѡ������
	 * @param a
	 */
	/*public static void selectSort(int []a) {
		for (int )
	}*/
}
