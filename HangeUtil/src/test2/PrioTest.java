package test2;

import java.util.PriorityQueue;

public class PrioTest {
	public static void main(String agrs[]) {
		PriorityQueue<Integer> queue = new PriorityQueue<Integer>();
		queue.offer(4);
		queue.offer(2);
		queue.offer(3);
		queue.offer(5);
		queue.offer(1);
		System.out.println(queue.peek());
		System.out.println(queue);
	}
}
