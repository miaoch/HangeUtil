package org.other.game.chinesechess.control.game;

import org.other.game.chinesechess.Config;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * ��Ϸ������������ת����Ϸ��ս��Ϣ
 * @author miaoch
 */
public class GameServer extends Thread {
	public static List<Socket> socketList;
	private static final int MAX_LINKS = 2;
	
	public void run() {
		socketList = new ArrayList<Socket>();
		ServerSocket server = null;
		Socket client = null;
		while (true) {
			try {
				server = new ServerSocket(Config.DEFAULT_GAME_PORT);
			} catch (IOException e1) {
				System.out.println("���ڼ���");
			}
			try {
				System.out.println("�ȴ��ͻ�����");
				client = server.accept();
				String ip = client.getInetAddress().toString();
				System.out.println(ip + "�ͻ�������...");
				socketList.add(client);
			} catch (IOException e) {
				System.out.println("���ڵȴ��ͻ�����");
			}
			//Ϊÿ���ͻ�����һ�������߳�
			if (client != null) {
				new GameServerThread(client).start();
			}
			if (socketList.size() == MAX_LINKS) {
				break;
			}
		}
	}
}