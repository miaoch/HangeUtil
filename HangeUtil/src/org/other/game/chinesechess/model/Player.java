package org.other.game.chinesechess.model;

import java.io.Serializable;

/**
 * 玩家信息类
 * @author Administrator
 *
 */
public class Player implements Serializable {
	private String name;
	private int team; // 1:红 2:黑
	
	public Player(String name, int team) {
		setName(name);
		setTeam(team);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTeam() {
		return team;
	}

	public void setTeam(int team) {
		this.team = team;
	}
}
