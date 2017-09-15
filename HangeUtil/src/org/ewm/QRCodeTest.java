package org.ewm;
import java.io.*;  
import java.util.Scanner;
import java.awt.*;  
import org.excel.ExcelUtil;
import org.sourceforge.jlibeps.epsgraphics.EpsGraphics2D;

import com.swetake.util.Qrcode;  

public class QRCodeTest {  
	static int width = 90;  //67 + 12 * (version - 1) ;
	static int height = 90; //67 + 12 * (version - 1) ;
	static String outputPath = "D:/";

	public static void create_image(String sms_info, String fileName)throws Exception{  
		try{  
			Qrcode testQrcode =new Qrcode();  
			testQrcode.setQrcodeErrorCorrect('M');  
			testQrcode.setQrcodeEncodeMode('B');  
			testQrcode.setQrcodeVersion(7);  
			String testString = sms_info;  
			byte[] d = testString.getBytes("gbk");  
			EpsGraphics2D graphics2d = new EpsGraphics2D();
			graphics2d.setBackground(Color.WHITE);  
			graphics2d.clearRect(0, 0, width, height);  
			graphics2d.setColor(Color.BLACK);
			if (d.length>0 && d.length <120){  
				boolean[][] s = testQrcode.calQrcode(d);  
				for (int i=0;i<s.length;i++){  
					for (int j=0;j<s.length;j++){  
						if (s[j][i]) {  
							graphics2d.fillRect(j*2 , i*2, 2, 2);  
						}  
					}  
				}  
			}
			graphics2d.flush();
			try { 
				FileWriter fos = new FileWriter(outputPath + "\\" + fileName + ".eps"); 
				fos.write(graphics2d.toString()); 
				fos.close(); 
			} catch (FileNotFoundException e) { 
				e.printStackTrace(); 
			} catch (IOException e) { 
				e.printStackTrace(); 
			} 
			graphics2d.close();
		}  
		catch (Exception e) {  
			e.printStackTrace();  
		}   
	}  

	public static void main(String[] args) throws Exception {  
		/*File config = new File("�����ļ�.txt");
		FileReader fr1 = new FileReader(config);
		BufferedReader br1 = new BufferedReader(fr1);
		String temp = null;
		temp = br1.readLine();
		temp = temp.substring(temp.indexOf(":") + 1).trim();
		width = Integer.parseInt(temp.split("\\s")[0]);
		height = Integer.parseInt(temp.split("\\s")[1]);
		temp = br1.readLine();
		String inputPath = temp.substring(temp.indexOf(":") + 1).trim();
		temp = br1.readLine();
		outputPath = temp.substring(temp.indexOf(":") + 1).trim();
		br1.close();
		fr1.close();
		////////////////
		ExcelUtil eu = new ExcelUtil(inputPath);
		String[][] result = eu.readRowData();
		System.out.println("�ܹ���"+(result.length - 1) +"�Ŷ�ά����Ҫ���ɣ�(Ҳ�����С���)");
		for (int i=1; i<result.length; i++) {
			QRCodeTest.create_image(result[i][0].trim(), result[i][1].trim());  
			if (i%100 == 0) {
				System.out.println("�Ѿ��ɹ�����"+i+"�Ŷ�ά�룡");
			}
		}
		System.out.println("�ɹ������س��˳�");
		new Scanner(System.in).next();*/
		QRCodeTest.create_image("123456", "text");  
	}   
}  