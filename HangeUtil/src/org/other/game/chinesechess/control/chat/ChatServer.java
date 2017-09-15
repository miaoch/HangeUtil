package org.other.game.chinesechess.control.chat;

import org.other.game.chinesechess.Config;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * ���������������㲥�ͻ��˷�������Ϣ port:12345
 * ��Ϸ������������ת����Ϸ��ս��Ϣ port:12346
 * @author Administrator
 *
 */
public class ChatServer extends Thread {
	private static Map<String, Socket> socketMap;
	
	public void run() {
		socketMap = new HashMap<>();
		ServerSocket server = null;
		Socket client = null;
		while (true) {
			try {
				server = new ServerSocket(Config.DEFAULT_CHAT_PORT);
			} catch (IOException e1) {
				System.out.println("���ڼ���");
			}
			try {
				System.out.println("�ȴ��ͻ�����");
				client = server.accept();
				String ip = client.getInetAddress().toString();
				System.out.println(ip + "�ͻ�������...");
				if (socketMap.containsKey(ip)) {
					Socket last = socketMap.get(ip);
					last.close();
				}
				socketMap.put(ip, client);
			} catch (IOException e) {
				System.out.println("���ڵȴ��ͻ�����");
			}
			//Ϊÿ���ͻ�����һ�������߳�
			if (client != null) {
				new ChatServerThread(client).start();
			}
		}
	}
	
	public static Map<String, Socket> getSocketMap() {
		return socketMap;
	}
}