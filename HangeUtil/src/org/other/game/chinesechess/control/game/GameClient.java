package org.other.game.chinesechess.control.game;

import org.other.game.chinesechess.Config;
import org.other.game.chinesechess.control.Listener;
import org.other.game.chinesechess.model.Command;
import org.other.game.chinesechess.view.ChessGame;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;


/**
 * 聊天室客户端 
 * @author miaoch
 *
 */
public class GameClient {
	private ObjectOutputStream out = null;
	private Listener listener;
	
	public GameClient(String serverIP,ChessGame chessGame) throws IOException {
		this.listener = chessGame.gamePanel.getListener();
		Socket socket = new Socket();
		if (!socket.isConnected()) {
			InetAddress address = InetAddress.getByName(serverIP);
			InetSocketAddress socketAddress = new InetSocketAddress(address, Config.DEFAULT_GAME_PORT);
			socket.connect(socketAddress);
			out = new ObjectOutputStream(socket.getOutputStream());
			new GameClientThread(socket, chessGame).start();
		}
	}
	
	/**
	 * 向服务端发送命令
	 * @param message
	 */
	public void sendCommand(Command command) {
		try {
			out.writeObject(command);
			if (command.getType() != Config.COMMAND_JOINGAME) {
				listener.valid = false;//设置监听器暂时失效
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}