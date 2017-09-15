package org.other.game.chinesechess.model;

import org.other.game.chinesechess.Config;

import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * ����������
 * @author miaoch
 *
 */
public abstract class ChessMan extends JLabel {
	private String type;//����
	private int team;//1:�췽  2:�ڷ�
	private boolean select;//�Ƿ�ѡ��
	
	public ChessMan(String type) {
		super();
		this.type = type;
		this.team = type.startsWith("R")? 1 : 2;
		setIcon(new ImageIcon(Config.IMAGEPATHMAP.get(type)));
	}
	
	/**
	 * ���ӵ��߷�
	 * @param me ������¼�
	 * @return
	 */
	public abstract boolean goTo(MouseEvent me);
	
	/**
	 * ���ӵĳԷ�
	 * @param eater �������ChessMan
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
	 * ����ѡ��״̬ �����޸�ͼ��
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
	 * ����ѡ��״̬ �����޸�ͼ��
	 */
	public void setSelect(boolean select) {
		this.select = select;
	}
	
}
