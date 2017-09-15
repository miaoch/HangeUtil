package org.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * �ļ�����������
 * @author miaoch
 *
 */
public class FileUtil {
	
	/**
	 * �����ļ�
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
	public static void copyFile(String src, String dest) throws IOException {
        FileInputStream in = new FileInputStream(src);
        File file = createOrEditFile(dest);
        FileOutputStream out = new FileOutputStream(file);
        transfer(in, out);
        in.close();
        out.close();
    }
	
	private static void transfer(InputStream in, OutputStream out) throws IOException {
		byte[] data = new byte[1024];
		int index;
		while ((index = in.read(data)) != -1) {  
			out.write(data, 0 , index);  
		}  
	}
	
	/**
	 * �ļ�������
	 * @param path
	 * @param oldname
	 * @param newname
	 */
	public static void renameFile(String path, String oldname, String newname) {
        if (!oldname.equals(newname)) {//�µ��ļ�������ǰ�ļ�����ͬʱ,���б�Ҫ����������
            File oldfile = new File(path, oldname);
            File newfile = new File(path, newname);
            if (!oldfile.exists()) {
            	System.out.println(oldfile.getAbsolutePath() + "�����ڣ�");
            } else if (newfile.exists()) {//���ڸ�Ŀ¼���Ѿ���һ���ļ������ļ�����ͬ��������������
                System.out.println(newfile.getAbsolutePath() + "�Ѿ����ڣ�");
            } else {
                oldfile.renameTo(newfile);
            }
        }         
    }
	
	/**
	 * ת���ļ���
	 * @param filePath
	 * @param newPath
	 * @param cover
	 */
	public static void changeDirectory(String filePath, String newPath, boolean cover) {
		File oldfile = new File(filePath);
		File newfile = new File(newPath, oldfile.getName());
		if (!oldfile.exists()) {
			System.out.println(oldfile.getAbsolutePath() + "�����ڣ�");
		}
        if(!oldfile.getParent().equals(newfile.getParentFile())) {
        	if (!newfile.getParentFile().exists()) { //�������ڸ�·�����򴴽�
        		newfile.getParentFile().mkdirs();
        	}
            if (newfile.exists()) {//���ڴ�ת��Ŀ¼�£��Ѿ����ڴ�ת���ļ�
                if (cover) {
                	deleteAllFilesOfDir(newfile);//ɾ��ԭ�ļ�
                	oldfile.renameTo(newfile);
                } else {
                	System.out.println(newfile.getAbsolutePath() + "�Ѿ�����!");
                }
            } else {
                oldfile.renameTo(newfile);
            }
        }       
    }
	
	/**
	 * ɾ���ļ�(������ļ��� ��ɾ�����������ļ�)
	 * @param path
	 */
	public static void deleteAllFilesOfDir(File path) {  
	    if (!path.exists())  
	        return;  
	    if (path.isFile()) {  
	        path.delete();  
	        return;  
	    }  
	    File[] files = path.listFiles();  
	    for (int i = 0; i < files.length; i++) {  
	        deleteAllFilesOfDir(files[i]);  
	    }  
	    path.delete();  
	}  
	
	/**
	 * ��������ļ����������ļ�
	 * @param path
	 */
	public static void getAllFileByDir(File file, List<File> result) {  
	    if (!file.exists()) return;  
	    if (file.isFile()) {  
	    	result.add(file);
	        return;  
	    }  
	    File[] files = file.listFiles();  
	    for (int i = 0; i < files.length; i++) {  
	    	getAllFileByDir(files[i], result);  
	    }  
	} 
	
	/**
	 * ��ȡFile���� ���ļ��������򴴽��ļ�
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static File createOrEditFile(String path) throws IOException {
		File file = new File(path);
		if (!file.getParentFile().exists()) {
        	file.getParentFile().mkdirs();
        	file.createNewFile();
        } else if (!file.exists()) {
        	file.createNewFile();
        }
		return file;
	}
	
	/**
	 * ��֤�ļ��Ƿ����,���������쳣�����򷵻ظö���
	 * @param path
	 * @return
	 * @throws FileNotFoundException
	 */
	public static File checkPath(String path) throws FileNotFoundException {
		File file = new File(path);
        if (!file.exists() || file.isDirectory()) {
        	throw new FileNotFoundException();
        }
        return file;
	}
	
	/**
	 * �������ļ����浽����
	 * @param outputFile	���ص�ַǰ׺
	 * @param fileName		�ļ���
	 * @param urlStr		�ļ������ַ
	 */
	public static void getURLResource(String outputFile, String fileName, String urlStr) {
		getURLResource(new File(outputFile), fileName, urlStr);
    }
	
	/**
	 * �������ļ����浽����
	 * @param outputFile	���ص�ַǰ׺
	 * @param fileName		�ļ���
	 * @param urlStr		�ļ������ַ
	 */
	public static void getURLResource(File file, String fileName, String urlStr) {
		urlStr = urlStr.replaceAll("\\\\", "/");
		if(!file.exists()){
			file.mkdirs();
		}
		GetMethod get = null;  
		BufferedInputStream bis = null;  
		BufferedOutputStream bos = null;  
		try {  
			URI uri = new URI(urlStr, false, "UTF-8");  
			HttpClient hc = new HttpClient();  
			get = new GetMethod(uri.toString());  
			int status = hc.executeMethod(get);  
			if (status == 200) {  
				bis = new BufferedInputStream(get 
						.getResponseBodyAsStream());  
				bos = new BufferedOutputStream(  
						new FileOutputStream(file.getAbsolutePath() + "\\" + fileName));  
				byte[] buffer = new byte[1024];  
				int len = 0;  
				while ((len = bis.read(buffer)) != -1) {  
					bos.write(buffer, 0, len);  
				}  
			}  
		} catch(Exception e) {
			e.printStackTrace();
		} finally {  
			if(get != null){  
				get.releaseConnection();  
			}  
			if(bis != null){  
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}  
			}  
			if(bos != null){  
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}  
			}  
		}  
	}
	public static void main(String args[]) throws Exception {
	}
}
