package org.other.game.chinesechess.control;

import org.other.game.chinesechess.model.ChessMan;

public class Flash extends Thread {
	private ChessMan chessMan;
	
	public Flash(ChessMan chessMan) {
		this.chessMan = chessMan;
	}
	
	@Override
	public void run() {
		while (true) {
			chessMan.setVisible(false);
			try {
				Thread.sleep(500);
			} catch(Exception e) {}
			chessMan.setVisible(true);
			try {
				Thread.sleep(500);
			} catch (Exception e) {}
		}
	}
	
	public void stopAndShow() {
		stop();
		chessMan.setVisible(true);
	}
	
}
