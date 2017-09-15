package org.other.game.chinesechess.control.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;

/**
 * ����������߳�
 * @author miaoch
 */
public class ChatServerThread extends Thread {
	private Socket socket;
	private DataInputStream in;
	private String message = null;
	private Map<String, Socket> socketMap;
	
	public ChatServerThread(Socket cilentSocket) {
		socket = cilentSocket;
		try {
			in = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		socketMap = ChatServer.getSocketMap();
	}
	public void run() {
		while (true) {
			try {
				message = in.readUTF();
				if (message != null && !"".equals(message)) {
					Iterator<String> it = socketMap.keySet().iterator();
					//�����пͻ�������Ϣ
					while (it.hasNext()) {
						Socket other = (Socket) socketMap.get(it.next());
						new DataOutputStream(other.getOutputStream()).writeUTF(message);
					}
				}
			} catch (IOException e) {
				System.out.println(socket.getInetAddress() + "�ͻ��뿪��...");
				return;
			}
		}
	}
}