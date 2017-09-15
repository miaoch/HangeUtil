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
 * 文件操作工具类
 * @author miaoch
 *
 */
public class FileUtil {
	
	/**
	 * 复制文件
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
	 * 文件重命名
	 * @param path
	 * @param oldname
	 * @param newname
	 */
	public static void renameFile(String path, String oldname, String newname) {
        if (!oldname.equals(newname)) {//新的文件名和以前文件名不同时,才有必要进行重命名
            File oldfile = new File(path, oldname);
            File newfile = new File(path, newname);
            if (!oldfile.exists()) {
            	System.out.println(oldfile.getAbsolutePath() + "不存在！");
            } else if (newfile.exists()) {//若在该目录下已经有一个文件和新文件名相同，则不允许重命名
                System.out.println(newfile.getAbsolutePath() + "已经存在！");
            } else {
                oldfile.renameTo(newfile);
            }
        }         
    }
	
	/**
	 * 转移文件夹
	 * @param filePath
	 * @param newPath
	 * @param cover
	 */
	public static void changeDirectory(String filePath, String newPath, boolean cover) {
		File oldfile = new File(filePath);
		File newfile = new File(newPath, oldfile.getName());
		if (!oldfile.exists()) {
			System.out.println(oldfile.getAbsolutePath() + "不存在！");
		}
        if(!oldfile.getParent().equals(newfile.getParentFile())) {
        	if (!newfile.getParentFile().exists()) { //若不存在该路径，则创建
        		newfile.getParentFile().mkdirs();
        	}
            if (newfile.exists()) {//若在待转移目录下，已经存在待转移文件
                if (cover) {
                	deleteAllFilesOfDir(newfile);//删除原文件
                	oldfile.renameTo(newfile);
                } else {
                	System.out.println(newfile.getAbsolutePath() + "已经存在!");
                }
            } else {
                oldfile.renameTo(newfile);
            }
        }       
    }
	
	/**
	 * 删除文件(如果是文件夹 则删除以下所有文件)
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
	 * 遍历获得文件夹下所有文件
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
	 * 获取File对象 若文件不存在则创建文件
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
	 * 验证文件是否存在,不存在抛异常，否则返回该对象。
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
	 * 将网络文件保存到本地
	 * @param outputFile	本地地址前缀
	 * @param fileName		文件名
	 * @param urlStr		文件网络地址
	 */
	public static void getURLResource(String outputFile, String fileName, String urlStr) {
		getURLResource(new File(outputFile), fileName, urlStr);
    }
	
	/**
	 * 将网络文件保存到本地
	 * @param outputFile	本地地址前缀
	 * @param fileName		文件名
	 * @param urlStr		文件网络地址
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
