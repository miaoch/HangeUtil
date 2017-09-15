package org.other.game.chinesechess.util;

import org.other.game.chinesechess.control.Listener;
import org.other.game.chinesechess.model.ChessMan;
import org.other.game.chinesechess.model.Command;
import org.other.game.chinesechess.view.ChessGame;
import org.other.game.chinesechess.Config;

import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

public class ChessManFactory {
	
	public static ChessMan createChessMan(String type) {
		type = type.toUpperCase();
		if (!checkType(type)) {
			return null;
		}
		try {
			Class rule = GameRule.class;
			final Method goTo = rule.getDeclaredMethod(parseMethodPrefix(type) + "GoTo", int.class, int.class, int.class, int.class);
			final Method eat = rule.getDeclaredMethod(parseMethodPrefix(type) + "Eat", int.class, int.class, int.class, int.class);
			return new ChessMan(type) {
				public boolean goTo(MouseEvent me) {
					try {
						int x1 = MathUtil.parseX(this.getX());
						int y1 = MathUtil.parseY(this.getY());
						int x2 = MathUtil.parseX(me.getX());
						int y2 = MathUtil.parseY(me.getY());
						if ((boolean) goTo.invoke(this, x1, y1, x2, y2)) {
							//TODO 发送走信息
							Listener.getGameClient().sendCommand(new Command(Config.COMMAND_GOTO, ChessGame.player.getTeam(), x1, y1, x2, y2));
							return true;
						} else {
							return false;
						}
					} catch (IllegalAccessException | IllegalArgumentException
							| InvocationTargetException e) {
						e.printStackTrace();
						return false;
					}
				}
				public boolean eat(ChessMan eater) {
					try {
						if (this.getTeam() == eater.getTeam()) {
							return false;
						}
						int x1 = MathUtil.parseX(this.getX());
						int y1 = MathUtil.parseY(this.getY());
						int x2 = MathUtil.parseX(eater.getX());
						int y2 = MathUtil.parseY(eater.getY());
						if ((boolean) eat.invoke(this, x1, y1, x2, y2)) {
							//TODO 发送吃信息
							Listener.getGameClient().sendCommand(new Command(Config.COMMAND_EAT, ChessGame.player.getTeam(), x1, y1, x2, y2));
							return true;
						} else {
							return false;
						}
					} catch (IllegalAccessException | IllegalArgumentException
							| InvocationTargetException e) {
						e.printStackTrace();
						return false;
					}
				}
			};
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
		
	/**
	 * 验证类型
	 * @param type
	 * @return
	 */
	private static boolean checkType(String type) {
		if (type.length() != 2) {
			return false;
		}
		Iterator<String> typeKey = Config.IMAGEPATHMAP.keySet().iterator();
		while (typeKey.hasNext()) {
			if (typeKey.next().equals(type)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 由类型获得方法前缀
	 * @param type
	 * @return
	 */
	private static String parseMethodPrefix(String type) {
		return Config.METHODPREFIXMAP.get(type.substring(1));
	}
}
