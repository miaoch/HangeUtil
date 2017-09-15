package org.other.game.banksystem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainWindow extends JFrame implements ActionListener {
	private JPanel currentPage;//当前页面
	private BankSystem bs;//后台管理系统 充当监视器
	private JMenuBar menubar = new JMenuBar();
	private JMenu menu_caozuo = new JMenu("操作");
	private JMenu menu_shuoming = new JMenu("说明");
	private JMenuItem homePage = new JMenuItem("首页");
	private JMenuItem author_info = new JMenuItem("作者说明");
	
	public MainWindow() {
		//设置布局为null，通过坐标画图。
		setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        init();
        setTitle("大脸cat大脸制造的一个小程序");
		setVisible(true);
    }

	private void init() {
		bs = new BankSystem(this);//新建一个后台管理监视器
		setPage(new HelloPage(bs)); //初始化登录页面
		menu_caozuo.add(homePage);
		menubar.add(menu_caozuo);
		menu_shuoming.add(author_info);
		menubar.add(menu_shuoming);
		setJMenuBar(menubar);
		
		homePage.addActionListener(this);
		author_info.addActionListener(this);
	}

	public void setPage(JPanel page) {
		setSize(400, 300);
		if (currentPage != null) {
			remove(currentPage);
		}
		currentPage = page;
		add(currentPage);
		repaint();//刷新
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == homePage) {
			setPage(new HelloPage(bs));
		} else if (e.getSource() == author_info) {
			JOptionPane.showMessageDialog(this, "大脸cat制造", "大脸cat制造", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
