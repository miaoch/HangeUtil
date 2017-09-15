package org.zip;

import java.io.*;  
import java.util.zip.*;  

public class ZipUtil {  
	private static void zip(ZipOutputStream out, File f, String base) throws Exception {
		if (f.isDirectory()) {
			File[] files = f.listFiles();
			base = (base.length() == 0 ? "" : base + "/");
			for (int i = 0; i < files.length; i++) {
				zip(out, files[i], base + files[i].getName());
			}
		} else { 
			out.putNextEntry(new ZipEntry(base));
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
			transfer(in, out);
			in.close();
		}  
	}  

	//压缩文件，inputFileName表示要压缩的文件（可以为目录）,zipFileName表示压缩后的zip文件  
	public static void zip(String inputFileName, String zipFileName) throws Exception {  
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));  
		zip(out, new File(inputFileName), "");
		out.close();
	}  

	//解压,zipFileName表示待解压的zip文件，unzipDir表示解压后文件存放目录  
	public static void unzip(String zipFileName, String unzipDir) throws Exception {
		createDir(unzipDir);
		ZipInputStream in = new ZipInputStream(new FileInputStream(zipFileName));  
		ZipEntry entry;  
		while ((entry = in.getNextEntry()) != null) {  
			String fileName = entry.getName();  
			//有层级结构，就先创建目录  
			String tmp;
			int index = fileName.lastIndexOf('/');  
			if (index != -1) {  
				tmp = fileName.substring(0, index);  
				tmp = unzipDir + "/" + tmp;  
				createDir(tmp);
			}
			//创建文件  
			fileName = unzipDir + "/" + fileName;  
			File file = new File(fileName);  
			file.createNewFile();  
			FileOutputStream out = new FileOutputStream(file);  
			transfer(in, out);
			out.close();  
		}  
		in.close();  
	}
	
	private static void transfer(InputStream in, OutputStream out) throws Exception {
		byte[] data = new byte[1024];
		int index;
		while ((index = in.read(data)) != -1) {  
			out.write(data, 0 , index);  
		}  
	}
	
	private static File createDir(String path) {
		File f = new File(path);
		if (!f.isDirectory()) {
			f.mkdirs();
		}
		return f;
	}
	
	public static void main(String args[]) throws Exception {
		zip("D:\\test", "D:\\test.zip");
		unzip("D:\\test.zip", "D:\\test_2");
	}
}  