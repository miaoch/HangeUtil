package org.other.game.chinesechess.control.chat;

import org.other.game.chinesechess.Config;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 聊天服务器，负责广播客户端发来的信息 port:12345
 * 游戏服务器，负责转发游戏对战信息 port:12346
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
				System.out.println("正在监听");
			}
			try {
				System.out.println("等待客户呼叫");
				client = server.accept();
				String ip = client.getInetAddress().toString();
				System.out.println(ip + "客户加入了...");
				if (socketMap.containsKey(ip)) {
					Socket last = socketMap.get(ip);
					last.close();
				}
				socketMap.put(ip, client);
			} catch (IOException e) {
				System.out.println("正在等待客户呼叫");
			}
			//为每个客户端起一个服务线程
			if (client != null) {
				new ChatServerThread(client).start();
			}
		}
	}
	
	public static Map<String, Socket> getSocketMap() {
		return socketMap;
	}
}