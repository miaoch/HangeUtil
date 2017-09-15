package org.other.game.chinesechess.control.game;

import org.other.game.chinesechess.Config;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * 游戏服务器，负责转发游戏对战信息
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
				System.out.println("正在监听");
			}
			try {
				System.out.println("等待客户呼叫");
				client = server.accept();
				String ip = client.getInetAddress().toString();
				System.out.println(ip + "客户加入了...");
				socketList.add(client);
			} catch (IOException e) {
				System.out.println("正在等待客户呼叫");
			}
			//为每个客户端起一个服务线程
			if (client != null) {
				new GameServerThread(client).start();
			}
			if (socketList.size() == MAX_LINKS) {
				break;
			}
		}
	}
}