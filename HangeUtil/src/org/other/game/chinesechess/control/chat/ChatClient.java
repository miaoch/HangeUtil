package org.other.game.chinesechess.control.chat;

import org.other.game.chinesechess.Config;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.JTextArea;

/**
 * 聊天室客户端 
 * @author miaoch
 *
 */
public class ChatClient {
	private DataOutputStream out = null;
	
	public ChatClient(String serverIP, JTextArea chatArea) throws IOException {
		Socket socket = new Socket();
		ChatClientThread clientThread = new ChatClientThread();
		DataInputStream in = null;
		if (!socket.isConnected()) {
			InetAddress address = InetAddress.getByName(serverIP);
			InetSocketAddress socketAddress = new InetSocketAddress(address, Config.DEFAULT_CHAT_PORT);
			socket.connect(socketAddress);
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
			clientThread.setInputStream(in);
			clientThread.setEditArea(chatArea);
			clientThread.start();
		}
	}
	
	/**
	 * 向服务端发送信息
	 * @param message
	 */
	public void sendMessage(String message) {
		try {
			out.writeUTF(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}