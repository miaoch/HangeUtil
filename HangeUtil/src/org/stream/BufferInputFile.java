package org.stream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/*缓冲输入文件*/
public class BufferInputFile {
	//这种抛异常方式非常危险，如果in.readLine()出现异常会导致in无法被close(虽然概率不大)。
	//这里只是一个例子，请不要直接调这个方法。
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
