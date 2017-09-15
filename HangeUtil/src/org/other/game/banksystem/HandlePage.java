package org.other.game.banksystem;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class HandlePage extends JPanel {
	private BankSystem bs;
	
	private JButton button_deposit = new JButton("存款");
	private JButton button_withdraw = new JButton("取款");
	private JButton button_query = new JButton("查询");
	private JButton button_exit = new JButton("退卡");
	private JTextArea area_card_info = new JTextArea(10, 20);
	private JScrollPane jsp_area_card= new JScrollPane(area_card_info);
	
	public HandlePage(BankSystem bs) {
		//设置布局为null，通过坐标画图。
		setLayout(null);
        setSize(400, 300);
        this.bs = bs;
        bs.setCardInfo(area_card_info);
        init();
	}

	private void init() {
		area_card_info.setEditable(false);
		button_deposit.setBounds(10, 40, 60, 30);
		button_withdraw.setBounds(10, 80, 60, 30);
		button_query.setBounds(10, 120, 60, 30);
		button_exit.setBounds(10, 160, 60, 30);
		jsp_area_card.setBounds(90, 20, 280, 200);
		add(button_deposit);
		add(button_withdraw);
		add(button_query);
		add(button_exit);
		add(jsp_area_card);
		
		button_deposit.setActionCommand("deposit");
		button_withdraw.setActionCommand("withdraw");
		button_query.setActionCommand("query");
		button_exit.setActionCommand("exit");
		button_deposit.addActionListener(bs);
		button_withdraw.addActionListener(bs);
		button_query.addActionListener(bs);
		button_exit.addActionListener(bs);
	}
}
