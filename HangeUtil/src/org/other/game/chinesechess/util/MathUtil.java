package org.other.game.chinesechess.util;

import org.other.game.chinesechess.Config;

public class MathUtil {
	
	/**
	 * xת��
	 * @param x
	 * @return
	 */
	public static int parseX(int x) {
		return (x - Config.GAME_BEGIN_X) / Config.CHESSMAN_LENGTH;
	}
	
	/**
	 * yת��
	 * @param y
	 * @return
	 */
	public static int parseY(int y) {
		return 9 - (y - Config.GAME_BEGIN_Y) / Config.CHESSMAN_LENGTH;
	}
}
