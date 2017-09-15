package org.stream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/*���������ļ�*/
public class BufferInputFile {
	//�������쳣��ʽ�ǳ�Σ�գ����in.readLine()�����쳣�ᵼ��in�޷���close(��Ȼ���ʲ���)��
	//����ֻ��һ�����ӣ��벻Ҫֱ�ӵ����������
	public static String read(String filename) throws IOException {
		return read(new File(filename));
	}
	
	public static String read(File file) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(file));
		StringBuilder result = new StringBuilder();
		String temp;
		while ((temp = in.readLine()) != null) {
			result.append(temp + "\n");
		}
		in.close();
		return result.toString();
	}
	
	public static void main(String args[]) throws IOException {
		System.out.println(read("c1.txt"));
	}
}
