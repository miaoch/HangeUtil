package test2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Serializable;

public class Test2 {
	public static void main(String args[]) {
		Bean bean1 = new Bean();
		bean1.s = new String("hello");
		Bean bean2 = copy(bean1);
		System.out.println(bean2.i);
		System.out.println(bean2.s);
		System.out.println(bean1 == bean2);
		bean1.s = "world";
		System.out.println(bean2.s);
	}
	private static<T extends Serializable> T copy(T obj) {
		PipedOutputStream pos = null;
		PipedInputStream pis = null;
		try {
			//此外还可以使用ByteArrayOutputStream生成字节数组，然后从ByteArrayInputStream读出
			pos = new PipedOutputStream();
			pis = new PipedInputStream(pos);
			ObjectOutputStream ous = new ObjectOutputStream(pos);
			ObjectInputStream ois = new ObjectInputStream(pis);
			ous.writeObject(obj);
			return (T) ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				pos.close();
				pis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
//Serializable是一个标记接口，代表可被序列化
class Bean implements Serializable {
	public Bean() {
		System.out.println("create");
	}
	int i;
	String s;
}

