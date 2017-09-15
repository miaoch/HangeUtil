package org.mail;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

class MailReceives {
    public static void main(String[] args) throws Exception {
        // ��������POP3��������������Ϣ
        String pop3Server = "pop.qq.com";
        String protocol = "pop3";
        String username = "476864279@qq.com";
        String password = "wtyvpggofkubbhci"; // QQ�����SMTP����Ȩ�룬ʲô����Ȩ�룬������������ã�

        Properties props = new Properties();
        props.setProperty("mail.smtp.socketFactory.port", "995");
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.transport.protocol", protocol); // ʹ�õ�Э�飨JavaMail�淶Ҫ��
        props.setProperty("mail.smtp.host", pop3Server); // �����˵������ SMTP��������ַ

        // ��ȡ����
        Session session = Session.getDefaultInstance(props);
        session.setDebug(false);

        // ��ȡStore����
        Store store = session.getStore(protocol);
        store.connect(pop3Server, username, password); // POP3�������ĵ�½��֤

        // ͨ��POP3Э����Store��������������ʱ���ʼ�������ֻ��ָ��Ϊ"INBOX"
        Folder folder = store.getFolder("INBOX");// ����û����ʼ��ʻ�
        folder.open(Folder.READ_WRITE); // ���ö��ʼ��ʻ��ķ���Ȩ��

        Message[] messages = folder.getMessages();// �õ������ʻ��е������ʼ�

        for (Message message : messages) {
            String subject = message.getSubject();// ����ʼ�����
            Address from = (Address) message.getFrom()[0];// ��÷����ߵ�ַ
            System.out.println("�ʼ�������Ϊ: " + subject + "\t�����˵�ַΪ: " + from);
            System.out.println("�ʼ�������Ϊ��");
            message.writeTo(System.out);// ����ʼ����ݵ�����̨
        }

        folder.close(false);// �ر��ʼ��ж���
        store.close(); // �ر����Ӷ���
    }
}
