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

	//ѹ���ļ���inputFileName��ʾҪѹ�����ļ�������ΪĿ¼��,zipFileName��ʾѹ�����zip�ļ�  
	public static void zip(String inputFileName, String zipFileName) throws Exception {  
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));  
		zip(out, new File(inputFileName), "");
		out.close();
	}  

	//��ѹ,zipFileName��ʾ����ѹ��zip�ļ���unzipDir��ʾ��ѹ���ļ����Ŀ¼  
	public static void unzip(String zipFileName, String unzipDir) throws Exception {
		createDir(unzipDir);
		ZipInputStream in = new ZipInputStream(new FileInputStream(zipFileName));  
		ZipEntry entry;  
		while ((entry = in.getNextEntry()) != null) {  
			String fileName = entry.getName();  
			//�в㼶�ṹ�����ȴ���Ŀ¼  
			String tmp;
			int index = fileName.lastIndexOf('/');  
			if (index != -1) {  
				tmp = fileName.substring(0, index);  
				tmp = unzipDir + "/" + tmp;  
				createDir(tmp);
			}
			//�����ļ�  
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