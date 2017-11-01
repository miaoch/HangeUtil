package test2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class Test2 {
	public static void main(String[] args) {
		TreeSet<Student2> test = new TreeSet<Student2>(new Comparator<Student2>() {
			@Override
			public int compare(Student2 o1, Student2 o2) {
				float zf1 = o1.sx + o1.yy + o1.jsj;
				float zf2 = o2.sx + o2.yy + o2.jsj;
				if (zf1 == zf2) return 0;
				else if (zf1 > zf2) return -1;//o1的总分大，返回负值排在左边
				else return 1;
			}
		});
		Student2 s1 = new Student2();s1.sx = 10;
		Student2 s2 = new Student2();s2.sx = 11;
		Student2 s3 = new Student2();s3.sx = 12;
		Student2 s4 = new Student2();s4.sx = 13;
		test.add(s1);
		test.add(s2);
		test.add(s3);
		test.add(s4);
		System.out.println(test);
	}
}
class Student2 {
	int xh;
	String xm;
	float sx;
	float yy;
	float jsj;
	
	public String toString() {
		return xh + ": " + (sx + yy + jsj);
	}
}