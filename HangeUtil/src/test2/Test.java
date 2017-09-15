package test2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.array.HArrays;

public class Test extends JFrame {
	public static void main(String args[]) throws Exception {
		int[][] i = new int[][]{{1,2,},{3,4,},{5,6,},};
		Object[][] i2 = new Object[][]{new String[]{"1","2",},
				new Integer[]{3,4,},
				new Float[]{5.5f,6.6f,},
				new Integer[][]{new Integer[]{3,4,},new Integer[]{5,6,}},};
		int[][][] i3 = new int[][][]{
				{{1,-1},{2,-2},},{{3,-3},{4,-4},},{{5,-5},{6,-6},},
				};
		//String[][] s = new String[][]{{"1","2",},{"3","4",},{"5","6",},};
		System.out.println(HArrays.toString(i));
		System.out.println(HArrays.toString(i3));
		//System.out.println(HArrays.toString(i2));
	}
	
	public static<T> String myPrint(T[] array) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (Object arr : array) {
			System.out.println(arr.getClass());
			if (arr.getClass().isArray()) {
				sb.append(myPrint((T[]) arr) + ", ");
			} else {
				sb.append(arr.toString() + ", ");
			}
		}
		if (sb.length() > 2) {
			sb.delete(sb.length() - 2, sb.length());
		}
		return sb.append("]").toString();
	}
}
