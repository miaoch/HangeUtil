package test2.file;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class DirList {
	public static void main(String args[]) {
		File path = new File(".");
		String[] list = path.list(new DirFilter("^[\\S]+txt$"));
		//���FileList�����ַ�ʽ����FilenameFilter����FileFilter���ˡ�
		File[] filelist1 = path.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith("txt");
			}
		});
		File[] filelist2 = path.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				System.out.println(pathname.getName() + ": " + pathname.getPath());
				try {
					System.out.println(pathname.getName() + ": " + pathname.getCanonicalPath());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return pathname.length() > 1000;//�ļ���С����1000�ֽ�
			}
		});
		Arrays.sort(list, String.CASE_INSENSITIVE_ORDER);//���ַ���Ȼ˳������
		System.out.println(Arrays.toString(list));
		System.out.println(filelist1.length);
		System.out.println(filelist2.length);
	}
}

class DirFilter implements FilenameFilter {
	private Pattern pattern;
	public DirFilter(String regex) {
		pattern = Pattern.compile(regex);
	}
	//����true���ǽ�������ļ����ڶ����������ļ�������ȻҲ����ͨ���ļ��������Թ���
	public boolean accept(File dir, String name) {
		return pattern.matcher(name).matches();
	}
}