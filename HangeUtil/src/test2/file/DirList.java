package test2.file;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class DirList {
	public static void main(String args[]) {
		File path = new File(".");
		String[] list = path.list(new DirFilter("^[\\S]+txt$"));
		//获得FileList的两种方式，用FilenameFilter或者FileFilter过滤。
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
				return pathname.length() > 1000;//文件大小大于1000字节
			}
		});
		Arrays.sort(list, String.CASE_INSENSITIVE_ORDER);//用字符自然顺序排序
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
	//返回true就是接受这个文件，第二个参数是文件名，当然也可以通过文件其他属性过滤
	public boolean accept(File dir, String name) {
		return pattern.matcher(name).matches();
	}
}