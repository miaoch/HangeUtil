package org.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Test {
	public int id() default 0;
	public String name() default "hello world";
}
class C {
	@Test(id = 520, name = "hehe")
	public static void test1(int id, String name) {
		System.out.println(id);
		System.out.println(name);
	}
	@Test
	public static void test2(int id, String name) {
		System.out.println(id);
		System.out.println(name);
	}
	
	public static void main(String args[]) throws Exception {
		test(C.class);
	}
	public static void test(Class<?> c) throws Exception {
		Object obj = c.newInstance();
		for (Method m : C.class.getDeclaredMethods()) {
			Test t = m.getAnnotation(Test.class);
			if (t != null) {
				m.invoke(obj, t.id(), t.name());
			}
		}
	}
}
