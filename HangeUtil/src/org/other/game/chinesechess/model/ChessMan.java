package org.other.game.chinesechess.model;

import org.other.game.chinesechess.Config;

import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * 棋子祖先类
 * @author miaoch
 *
 */
public abstract class ChessMan extends JLabel {
	private String type;//类型
	private int team;//1:红方  2:黑方
	private boolean select;//是否被选中
	
	public ChessMan(String type) {
		super();
		this.type = type;
		this.team = type.startsWith("R")? 1 : 2;
		setIcon(new ImageIcon(Config.IMAGEPATHMAP.get(type)));
	}
	
	/**
	 * 棋子的走法
	 * @param me 鼠标点击事件
	 * @return
	 */
	public abstract boolean goTo(MouseEvent me);
	
	/**
	 * 棋子的吃法
	 * @param eater 被点击的ChessMan
	 * @return
	 */
	public abstract boolean eat(ChessMan eater);

	public String getType() {
		return type;
	}
	
	public int getTeam() {
		return team;
	}

	public boolean isSelect() {
		return select;
	}

	/*
	 * 设置选中状态 并且修改图标
	 */
	public void setSelectAndIcon(boolean select) {
		this.select = select;
		if (select) {
			setIcon(new ImageIcon(Config.IMAGEPATHMAP.get(type + "S")));
		} else {
			setIcon(new ImageIcon(Config.IMAGEPATHMAP.get(type)));
		}
	}
	
	/*
	 * 设置选中状态 并且修改图标
	 */
	public void setSelect(boolean select) {
		this.select = select;
	}
	
}
