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
	private int currentCardIndex = -1;//�˲��������û�����
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
			//��¼��ť������󣬻�ȡ�����ı�ֵת��int���õ�ǰ�Ŀ����±꣬�����򵯴���ʾ
			try {
				int kahao = Integer.parseInt(text_kahao.getText());
				if (kahao < 0) {
					showErrorDialog("���Ų���Ϊ������");
					return;
				}
				boolean isfind = setCardIndex(kahao);
				if (isfind) {
					//��¼�ɹ� ��תҳ�� ���� currentCardIndex
					currentCardIndex = getCardIndex(kahao);
					mainWindow.setPage(new HandlePage(this));
					return;
				} else {
					showErrorDialog("�ÿ��Ų����ڣ�");
				}
			} catch (Exception exp) {
				if ("root".equals(text_kahao.getText())) {
					mainWindow.setPage(new RootPage(this));
				} else {
					showErrorDialog("�ÿ��Ÿ�ʽ����");
				}
			}
		} else if (e.getActionCommand().equals("add")) {
			//��ӳ�ʼ��Ϣ������󣬵���������Ϣ�����ȷ������
			if (cardnum == M) {
				showErrorDialog("���п��Ѵ����ޣ�");
				return ;
			}
			String str = showInputDialog("��ӿ�����Ϣ", "���������뿨�źͳ�ʼ���ÿո����:\n");
			if (str == null) {
				//���ȡ�� ʲô��������
				return;
			}
			int cardId;
			int money;
			try {
				String[] data = str.split(" ");
				cardId = Integer.parseInt(data[0]);
				money = Integer.parseInt(data[1]);
				if (cardId < 0) {
					showErrorDialog("���Ų���С��0��");
					return;
				}
			} catch (Exception exp) {
				showErrorDialog("������Ϣ����");
				return;
			}
			addCard(cardId, money);
			area_cards.setText(getCardInfo());//ˢ���ı�������
		} else if (e.getActionCommand().equals("remove")) {
			//�Ƴ����п�������󣬵���������Ϣ�����ȷ������
			if (cardnum == 0) {
				showErrorDialog("�������п���Ϣ��");
				return ;
			}
			String str = showInputDialog("ɾ��������Ϣ", "�����뿨��:\n");
			if (str == null) {
				//���ȡ�� ʲô��������
				return;
			}
			int cardId;
			try {
				cardId = Integer.parseInt(str.trim());
				if (cardId < 0) {
					showErrorDialog("���Ų���С��0��");
					return;
				}
			} catch (Exception exp) {
				showErrorDialog("������Ϣ����");
				return;
			}
			removeCard(cardId);
			area_cards.setText(getCardInfo());//ˢ���ı�������
		} else if (e.getActionCommand().equals("show")) {
			//����鿴�������п���Ϣ,����Ϣ������ı����С�
			area_cards.setText(getCardInfo());
		} else if (e.getActionCommand().equals("deposit")) {
			//������ card[currentCardIndex]Ϊ��ǰ�û��Ŀ�
			int czs = card[currentCardIndex][2];//������
			if (czs == N) {
				showErrorDialog("�����Ѵ����ޣ�");
				return ;
			}
			String str = showInputDialog("���", "����������:\n");
			if (str == null) {
				//���ȡ�� ʲô��������
				return;
			}
			int money;
			try {
				money = Integer.parseInt(str.trim());
				if (money < 0) {
					showErrorDialog("������Ϊ������");
					return;
				}
			} catch (Exception exp) {
				showErrorDialog("������Ϣ����");
				return;
			}
			//��¼����
			card[currentCardIndex][3 + czs++] = money;
			card[currentCardIndex][1] += money;
			card[currentCardIndex][2]++;
			showMessageDialog("�������ɹ���");
			area_card_info.setText(getCardInfo(currentCardIndex));//ˢ�²�ѯ��Ϣ
		} else if (e.getActionCommand().equals("withdraw")) {
			//���ȡ�� card[currentCardIndex]Ϊ��ǰ�û��Ŀ�
			int czs = card[currentCardIndex][2];//������
			if (czs == N) {
				showErrorDialog("�����Ѵ����ޣ�");
				return ;
			}
			String str = showInputDialog("ȡ��", "������ȡ����:\n");
			if (str == null) {
				//���ȡ�� ʲô��������
				return;
			}
			int money;
			try {
				money = Integer.parseInt(str.trim());
				if (money < 0) {
					showErrorDialog("ȡ�����Ϊ������");
					return;
				} else if (money > card[currentCardIndex][1]) {
					showErrorDialog("�����㣡");
					return;
				}
			} catch (Exception exp) {
				showErrorDialog("������Ϣ����");
				return;
			}
			//��¼����
			card[currentCardIndex][3 + czs++] = -1 * money;
			card[currentCardIndex][1] -= money;
			card[currentCardIndex][2]++;
			showMessageDialog("ȡ������ɹ���");
			area_card_info.setText(getCardInfo(currentCardIndex));//ˢ�²�ѯ��Ϣ
		} else if (e.getActionCommand().equals("query")) {
			//�����ѯ currentCardIndexΪ��ǰ�û��Ŀ��±�
			area_card_info.setText(getCardInfo(currentCardIndex));
		} else if (e.getActionCommand().equals("exit")) {
			//���س�ʼ����
			mainWindow.setPage(new HelloPage(this));
		}
	}

	/**
	 * ��ȡ�������п���Ϣ
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
	 * ���ݿ��Ż�ȡ���п���Ϣ
	 * @param cardIndex
	 * @return
	 */
	private String getCardInfo(int cardIndex) {
		StringBuffer sb = new StringBuffer();
		sb.append("���ţ�" + card[cardIndex][0]);
		sb.append("  ��" + card[cardIndex][1]);
		sb.append("  ҵ������" + card[cardIndex][2]);
		for (int i=0; i<card[cardIndex][2]; i++) {
			sb.append("\n\t����"+ (i+1) + ":");
			if (card[cardIndex][3+i] > 0) {
				sb.append("��Ǯ" + card[cardIndex][3+i] +"Ԫ");
			} else {
				sb.append("ȡǮ" + -1 * card[cardIndex][3+i] +"Ԫ");
			}
		}
		sb.append("\n");
		return sb.toString();
	}
	
	/**
	 * �Ƴ����п�
	 * @param cardId
	 */
	private void removeCard(int cardId) {
		int index = getCardIndex(cardId);
		if (index == -1) {
			showErrorDialog("�˿��Ų����ڣ�");
			return;
		}
		for (int i=index; i<M-1; i++) {
			card[i] = card[i+1];
		}
		cardnum--;
	}
	
	/**
	 * ������п�
	 * @param cardId
	 * @param money
	 */
	private void addCard(int cardId, int money) {
		int index = getCardIndex(cardId);
		if (index != -1) {
			showErrorDialog("�˿����Ѿ����ڣ������ظ����룡");
			return;
		}
		card[cardnum][0] = cardId;
		card[cardnum][1] = money;
		cardnum++;
	}
	
	/**
	 * ���ݿ��Ż�ȡ�±겢����currentCardIndex��ʧ�ܷ���false
	 * @param cardId
	 * @return
	 */
	private boolean setCardIndex(int cardId) {
		currentCardIndex = getCardIndex(cardId);
		return currentCardIndex != -1;
	}
	
	/**
	 * ���ݿ��Ż�ȡ�±�,���𷵻�-1
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
	 * ��ʾ����
	 * @param message
	 */
	private void showErrorDialog(String message) {
		JOptionPane.showMessageDialog(mainWindow, message, "����", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * ��ʾ����
	 * @param message
	 */
	private void showMessageDialog(String message) {
		JOptionPane.showMessageDialog(mainWindow, message, "��ʾ", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * ���봰��
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
