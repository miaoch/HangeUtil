package test2;

import java.util.HashSet;


public class Test {
	public static void main(String[] args) {
		HashSet<String> set = new HashSet<String>();
		set.add("a");
		set.add("f");
		set.add("b");
		set.add("c");
		set.add("a");
		set.add("d");
		System.out.println(set);
	}
}
