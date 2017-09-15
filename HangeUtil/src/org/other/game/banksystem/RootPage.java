package org.other.game.banksystem;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class RootPage extends JPanel {
	private BankSystem bs;
	
	private JTextArea area_cards = new JTextArea(10, 20);
	private JScrollPane jsp_area_cards= new JScrollPane(area_cards);
	
	private JButton button_add = new JButton("添加");
	private JButton button_remove = new JButton("删除");
	private JButton button_show = new JButton("查看");
	public RootPage(BankSystem bs) {
		//设置布局为null，通过坐标画图。
		setLayout(null);
        setSize(400, 300);
        this.bs = bs;
        bs.setCards(area_cards);
        init();
	}

	private void init() {
		area_cards.setEditable(false);
		button_add.setBounds(100, 20, 60, 30);
		button_remove.setBounds(170, 20, 60, 30);
		button_show.setBounds(240, 20, 60, 30);
		jsp_area_cards.setBounds(20, 60, 360, 180);
		add(button_add);
		add(button_remove);
		add(button_show);
		add(jsp_area_cards);
		
		button_add.setActionCommand("add");
		button_remove.setActionCommand("remove");
		button_show.setActionCommand("show");
		button_add.addActionListener(bs);
		button_remove.addActionListener(bs);
		button_show.addActionListener(bs);
	}
}
