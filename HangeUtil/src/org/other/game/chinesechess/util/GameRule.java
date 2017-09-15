package org.other.game.chinesechess.util;

import org.other.game.chinesechess.model.ChessMan;
import org.other.game.chinesechess.view.GamePanel;

public class GameRule {
	public static ChessMan[][] aliveChessMans = new ChessMan[9][10];
	public static boolean[][] isAliveChessMans = new boolean[9][10];
	
	/**
	 * 车的走法
	 * @param me
	 * @return
	 */
	protected static boolean cheGoTo(int x1, int y1, int x2, int y2) {
		if (x1 == x2) {
			int maxY = y2 - y1 > 0? y2: y1;
			int minY = y2 - y1 > 0? y1: y2;
			for (int i=1; i<maxY-minY; i++) {
				if (isAliveChessMans[x1][minY + i]) {
					return false;
				}
			}
			return true;
		} else if (y1 == y2) {
			int maxX = x2 - x1 > 0? x2: x1;
			int minX = x2 - x1 > 0? x1: x2;
			for (int i=1; i<maxX-minX; i++) {
				if (isAliveChessMans[minX + i][y1]) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 车的吃法 与走法完全相同
	 * @param me
	 * @return
	 */
	protected static boolean cheEat(int x1, int y1, int x2, int y2) {
		return cheGoTo(x1, y1, x2, y2);
	}
	
	/**
	 * 马的走法
	 * @param me
	 * @return
	 */
	protected static boolean maGoTo(int x1, int y1, int x2, int y2) {
		//马的八种走法
		if ((x2 == x1 + 1 || x2 == x1 - 1) && (y2 == y1 + 2 || y2 == y1 - 2)) {
			//寻找马脚位置 x=x1 y=(y2+y1)/2
			return !isAliveChessMans[x1][(y2 + y1) / 2];
		} else if ((x2 == x1 + 2 || x2 == x1 - 2 ) && (y2 == y1 + 1 || y2 == y1 - 1)) {
			//寻找马脚位置 x=(x2+x1)/2 y=y1
			return !isAliveChessMans[(x2 + x1) / 2][y1];
		}
		return false;
	}
	
	/**
	 * 马的吃法 与走法完全相同
	 * @param eater
	 * @return
	 */
	protected static boolean maEat(int x1, int y1, int x2, int y2) {
		return maGoTo(x1, y1, x2, y2);
	}
	
	/**
	 * 炮的走法 走法和车相同 使用车的走法
	 * @param me
	 * @return
	 */
	protected static boolean paoGoTo(int x1, int y1, int x2, int y2) {
		return cheGoTo(x1, y1, x2, y2);
	}
	
	/**
	 * 炮的吃法
	 * @param eater
	 * @return
	 */
	protected static boolean paoEat(int x1, int y1, int x2, int y2) {
		int count = 0;
		if (x1 == x2) {
			int maxY = y2 - y1 > 0? y2: y1;
			int minY = y2 - y1 > 0? y1: y2;
			for (int i=1; i<maxY-minY; i++) {
				if (isAliveChessMans[x1][minY + i]) {
					count++;
				}
			}
			return count == 1;
		} else if (y1 == y2) {
			int maxX = x2 - x1 > 0? x2: x1;
			int minX = x2 - x1 > 0? x1: x2;
			for (int i=1; i<maxX-minX; i++) {
				if (isAliveChessMans[minX + i][y1]) {
					count++;
				}
			}
			return count == 1;
		}
		return false;
	}
	
	/**
	 * 将(帅)的走法
	 * @param me
	 * @return
	 */
	protected static boolean jiangGoTo(int x1, int y1, int x2, int y2) {
		//不在城墙内的走法 默认拒绝
		if (x2 < 3 || x2 > 5 || y2 > 2 && y2 < 7) {
			return false;
		}
		if (x1 == x2) {
			return Math.abs(y2 - y1) == 1;
		} else if (y1 == y2){
			return Math.abs(x2 - x1) == 1;
		}
		return false;
	}
	
	/**
	 * 将(帅)的吃法
	 * @param eater
	 * @return
	 */
	protected static boolean jiangEat(int x1, int y1, int x2, int y2) {
		//不在城墙内的走法 默认拒绝
		if (x2 < 3 || x2 > 5 || y2 > 2 && y2 < 7) {
			return false;
		}
		if (x1 == x2) {
			if (Math.abs(y2 - y1) == 1) {
				return true;
			} else if (MathUtil.parseX(GamePanel.chessMans[30].getX()) == x1 
					&& MathUtil.parseY(GamePanel.chessMans[30].getY()) == y1
					&& MathUtil.parseX(GamePanel.chessMans[31].getX()) == x2 
					&& MathUtil.parseY(GamePanel.chessMans[31].getY()) == y2 
					|| MathUtil.parseX(GamePanel.chessMans[31].getX()) == x1 
					&& MathUtil.parseY(GamePanel.chessMans[31].getY()) == y1
					&& MathUtil.parseX(GamePanel.chessMans[30].getX()) == x2 
					&& MathUtil.parseY(GamePanel.chessMans[30].getY()) == y2) {
				int maxY = y2 - y1 > 0? y2: y1;
				int minY = y2 - y1 > 0? y1: y2;
				for (int i=1; i<maxY-minY; i++) {
					if (isAliveChessMans[x1][minY + i]) {
						return false;
					}
				}
				return true;
			}
		} else if (y1 == y2){
			return Math.abs(x2 - x1) == 1;
		}
		return false;
	}
	
	/**
	 * 士的走法
	 * @param me
	 * @return
	 */
	protected static boolean shiGoTo(int x1, int y1, int x2, int y2) {
		//不在城墙内的走法 默认拒绝
		if (x2 < 3 || x2 > 5 || y2 > 2 && y2 < 7) {
			return false;
		}
		return Math.abs(x1 - x2) == 1 && Math.abs(y1 - y2) == 1;
	}
	
	/**
	 * 士的吃法 同吃法
	 * @param eater
	 * @return
	 */
	protected static boolean shiEat(int x1, int y1, int x2, int y2) {
		return shiGoTo(x1, y1, x2, y2);
	}
	
	/**
	 * 相(象)的走法
	 * @param me
	 * @return
	 */
	protected static boolean xiangGoTo(int x1, int y1, int x2, int y2) {
		//不能过河
		if (y1 <= 4 && y2 >= 5 || y2 <= 4 && y1 >= 5 ) {
			return false;
		}
		return Math.abs(x1-x2) == 2 
				&& Math.abs(y1-y2) == 2 
				&& !isAliveChessMans[(x1 + x2) / 2][(y1 + y2) / 2];
	}
	
	/**
	 * 相(象)的吃法 同走法
	 * @param eater
	 * @return
	 */
	protected static boolean xiangEat(int x1, int y1, int x2, int y2) {
		return xiangGoTo(x1, y1, x2, y2);
	}
	
	/**
	 * 卒(兵)的走法 此处默认自身为下方玩家 (不然无法判断走法，虽然上面的走法都是通用的)
	 * @param me
	 * @return
	 */
	protected static boolean zuGoTo(int x1, int y1, int x2, int y2) {
		//往前走
		if (x1 == x2 && y2 == y1 + 1) {
			return true;
		} else if (y1 >= 5 && y2 == y1 && Math.abs(x1 - x2) == 1) { 
			//过河左右走
			return true;
		}
		return false;
	}
	
	/**
	 * 卒(兵)的吃法 同走法
	 * @param eater
	 * @return
	 */
	protected static boolean zuEat(int x1, int y1, int x2, int y2) {
		return zuGoTo(x1, y1, x2, y2);
	}
	
}
