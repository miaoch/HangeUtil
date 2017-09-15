package org.other.game.banksystem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class BankSystem implements ActionListener {
	private final int M = 3;
	private final int N = 5;
	private int [][]card = new int[M][N+3];
	private int currentCardIndex = -1;//此参数用于用户操作
	private int cardnum = 0;
	
	private JTextField text_kahao;
	private JTextArea area_cards;
	private JTextArea area_card_info;
	
	private MainWindow mainWindow;
	public BankSystem (MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("login")) {
			//登录按钮被点击后，获取卡号文本值转成int设置当前的卡号下标，错误则弹窗提示
			try {
				int kahao = Integer.parseInt(text_kahao.getText());
				if (kahao < 0) {
					showErrorDialog("卡号不能为负数！");
					return;
				}
				boolean isfind = setCardIndex(kahao);
				if (isfind) {
					//登录成功 跳转页面 设置 currentCardIndex
					currentCardIndex = getCardIndex(kahao);
					mainWindow.setPage(new HandlePage(this));
					return;
				} else {
					showErrorDialog("该卡号不存在！");
				}
			} catch (Exception exp) {
				if ("root".equals(text_kahao.getText())) {
					mainWindow.setPage(new RootPage(this));
				} else {
					showErrorDialog("该卡号格式错误！");
				}
			}
		} else if (e.getActionCommand().equals("add")) {
			//添加初始信息被点击后，弹窗输入信息，点击确定保存
			if (cardnum == M) {
				showErrorDialog("银行卡已达上限！");
				return ;
			}
			String str = showInputDialog("添加卡号信息", "请依次输入卡号和初始余额，用空格隔开:\n");
			if (str == null) {
				//点击取消 什么都不做。
				return;
			}
			int cardId;
			int money;
			try {
				String[] data = str.split(" ");
				cardId = Integer.parseInt(data[0]);
				money = Integer.parseInt(data[1]);
				if (cardId < 0) {
					showErrorDialog("卡号不能小于0！");
					return;
				}
			} catch (Exception exp) {
				showErrorDialog("输入信息有误！");
				return;
			}
			addCard(cardId, money);
			area_cards.setText(getCardInfo());//刷新文本区文字
		} else if (e.getActionCommand().equals("remove")) {
			//移除银行卡被点击后，弹窗输入信息，点击确定保存
			if (cardnum == 0) {
				showErrorDialog("不含银行卡信息！");
				return ;
			}
			String str = showInputDialog("删除卡号信息", "请输入卡号:\n");
			if (str == null) {
				//点击取消 什么都不做。
				return;
			}
			int cardId;
			try {
				cardId = Integer.parseInt(str.trim());
				if (cardId < 0) {
					showErrorDialog("卡号不能小于0！");
					return;
				}
			} catch (Exception exp) {
				showErrorDialog("输入信息有误！");
				return;
			}
			removeCard(cardId);
			area_cards.setText(getCardInfo());//刷新文本区文字
		} else if (e.getActionCommand().equals("show")) {
			//点击查看所有银行卡信息,将信息输出到文本区中。
			area_cards.setText(getCardInfo());
		} else if (e.getActionCommand().equals("deposit")) {
			//点击存款 card[currentCardIndex]为当前用户的卡
			int czs = card[currentCardIndex][2];//操作数
			if (czs == N) {
				showErrorDialog("操作已达上限！");
				return ;
			}
			String str = showInputDialog("存款", "请输入存款金额:\n");
			if (str == null) {
				//点击取消 什么都不做。
				return;
			}
			int money;
			try {
				money = Integer.parseInt(str.trim());
				if (money < 0) {
					showErrorDialog("存款金额不能为负数！");
					return;
				}
			} catch (Exception exp) {
				showErrorDialog("输入信息有误！");
				return;
			}
			//记录操作
			card[currentCardIndex][3 + czs++] = money;
			card[currentCardIndex][1] += money;
			card[currentCardIndex][2]++;
			showMessageDialog("存款操作成功！");
			area_card_info.setText(getCardInfo(currentCardIndex));//刷新查询信息
		} else if (e.getActionCommand().equals("withdraw")) {
			//点击取款 card[currentCardIndex]为当前用户的卡
			int czs = card[currentCardIndex][2];//操作数
			if (czs == N) {
				showErrorDialog("操作已达上限！");
				return ;
			}
			String str = showInputDialog("取款", "请输入取款金额:\n");
			if (str == null) {
				//点击取消 什么都不做。
				return;
			}
			int money;
			try {
				money = Integer.parseInt(str.trim());
				if (money < 0) {
					showErrorDialog("取款金额不能为负数！");
					return;
				} else if (money > card[currentCardIndex][1]) {
					showErrorDialog("您余额不足！");
					return;
				}
			} catch (Exception exp) {
				showErrorDialog("输入信息有误！");
				return;
			}
			//记录操作
			card[currentCardIndex][3 + czs++] = -1 * money;
			card[currentCardIndex][1] -= money;
			card[currentCardIndex][2]++;
			showMessageDialog("取款操作成功！");
			area_card_info.setText(getCardInfo(currentCardIndex));//刷新查询信息
		} else if (e.getActionCommand().equals("query")) {
			//点击查询 currentCardIndex为当前用户的卡下标
			area_card_info.setText(getCardInfo(currentCardIndex));
		} else if (e.getActionCommand().equals("exit")) {
			//返回初始界面
			mainWindow.setPage(new HelloPage(this));
		}
	}

	/**
	 * 获取所有银行卡信息
	 * @param cardIndex
	 * @return
	 */
	private String getCardInfo() {
		StringBuffer sb = new StringBuffer();
		for (int i=0; i<cardnum; i++) {
			sb.append(getCardInfo(i));
		}
		return sb.toString();
	}
	
	/**
	 * 根据卡号获取银行卡信息
	 * @param cardIndex
	 * @return
	 */
	private String getCardInfo(int cardIndex) {
		StringBuffer sb = new StringBuffer();
		sb.append("卡号：" + card[cardIndex][0]);
		sb.append("  余额：" + card[cardIndex][1]);
		sb.append("  业务数：" + card[cardIndex][2]);
		for (int i=0; i<card[cardIndex][2]; i++) {
			sb.append("\n\t操作"+ (i+1) + ":");
			if (card[cardIndex][3+i] > 0) {
				sb.append("存钱" + card[cardIndex][3+i] +"元");
			} else {
				sb.append("取钱" + -1 * card[cardIndex][3+i] +"元");
			}
		}
		sb.append("\n");
		return sb.toString();
	}
	
	/**
	 * 移除银行卡
	 * @param cardId
	 */
	private void removeCard(int cardId) {
		int index = getCardIndex(cardId);
		if (index == -1) {
			showErrorDialog("此卡号不存在！");
			return;
		}
		for (int i=index; i<M-1; i++) {
			card[i] = card[i+1];
		}
		cardnum--;
	}
	
	/**
	 * 添加银行卡
	 * @param cardId
	 * @param money
	 */
	private void addCard(int cardId, int money) {
		int index = getCardIndex(cardId);
		if (index != -1) {
			showErrorDialog("此卡号已经存在，不能重复输入！");
			return;
		}
		card[cardnum][0] = cardId;
		card[cardnum][1] = money;
		cardnum++;
	}
	
	/**
	 * 根据卡号获取下标并设置currentCardIndex，失败返回false
	 * @param cardId
	 * @return
	 */
	private boolean setCardIndex(int cardId) {
		currentCardIndex = getCardIndex(cardId);
		return currentCardIndex != -1;
	}
	
	/**
	 * 根据卡号获取下标,负责返回-1
	 * @param cardId
	 * @return
	 */
	private int getCardIndex(int cardId) {
		for (int i=0; i<cardnum; i++) {
			if (card[i][0] == cardId) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * 提示窗口
	 * @param message
	 */
	private void showErrorDialog(String message) {
		JOptionPane.showMessageDialog(mainWindow, message, "错误", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * 提示窗口
	 * @param message
	 */
	private void showMessageDialog(String message) {
		JOptionPane.showMessageDialog(mainWindow, message, "提示", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * 输入窗口
	 * @param title
	 * @param tips
	 * @return
	 */
	private String showInputDialog(String title, String tips) {
		return JOptionPane.showInputDialog(mainWindow, tips, title, JOptionPane.QUESTION_MESSAGE);
	}


	public void setKahao(JTextField text_kahao) {
		this.text_kahao = text_kahao;
	}
	
	public void setCards(JTextArea area_cards) {
		this.area_cards = area_cards;
	}
	
	public void setCardInfo(JTextArea area_card_info) {
		this.area_card_info = area_card_info;
	}
	
}
