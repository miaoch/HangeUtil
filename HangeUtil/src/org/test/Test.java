package org.test;

/**
 * ��Ԫ�����࣬��Ҫ��дtest����
 */
public abstract class Test<C> {
   String name;
   public Test(String name) {
	   this.name = name;
   }
   /**
    * @param container ������������
    * @param tp	���Բ���
    * @return ���Դ���
    */
   abstract int test(C container, TestParam tp);
}
