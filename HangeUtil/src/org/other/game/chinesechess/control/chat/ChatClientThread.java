package org.other.game.chinesechess.control.chat;

import java.io.DataInputStream;
import java.io.IOException;

import javax.swing.JTextArea;

public class ChatClientThread extends Thread {
	private DataInputStream in;
	private JTextArea chatArea;
	
	public void setEditArea(JTextArea chatArea) {
		this.chatArea = chatArea;
	}

	public void setInputStream(DataInputStream in){
		this.in = in;
	}
	@Override
	public void run() {
		while (true) {
			try {
				String message = in.readUTF();
				chatArea.setText(chatArea.getText() + message + "\n");
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("已经与服务器断开！");
				break;
			}
		}
	}
}
