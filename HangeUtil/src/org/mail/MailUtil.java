package org.mail;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 * �����ʼ��Ĳ��Գ���(����qq����)
 * ͨ�����˵�qq����: xxx@qq.com �����ʼ�
 * @author miaoch
 * 
 */
public class MailUtil {

	//���͵����� �ڲ�����ֻ����qq����
	private static final String USER = "476864279@qq.com";
	//��Ȩ���� ͨ��QQ��������->�˻�->POP3/IMAP/SMTP/Exchange/CardDAV/CalDAV����->����POP3/SMTP�����ȡ
	private static final String PWD = "wtyvpggofkubbhci";
    
	private String[] to;
	private String[] cc;
	private String[] bcc;
	private String[] fileList;
	private String subject;
	private String content;
	public void sendMessage() throws MessagingException, UnsupportedEncodingException {
        // ���÷����ʼ��Ļ�������
        final Properties props = new Properties();
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        //props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        // ��ʾSMTP�����ʼ�����Ҫ���������֤
        props.setProperty("mail.transport.protocol", "smtp");// ���ô���Э��
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.qq.com");
        // �����˵��˺�
        props.put("mail.user", USER);
        // ����SMTP����ʱ��Ҫ�ṩ������ 
        props.put("mail.password", PWD);

        // ������Ȩ��Ϣ�����ڽ���SMTP���������֤
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // �û���������
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        // ʹ�û������Ժ���Ȩ��Ϣ�������ʼ��Ự
        Session mailSession = Session.getInstance(props, authenticator);
        // �����ʼ���Ϣ
        MimeMessage message = new MimeMessage(mailSession);
        BodyPart messageBodyPart = new MimeBodyPart(); 
        Multipart multipart = new MimeMultipart(); 
        // ���÷�����
        InternetAddress form = new InternetAddress(
                props.getProperty("mail.user"));
        message.setFrom(form);
        //����
        if (to != null) { 
        	String toList = getMailList(to); 
        	InternetAddress[] iaToList = new InternetAddress().parse(toList); 
        	message.setRecipients(RecipientType.TO, iaToList); // �ռ��� 
        } 
        //���� 
        if (cc != null) { 
            String toListcc = getMailList(cc); 
            InternetAddress[] iaToListcc = new InternetAddress().parse(toListcc); 
            message.setRecipients(RecipientType.CC, iaToListcc); // ������ 
        } 
        //���� 
        if (bcc != null) { 
            String toListbcc = getMailList(bcc); 
            InternetAddress[] iaToListbcc = new InternetAddress().parse(toListbcc); 
            message.setRecipients(RecipientType.BCC, iaToListbcc); // ������ 
        } 
        message.setSentDate(new Date()); // �������� 
        message.setSubject(subject); // ���� 
        message.setText(content); // ���� 
        //��ʾ��html��ʽ���ı����� 
        messageBodyPart.setContent(content,"text/html;charset=utf-8"); 
        multipart.addBodyPart(messageBodyPart); 
        //���������� 
        if(fileList!=null){ 
            addTach(fileList, multipart); 
        } 
        message.setContent(multipart); 
        // �����ʼ�
        Transport.send(message);
    }

	public void setTo(String[] to) {
		this.to = to;
	}

	public void setCc(String[] cc) {
		this.cc = cc;
	}

	public void setBcc(String[] bcc) {
		this.bcc = bcc;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public void setFileList(String[] fileList) {
		this.fileList = fileList;
	}
	
	private String getMailList(String[] mailArray) { 
		StringBuffer toList = new StringBuffer(); 
		int length = mailArray.length; 
		if (mailArray != null && length < 2) { 
			toList.append(mailArray[0]); 
		} else { 
			for (int i = 0; i < length; i++) { 
				toList.append(mailArray[i]); 
				if (i != (length - 1)) { 
					toList.append(","); 
				} 
			} 
		} 
		return toList.toString(); 
	} 
	
	//��Ӷ������ 
	public void addTach(String fileList[], Multipart multipart) throws MessagingException, UnsupportedEncodingException { 
	    for (int index = 0; index < fileList.length; index++) { 
	         MimeBodyPart mailArchieve = new MimeBodyPart(); 
	         FileDataSource fds = new FileDataSource(fileList[index]); 
	         mailArchieve.setDataHandler(new DataHandler(fds)); 
	         mailArchieve.setFileName(MimeUtility.encodeText(fds.getName(),"UTF-8","B")); 
	         multipart.addBodyPart(mailArchieve); 
        }   
	}
	
	public static void main(String args[]) throws UnsupportedEncodingException, MessagingException{
		MailUtil m = new MailUtil();
		m.setSubject("1");
		m.setContent("1");
		m.setTo(new String[]{"476864279@qq.com"});
		m.sendMessage();
	}
}
