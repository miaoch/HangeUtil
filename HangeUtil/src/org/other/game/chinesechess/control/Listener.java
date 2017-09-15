package org.other.game.chinesechess.control;

import org.other.game.chinesechess.control.game.GameClient;
import org.other.game.chinesechess.model.ChessMan;
import org.other.game.chinesechess.util.GameRule;
import org.other.game.chinesechess.util.MathUtil;
import org.other.game.chinesechess.view.ChessGame;
import org.other.game.chinesechess.view.GamePanel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Listener extends MouseAdapter {
	private static GameClient gameClient;
	//上次点击的棋子
	private ChessMan lastSelectChessMan = null;
	private GamePanel gamePanel;
	private Flash flashThread;
	public boolean valid;
	
	public Listener(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (valid) {
			Object obj = e.getSource();
			//如果点到了棋子
			if (obj instanceof ChessMan) {
				ChessMan clickObj = (ChessMan) obj;
				if (clickObj.isSelect()) {
					cancelSelect();
				} else {
					if (lastSelectChessMan != null) {
						lastSelectChessMan.eat(clickObj);
					} else if (ChessGame.player.getTeam() == clickObj.getTeam()) {
						selectAndFlash(clickObj);
					}
				} 
			} else if (GameRule.isAliveChessMans[MathUtil.parseX(e.getX())][MathUtil.parseY(e.getY())]) {
				//点到了棋盘 如果该点有棋子 只是刚好闪烁没点到 执行取消闪烁
				cancelSelect();
			} else if (lastSelectChessMan != null) {
				lastSelectChessMan.goTo(e);
			}
		}
	}

	/**
	 * 选中并闪烁棋子
	 * @param clickObj
	 */
	private void selectAndFlash(ChessMan clickObj) {
		gamePanel.clearSelect();
		lastSelectChessMan = clickObj;
		lastSelectChessMan.setSelectAndIcon(true);
		flashThread = new Flash(lastSelectChessMan);
		flashThread.start();
	}

	/**
	 * 取消选中棋子
	 */
	private void cancelSelect() {
		lastSelectChessMan.setSelectAndIcon(false);
		lastSelectChessMan = null;
		if (flashThread != null) {
			flashThread.stopAndShow();
		}
	}
	
	/**
	 * 移动棋子
	 * @param e
	 */
	public void goTo(int x1, int y1, int x2, int y2) {
		ChessMan chessman = GameRule.aliveChessMans[x1][y1];
		gamePanel.action(chessman, x2, y2);
		if (lastSelectChessMan != null) {
			lastSelectChessMan.setSelect(false);
			lastSelectChessMan = null;
		}
		if (flashThread != null) {
			flashThread.stopAndShow();
		}
		gamePanel.repaint();
	}
	
	/**
	 * 消灭棋子
	 * @param clickObj
	 */
	public void eat(int x1, int y1, int x2, int y2) {
		ChessMan chessman = GameRule.aliveChessMans[x1][y1];
		ChessMan deathChessMan = GameRule.aliveChessMans[x2][y2];
		gamePanel.action(chessman, x2, y2);
		gamePanel.remove(deathChessMan);
		if (lastSelectChessMan != null) {
			lastSelectChessMan.setSelect(false);
			lastSelectChessMan = null;
		}
		if (flashThread != null) {
			flashThread.stopAndShow();
		}
		gamePanel.repaint();
	}
	

	public void setGameClient(GameClient gameClient1) {
		gameClient = gameClient1;
	}
	public static GameClient getGameClient() {
		return gameClient;
	}

}
