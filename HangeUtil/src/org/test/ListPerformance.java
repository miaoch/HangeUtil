package org.test;

import java.util.*;

public class ListPerformance {
	static Random rand = new Random();
	static int reps = 1000;
	//容器为List的测试类列表
	static List<Test<List<Integer>>> tests = 
			new ArrayList<Test<List<Integer>>>();
	static {
		//增加第一个测试单元，add
		tests.add(new Test<List<Integer>>("add") {
			int test(List<Integer> list, TestParam tp) {
				int loops = tp.loops;
				int listSize = tp.size;
				for (int i = 0; i < loops; i++) {
					list.clear();
					for (int j = 0; j < listSize; j++) {
						list.add(j);
					}
				}
				return loops * listSize;
			}
		});
		//增加第二个测试单元，get
		tests.add(new Test<List<Integer>>("get") {
			int test(List<Integer> list, TestParam tp) {
				int loops = tp.loops * reps;
				int listSize = list.size();
				for (int i = 0; i < loops; i++) {
					list.get(rand.nextInt(listSize));//[0,listSize)
				}
				return loops;
			}
		});
		//增加第三个测试单元，set
		tests.add(new Test<List<Integer>>("set") {
			int test(List<Integer> list, TestParam tp) {
				int loops = tp.loops * reps;
				int listSize = list.size();
				for (int i = 0; i < loops; i++) {
					list.set(rand.nextInt(listSize), 47);//[0,listSize)
				}
				return loops;
			}
		});
		//增加第四个测试单元，iteradd
		tests.add(new Test<List<Integer>>("iteradd") {
			int test(List<Integer> list, TestParam tp) {
				final int LOOPS = 1000000;
				int half = list.size() / 2;
				ListIterator<Integer> it = list.listIterator(half);
				for (int i = 0; i < LOOPS; i++) {
					it.add(47);
				}
				return LOOPS;
			}
		});
		//增加第五个测试单元，insert
		tests.add(new Test<List<Integer>>("insert") {
			int test(List<Integer> list, TestParam tp) {
				int loops = tp.loops;
				for (int i = 0; i < loops; i++) {
					list.add(5, 47);
				}
				return loops;
			}
		});
		//增加第六个测试单元，remove
		tests.add(new Test<List<Integer>>("remove") {
			int test(List<Integer> list, TestParam tp) {
				int loops = tp.loops;
				int size = tp.size;
				for (int i = 0; i < loops; i++) {
					list.clear();
					list.addAll(Collections.nCopies(size, 47));
					while (list.size() > 5) {
						list.remove(5);
					}
				}
				return loops * size - 5;
			}
		});
	}
	static class ListTester extends Tester<List<Integer>> {
		public ListTester(List<Integer> container, List<Test<List<Integer>>> tests) {
			super(container, tests);
		}
		protected List<Integer> initalize(int size) {
			container.clear();
			container.addAll(Collections.nCopies(size, 47));
			return container;
		}
		public static void run(List<Integer> container, List<Test<List<Integer>>> tests) {
			new ListTester(container, tests).timedTest();
		}
	}
	public static void main(String args[]) {
		ListTester.run(new ArrayList<Integer>(), tests);
		ListTester.run(new LinkedList<Integer>(), tests);
		ListTester.run(new Vector<Integer>(), tests);
	}
}
