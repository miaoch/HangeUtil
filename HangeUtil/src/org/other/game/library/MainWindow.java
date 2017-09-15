package org.other.game.library;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.other.game.library.root.BookManagePage;
import org.other.game.library.root.UserManagePage;
import org.other.game.library.user.HandlePage;

public class MainWindow extends JFrame implements ActionListener {
	private JPanel currentPage;//当前页面
	private String role = "2";//2:未登录 0:用户 1:管理员 展示操作菜单不同
	private int userId = -1;//用户id
	private JMenuBar menubar = new JMenuBar();
	private JMenu menu_caozuo = new JMenu("操作");
	private JMenu menu_shuoming = new JMenu("说明");
	private JMenuItem homePage = new JMenuItem("首页");//都展示
	private JMenuItem bookManagePage = new JMenuItem("书籍管理");//管理员展示
	private JMenuItem userManagePage = new JMenuItem("用户管理");//管理员展示
	private JMenuItem managePage = new JMenuItem("借还管理");//用户展示
	private JMenuItem author_info = new JMenuItem("作者说明");
	
	public MainWindow() {
		//设置布局为null，通过坐标画图。
		setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        init();
        setTitle("简易模拟图书馆管理系统");
		setVisible(true);
    }

	private void init() {
		setPage(new HelloPage(this)); //初始化登录页面
		//TODO
		//setRole("0");//测试用 记得删
		menu_caozuo.add(homePage);
		menubar.add(menu_caozuo);
		menu_shuoming.add(author_info);
		menubar.add(menu_shuoming);
		setJMenuBar(menubar);
		
		homePage.addActionListener(this);
		bookManagePage.addActionListener(this);
		userManagePage.addActionListener(this);
		managePage.addActionListener(this);
		author_info.addActionListener(this);
	}

	public void setPage(JPanel page) {
		setSize(450, 300);
		if (currentPage != null) {
			remove(currentPage);
		}
		currentPage = page;
		add(currentPage);
		repaint();//刷新
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == homePage) {//退回首页
			setRole("2");//退回首页清空登录角色信息
			setUserId(-1);//退回首页清空登录角色信息
			setPage(new HelloPage(this));
		} else if (e.getSource() == author_info) {//作者信息
			JOptionPane.showMessageDialog(this, "微微的骑士", "图书管理系统", JOptionPane.INFORMATION_MESSAGE);
		} else if (e.getSource() == managePage) {//用户书籍管理界面
			setPage(new HandlePage(this));
		} else if (e.getSource() == bookManagePage) {//管理员书籍管理界面
			setPage(new BookManagePage(this));
		} else if (e.getSource() == userManagePage) {//管理员用户管理界面
			setPage(new UserManagePage(this));
		}
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
		if ("1".equals(role)) {
			menu_caozuo.add(bookManagePage);
			menu_caozuo.add(userManagePage);
			menu_caozuo.remove(managePage);
		} else if ("0".equals(role)) {
			menu_caozuo.add(managePage);
			menu_caozuo.remove(bookManagePage);
			menu_caozuo.remove(userManagePage);
		} else {
			menu_caozuo.remove(bookManagePage);
			menu_caozuo.remove(userManagePage);
			menu_caozuo.remove(managePage);
		}
	}
	

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public static void main(String args[]) {
		new MainWindow();
	}
}
