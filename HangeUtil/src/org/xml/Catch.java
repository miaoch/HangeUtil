package org.xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.file.FileUtil;
import org.stream.BufferInputFile;


public class Catch {
	private final static String regex = "<img.*src[\\s]*=[\\s]*([\'\"]([^\'\"]*)[\'\"])";
	private final static Pattern pattern = Pattern.compile(regex);
	private final static String sourceDir = "D:\\source";
	
	public static void main(String args[]) throws Exception {
		List<File> fileList = new ArrayList<File>();
		FileUtil.getAllFileByDir(new File(sourceDir), fileList);
		for (File file : fileList) {
			try {
				onePage(file);
			} catch (Exception e) {
				System.out.println(file.getAbsolutePath() + " error!");
			}
		}
	}
	
	private static void onePage(File file) throws IOException {
		List<String> links = new ArrayList<String>();
		String result = BufferInputFile.read(file);
		Matcher matcher = pattern.matcher(result);
		while (matcher.find()) {
			links.add(matcher.group(2).replaceAll("/./", "/f/"));
		}
		String filepath = file.getAbsolutePath();
		int count = 0;
		for (String link : links) {
			FileUtil.getURLResource(filepath.substring(0,
					filepath.lastIndexOf(".")),
						count++ + ".png", link);
		}
	}
}
