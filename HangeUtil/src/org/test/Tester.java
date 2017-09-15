package org.test;

import java.util.List;

/**
 * ²âÊÔ¿ØÖÆÆ÷
 */
public class Tester<C> {
	public static TestParam[] defaultParams = TestParam.array(
			10, 5000, 100, 5000, 1000, 5000, 10000, 500);
	protected C initalize(int size) {
		return container;
	}
	public static int fieldWidth = 8;
	protected C container;
	private String headline = "";
	private List<Test<C>> tests;
	private static String stringField() {
		return "%" + fieldWidth + "s";
	}
	private static String numberField() {
		return "%" + fieldWidth + "d";
	}
	private static int sizeWidth = 5;
	private static String sizeField = "%" + sizeWidth + "s";
	private TestParam[] paramList = defaultParams;
	
	public Tester(C container, List<Test<C>> tests) {
		this.container = container;
		this.tests = tests;
		if (container != null) {
			headline = container.getClass().getSimpleName();
		}
	}
	public Tester(C container, List<Test<C>> tests, TestParam[] paramList) {
		this(container, tests);
		this.paramList = paramList;
	}
	public void setHeadline(String newHeadline) {
		headline = newHeadline;
	}
	public static<C> void run(C cntnr, List<Test<C>> tests) {
		new Tester<C>(cntnr, tests).timedTest();
	}
	public static<C> void run(C cntnr, List<Test<C>> tests, TestParam[] paramList) {
		new Tester<C>(cntnr, tests, paramList).timedTest();
	}
	public void timedTest() {
		displayHeader();
		for (TestParam param : paramList) {
			System.out.printf(sizeField, param.size);
			for (Test<C> test : tests) {
				C kontainer = initalize(param.size);
				long start = System.nanoTime();
				int reps = test.test(kontainer, param);
				long duration = System.nanoTime() - start;
				long timePerRep = duration / reps;
				System.out.printf(numberField(), timePerRep);
			}
			System.out.println();
		}
	}
	private void displayHeader() {
		int width = fieldWidth * tests.size() + sizeWidth;
		int dashLength = width - headline.length() - sizeWidth;
		StringBuilder head = new StringBuilder(width);
		for (int i = 0; i < dashLength / 2; i++) {
			head.append('-');
		}
		head.append(' ');
		head.append(headline);
		head.append(' ');
		for (int i = 0; i < dashLength / 2; i++) {
			head.append('-');
		}
		System.out.println(head);
		System.out.printf(sizeField, "size");
		for (Test test : tests) {
			System.out.printf(stringField(), test.name);
		}
		System.out.println();
	}
}
