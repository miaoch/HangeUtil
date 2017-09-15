package org.other.game.banksystem;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class HelloPage extends JPanel {
	private BankSystem bs;
	private JLabel lable_biaoti = new JLabel("简易模拟ATM机系统");
	private JLabel lable_kahao = new JLabel("卡号");
	private JTextField text_kahao = new JTextField(20);
	private JButton button_login = new JButton("登录");
	
	public HelloPage(BankSystem bs) {
		//设置布局为null，通过坐标画图。
		setLayout(null);
        setSize(400, 300);
        this.bs = bs;
        init();
	}

	private void init() {
		lable_biaoti.setFont(new Font (Font.DIALOG, Font.BOLD, 25));
		lable_kahao.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
		lable_biaoti.setBounds(85, 20, 250, 30);
		lable_kahao.setBounds(100, 100, 50, 30);
		text_kahao.setBounds(150, 100,  100, 30);
		button_login.setBounds(260, 100, 60, 30);
		add(lable_biaoti);
		add(lable_kahao);
		add(text_kahao);
		add(button_login);
		
		bs.setKahao(text_kahao);
		button_login.setActionCommand("login");
		button_login.addActionListener(bs);
	}
}
