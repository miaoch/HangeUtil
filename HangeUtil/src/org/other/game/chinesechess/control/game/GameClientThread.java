package org.other.game.chinesechess.control.game;

import org.other.game.chinesechess.Config;
import org.other.game.chinesechess.control.Listener;
import org.other.game.chinesechess.model.Command;
import org.other.game.chinesechess.view.ChessGame;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class GameClientThread extends Thread {
	private Socket clientSocket;
	private Listener lisener;
	private ChessGame chessGame;
	
	public GameClientThread(Socket clientSocket, ChessGame chessGame){
		this.chessGame = chessGame;
		this.clientSocket = clientSocket;
		this.lisener = chessGame.gamePanel.getListener();
	}
	@Override
	public void run() {
		while (true) {
			try {
				ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
				Command command = (Command) in.readObject();
				if (command != null) {
					//加入游戏命令
					if (command.getType() == Config.COMMAND_JOINGAME) {
						//如果是黑方发过来的 就从红方转发一次
						if (command.getTeam() == Config.TEAM_BLACK) {
							chessGame.gameClient.sendCommand(new Command(Config.COMMAND_JOINGAME, chessGame.player, Config.TEAM_RED));
							chessGame.infoPanel.setRed(chessGame.player.getName());
							chessGame.infoPanel.setBlack(command.getPlayer().getName());
						} else {
							chessGame.infoPanel.setRed(command.getPlayer().getName());
							chessGame.infoPanel.setBlack(chessGame.player.getName());
						}
						//TODO 红方黑方计时器启动 且立马暂停黑方计时器
						chessGame.infoPanel.timer_red.start();
						chessGame.infoPanel.timer_black.start();
						chessGame.infoPanel.timer_black.suspend();
						chessGame.repaint();
						continue;
					}
					int x1 = command.getFormX();
					int y1 = command.getFormY();
					int x2 = command.getToX();
					int y2 = command.getToY();
					if (command.getTeam() != ChessGame.player.getTeam()) {
						x1 = 8 - x1;
						x2 = 8 - x2;
						y1 = 9 - y1;
						y2 = 9 - y2;
						lisener.valid = true;//设置监听器有效
					}
					if (command.getType() == Config.COMMAND_GOTO) {
						lisener.goTo(x1, y1, x2, y2);
					} else if (command.getType() == Config.COMMAND_EAT){
						lisener.eat(x1, y1, x2, y2); 
					}
					if (command.getTeam() == Config.TEAM_RED) {
						//TODO 黑方计时器启动
						//TODO 红方计时器暂停
						chessGame.infoPanel.timer_black.resume();
						chessGame.infoPanel.timer_red.suspend();
					} else if (command.getTeam() == Config.TEAM_BLACK) {
						//TODO 红方计时器启动
						//TODO 黑方计时器暂停
						chessGame.infoPanel.timer_black.suspend();
						chessGame.infoPanel.timer_red.resume();
					}
				}
				System.out.println("收到信息 " + command.toString());
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("已经与服务器断开！");
				break;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
