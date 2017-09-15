package org.other.game.chinesechess.view;

import org.other.game.chinesechess.control.Listener;
import org.other.game.chinesechess.control.game.GameClient;
import org.other.game.chinesechess.model.ChessMan;
import org.other.game.chinesechess.util.ChessManFactory;
import org.other.game.chinesechess.util.GameRule;
import org.other.game.chinesechess.util.MathUtil;
import org.other.game.chinesechess.Config;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GamePanel extends JPanel{
	public static ChessMan[] chessMans = new ChessMan[32];
	private Listener listener;
	private int team;
	public JLabel board;
	public JLabel select = null;
	public ChessMan selectChessMan = null;
	
	public GamePanel(int team) {
		listener = new Listener(this);
		this.team = team;
		setLayout(null);
		initChessMans();
		board = new JLabel(new ImageIcon(Config.IMAGEPATHMAP.get("board")));
		add(board);
		board.addMouseListener(listener);
		board.setBounds(0, 0, 521, 577);
	}
	
	private void initChessMans() {
		//车
		chessMans[0] = ChessManFactory.createChessMan("BC");
		chessMans[1] = ChessManFactory.createChessMan("BC");
		chessMans[2] = ChessManFactory.createChessMan("RC");
		chessMans[3] = ChessManFactory.createChessMan("RC");
		initLocation(chessMans[0], 0, 9);
		initLocation(chessMans[1], 8, 9);
		initLocation(chessMans[2], 0, 0);
		initLocation(chessMans[3], 8, 0);
		//马
		chessMans[4] = ChessManFactory.createChessMan("BM");
		chessMans[5] = ChessManFactory.createChessMan("BM");
		chessMans[6] = ChessManFactory.createChessMan("RM");
		chessMans[7] = ChessManFactory.createChessMan("RM");
		initLocation(chessMans[4], 1, 9);
		initLocation(chessMans[5], 7, 9);
		initLocation(chessMans[6], 1, 0);
		initLocation(chessMans[7], 7, 0);
		//炮
		chessMans[8] = ChessManFactory.createChessMan("BP");
		chessMans[9] = ChessManFactory.createChessMan("BP");
		chessMans[10] = ChessManFactory.createChessMan("RP");
		chessMans[11] = ChessManFactory.createChessMan("RP");
		initLocation(chessMans[8], 1, 7);
		initLocation(chessMans[9], 7, 7);
		initLocation(chessMans[10], 1, 2);
		initLocation(chessMans[11], 7, 2);
		//相(象)
		chessMans[12] = ChessManFactory.createChessMan("BX");
		chessMans[13] = ChessManFactory.createChessMan("BX");
		chessMans[14] = ChessManFactory.createChessMan("RX");
		chessMans[15] = ChessManFactory.createChessMan("RX");
		initLocation(chessMans[12], 2, 9);
		initLocation(chessMans[13], 6, 9);
		initLocation(chessMans[14], 2, 0);
		initLocation(chessMans[15], 6, 0);
		//士
		chessMans[16] = ChessManFactory.createChessMan("BS");
		chessMans[17] = ChessManFactory.createChessMan("BS");
		chessMans[18] = ChessManFactory.createChessMan("RS");
		chessMans[19] = ChessManFactory.createChessMan("RS");
		initLocation(chessMans[16], 3, 9);
		initLocation(chessMans[17], 5, 9);
		initLocation(chessMans[18], 3, 0);
		initLocation(chessMans[19], 5, 0);
		//卒(兵)
		chessMans[20] = ChessManFactory.createChessMan("BZ");
		chessMans[21] = ChessManFactory.createChessMan("BZ");
		chessMans[22] = ChessManFactory.createChessMan("BZ");
		chessMans[23] = ChessManFactory.createChessMan("BZ");
		chessMans[24] = ChessManFactory.createChessMan("BZ");
		chessMans[25] = ChessManFactory.createChessMan("RZ");
		chessMans[26] = ChessManFactory.createChessMan("RZ");
		chessMans[27] = ChessManFactory.createChessMan("RZ");
		chessMans[28] = ChessManFactory.createChessMan("RZ");
		chessMans[29] = ChessManFactory.createChessMan("RZ");
		initLocation(chessMans[20], 0, 6);
		initLocation(chessMans[21], 2, 6);
		initLocation(chessMans[22], 4, 6);
		initLocation(chessMans[23], 6, 6);
		initLocation(chessMans[24], 8, 6);
		initLocation(chessMans[25], 0, 3);
		initLocation(chessMans[26], 2, 3);
		initLocation(chessMans[27], 4, 3);
		initLocation(chessMans[28], 6, 3);
		initLocation(chessMans[29], 8, 3);
		//将(帅)
		chessMans[30] = ChessManFactory.createChessMan("BJ");
		chessMans[31] = ChessManFactory.createChessMan("RJ");
		initLocation(chessMans[30], 4, 9);
		initLocation(chessMans[31], 4, 0);
		
		for (int i=0; i<chessMans.length; i++) {
			if (chessMans[i] != null) {
				add(chessMans[i]);
				chessMans[i].addMouseListener(listener);
			}
		}
	}
	
	/**
	 * 以左下角为原点 每个格子长度为1进行布局 (9*10)
	 * 记录棋子布局 黑方实行转置
	 * @param obj
	 * @param x
	 * @param y
	 */
	public void initLocation(ChessMan obj, int x, int y) {
		if (team == Config.TEAM_RED) {
			//记录棋子布局
			recordLayout(obj, x, y);
			obj.setBounds(Config.GAME_BEGIN_X + x * Config.CHESSMAN_LENGTH,
					Config.GAME_BEGIN_Y + (9-y) * Config.CHESSMAN_LENGTH,
					Config.CHESSMAN_LENGTH, Config.CHESSMAN_LENGTH);
		} else {
			//记录棋子布局
			recordLayout(obj, 8-x, 9-y);
			obj.setBounds(Config.GAME_BEGIN_X + (8-x) * Config.CHESSMAN_LENGTH,
					Config.GAME_BEGIN_Y + y * Config.CHESSMAN_LENGTH,
					Config.CHESSMAN_LENGTH, Config.CHESSMAN_LENGTH);
		}
	}
	
	/**
	 * 以左下角为原点 每个格子长度为1进行布局 (9*10)
	 * @param obj
	 * @param x
	 * @param y
	 */
	public void setLocation(ChessMan obj, int x, int y) {
		//记录棋子布局
		recordLayout(obj, x, y);
		obj.setBounds(Config.GAME_BEGIN_X + x * Config.CHESSMAN_LENGTH,
				Config.GAME_BEGIN_Y + (9-y) * Config.CHESSMAN_LENGTH,
				Config.CHESSMAN_LENGTH, Config.CHESSMAN_LENGTH);
	}

	/**
	 * 以左下角为原点 每个格子长度为1进行布局 (9*10) 
	 * 移动棋子或吃棋子的坐标事件
	 * @param obj
	 * @param 鼠标点击事件
	 */
	public void action(ChessMan oldObj, int x2, int y2) {
		clearSelect();
		updateSelect(oldObj);
		setLocation(oldObj, x2, y2);
	}
	
	/**
	 * 更新选中标志
	 * @param obj
	 */
	private void updateSelect(ChessMan oldObj) {
		selectChessMan = oldObj;
		oldObj.setSelectAndIcon(true);
		select = new JLabel(new ImageIcon(Config.IMAGEPATHMAP.get("select")));
		select.setBounds(oldObj.getX(), oldObj.getY(), Config.CHESSMAN_LENGTH, Config.CHESSMAN_LENGTH);
		board.add(select);
		repaint();
	}
	
	/**
	 * 清除选中标志
	 * @param obj
	 */
	public void clearSelect() {
		if (select != null) {
			board.remove(select);
		}
		if (selectChessMan != null) {
			selectChessMan.setSelectAndIcon(false);
		}
		repaint();
	}
	
	/**
	 * 记录棋子布局,原地点为false 新地点为true
	 * @param obj
	 * @param x
	 * @param y
	 */
	private void recordLayout(ChessMan obj, int x, int y) {
		//GameRule.aliveChessMans[MathUtil.parseX(obj.getX())][MathUtil.parseY(obj.getY())] = null;
		GameRule.aliveChessMans[x][y] = obj;
		GameRule.isAliveChessMans[MathUtil.parseX(obj.getX())][MathUtil.parseY(obj.getY())] = false;
		GameRule.isAliveChessMans[x][y] = true;
	}

	public void setGameClient(GameClient gameClient) {
		this.listener.setGameClient(gameClient);
		//this.gameClient = gameClient;
	}
	
	public Listener getListener() {
		return listener;
	}
	
}
