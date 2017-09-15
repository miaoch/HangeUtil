package org.other.game.chinesechess.control.game;

import org.other.game.chinesechess.Config;
import org.other.game.chinesechess.model.Command;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

/**
 * 服务端聊天线程
 * @author miaoch
 */
public class GameServerThread extends Thread {
	private Socket socket;
	private ObjectInputStream in;
	private Command command = null;
	public List<Socket> socketList;	
	
	public GameServerThread(Socket cilentSocket) {
		socket = cilentSocket;
		try {
			in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.socketList = GameServer.socketList;
	}
	public void run() {
		while (true) {
			try {
				command = (Command) in.readObject();
				if (command != null && !"".equals(command.getType())) {
					//加入游戏只发别人不发自己
					if (command.getType() == Config.COMMAND_JOINGAME) {
						for (Socket socket:socketList) {
							if (this.socket != socket) {
								new ObjectOutputStream(socket.getOutputStream()).writeObject(command);
							}
						}
					} else {
						for (Socket socket:socketList) {
							new ObjectOutputStream(socket.getOutputStream()).writeObject(command);
						}
					}
				}
			} catch (IOException e) {
				System.out.println(socket.getInetAddress() + "客户离开了...");
				return;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}