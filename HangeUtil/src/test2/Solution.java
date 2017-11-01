package test2;

public class Solution {
	public static void main(String args[]) {
		int[] stone = {1,2,0,4,0,1,3,4,2,2,1,3,
				1,4,0,3,0,1,2,3,3,2,2,0,1,0,0,0,0,
				1,2,1,3,4,0,3,4,4,1,0,4,1,3,1,
				1,2,3,4,4,4,0,2,0,1,1,1,3,1,3,
				2,1,2,4,1,2,1,4,1,0,0,1,2,3,0,
				2,4,4,0,0,4,2,0,0,2,1,3,3,3,0,0,
				2,0,0,1,2,4,2,2,2,4,0};
		function(stone);
	}
	
	public static void function(int[] stone) {
		int length = stone.length;
		int[] dis = new int[length];
		String[] path = new String[length];
		dis[0] = stone[0];
		path[0] = "0";
		for (int i = 1; i < length; i++) {
			int index = indexOfMin(dis, i - 3, i);
			dis[i] = dis[index] + stone[i];
			path[i] = path[index] + " " + i;
		}
		int index = indexOfMin(dis, length - 3, length);
		System.out.println(dis[index]);
		System.out.println(path[index]);
	}
	
	public static int indexOfMin(int[] data, int begin, int end) {
		if (begin < 0) begin = 0;
		if (end > data.length) end = data.length;
		int min = Integer.MAX_VALUE;
		int minIndex = -1;
		for (int i = begin; i < end; i++) {
			if (min >= data[i]) {//有等号，保证尽量靠近起点
				min = data[i];
				minIndex = i;
			}
		}
		return minIndex;
	}
}


