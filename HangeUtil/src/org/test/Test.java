package org.test;

/**
 * 单元测试类，需要重写test方法
 */
public abstract class Test<C> {
   String name;
   public Test(String name) {
	   this.name = name;
   }
   /**
    * @param container 测试容器对象
    * @param tp	测试参数
    * @return 测试次数
    */
   abstract int test(C container, TestParam tp);
}
