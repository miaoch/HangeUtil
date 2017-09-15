package org.other.game.chinesechess.util;

import org.other.game.chinesechess.model.ChessMan;
import org.other.game.chinesechess.view.GamePanel;

public class GameRule {
	public static ChessMan[][] aliveChessMans = new ChessMan[9][10];
	public static boolean[][] isAliveChessMans = new boolean[9][10];
	
	/**
	 * �����߷�
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
	 * ���ĳԷ� ���߷���ȫ��ͬ
	 * @param me
	 * @return
	 */
	protected static boolean cheEat(int x1, int y1, int x2, int y2) {
		return cheGoTo(x1, y1, x2, y2);
	}
	
	/**
	 * ����߷�
	 * @param me
	 * @return
	 */
	protected static boolean maGoTo(int x1, int y1, int x2, int y2) {
		//��İ����߷�
		if ((x2 == x1 + 1 || x2 == x1 - 1) && (y2 == y1 + 2 || y2 == y1 - 2)) {
			//Ѱ�����λ�� x=x1 y=(y2+y1)/2
			return !isAliveChessMans[x1][(y2 + y1) / 2];
		} else if ((x2 == x1 + 2 || x2 == x1 - 2 ) && (y2 == y1 + 1 || y2 == y1 - 1)) {
			//Ѱ�����λ�� x=(x2+x1)/2 y=y1
			return !isAliveChessMans[(x2 + x1) / 2][y1];
		}
		return false;
	}
	
	/**
	 * ��ĳԷ� ���߷���ȫ��ͬ
	 * @param eater
	 * @return
	 */
	protected static boolean maEat(int x1, int y1, int x2, int y2) {
		return maGoTo(x1, y1, x2, y2);
	}
	
	/**
	 * �ڵ��߷� �߷��ͳ���ͬ ʹ�ó����߷�
	 * @param me
	 * @return
	 */
	protected static boolean paoGoTo(int x1, int y1, int x2, int y2) {
		return cheGoTo(x1, y1, x2, y2);
	}
	
	/**
	 * �ڵĳԷ�
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
	 * ��(˧)���߷�
	 * @param me
	 * @return
	 */
	protected static boolean jiangGoTo(int x1, int y1, int x2, int y2) {
		//���ڳ�ǽ�ڵ��߷� Ĭ�Ͼܾ�
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
	 * ��(˧)�ĳԷ�
	 * @param eater
	 * @return
	 */
	protected static boolean jiangEat(int x1, int y1, int x2, int y2) {
		//���ڳ�ǽ�ڵ��߷� Ĭ�Ͼܾ�
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
	 * ʿ���߷�
	 * @param me
	 * @return
	 */
	protected static boolean shiGoTo(int x1, int y1, int x2, int y2) {
		//���ڳ�ǽ�ڵ��߷� Ĭ�Ͼܾ�
		if (x2 < 3 || x2 > 5 || y2 > 2 && y2 < 7) {
			return false;
		}
		return Math.abs(x1 - x2) == 1 && Math.abs(y1 - y2) == 1;
	}
	
	/**
	 * ʿ�ĳԷ� ͬ�Է�
	 * @param eater
	 * @return
	 */
	protected static boolean shiEat(int x1, int y1, int x2, int y2) {
		return shiGoTo(x1, y1, x2, y2);
	}
	
	/**
	 * ��(��)���߷�
	 * @param me
	 * @return
	 */
	protected static boolean xiangGoTo(int x1, int y1, int x2, int y2) {
		//���ܹ���
		if (y1 <= 4 && y2 >= 5 || y2 <= 4 && y1 >= 5 ) {
			return false;
		}
		return Math.abs(x1-x2) == 2 
				&& Math.abs(y1-y2) == 2 
				&& !isAliveChessMans[(x1 + x2) / 2][(y1 + y2) / 2];
	}
	
	/**
	 * ��(��)�ĳԷ� ͬ�߷�
	 * @param eater
	 * @return
	 */
	protected static boolean xiangEat(int x1, int y1, int x2, int y2) {
		return xiangGoTo(x1, y1, x2, y2);
	}
	
	/**
	 * ��(��)���߷� �˴�Ĭ������Ϊ�·���� (��Ȼ�޷��ж��߷�����Ȼ������߷�����ͨ�õ�)
	 * @param me
	 * @return
	 */
	protected static boolean zuGoTo(int x1, int y1, int x2, int y2) {
		//��ǰ��
		if (x1 == x2 && y2 == y1 + 1) {
			return true;
		} else if (y1 >= 5 && y2 == y1 && Math.abs(x1 - x2) == 1) { 
			//����������
			return true;
		}
		return false;
	}
	
	/**
	 * ��(��)�ĳԷ� ͬ�߷�
	 * @param eater
	 * @return
	 */
	protected static boolean zuEat(int x1, int y1, int x2, int y2) {
		return zuGoTo(x1, y1, x2, y2);
	}
	
}
