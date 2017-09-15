package org.other.game.chinesechess.model;

import org.other.game.chinesechess.Config;

import java.io.Serializable;

/**
 * Ö¸ÁîÀà
 * @author miaoch
 *
 */
public class Command implements Serializable {
	private int type;//1:×ß 2:³Ô
	private int team;//1ºì 2ºÚ
	private int fromX;
	private int fromY;
	private int toX;
	private int toY;
	private Player player;
	
	public Command(int type, int team, int fromX, int fromY, int toX, int toY) {
		this.type = type;
		this.team = team;
		this.fromX = fromX;
		this.fromY = fromY;
		this.toX = toX;
		this.toY = toY;
	}
	
	public Command(int type, Player player, int team) {
		this.type = type;
		this.player = player;
		this.team = team;
	}
	
	public int getType() {
		return type;
	}
	public int getTeam() {
		return team;
	}
	public int getFormX() {
		return fromX;
	}
	public int getFormY() {
		return fromY;
	}
	public int getToX() {
		return toX;
	}
	public int getToY() {
		return toY;
	}
	public Player getPlayer() {
		return player;
	}
	
	public String toString() {
		return "command "+Config.TEAMNAMEMAP.get(team)+" "+Config.COMMANDMAP.get(type)+" : from point "
				+ "("+fromX+", "+fromY+") to point "
				+ "("+toX+", "+toY+") !";
	}

}
