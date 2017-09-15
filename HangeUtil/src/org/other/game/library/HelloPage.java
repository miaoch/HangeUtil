package org.other.game.library;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.other.game.library.db.DBHelper;
import org.other.game.library.root.UserManagePage;
import org.other.game.library.user.HandlePage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.other.game.library.util.DialogUtil.*;//静态import 可以直接使用静态方法

/**
 * 登陆页面
 * @author Administrator
 *
 */
public class HelloPage extends JPanel implements ActionListener {
	private final MainWindow mainWindow;
	private JLabel lable_biaoti = new JLabel("简易模拟图书馆管理系统");
	private JLabel lable_name = new JLabel("账号");
	private JLabel lable_password = new JLabel("密码");
	private JTextField text_name = new JTextField(20);
	private JTextField text_password = new JTextField(20);
	private JButton button_login = new JButton("登录");
	
	public HelloPage(MainWindow mw) {
		mainWindow = mw;
		//设置布局为null，通过坐标画图。
		setLayout(null);
        setSize(450, 300);
        init();
	}

	private void init() {
		lable_biaoti.setFont(new Font (Font.DIALOG, Font.BOLD, 25));
		lable_name.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
		lable_password.setFont(new Font (Font.DIALOG, Font.BOLD, 15));
		lable_biaoti.setBounds(85, 20, 300, 30);
		lable_name.setBounds(125, 100, 50, 30);
		lable_password.setBounds(125, 150, 50, 30);
		text_name.setBounds(175, 100,  100, 30);
		text_password.setBounds(175, 150,  100, 30);
		button_login.setBounds(305, 150, 60, 30);
		add(lable_biaoti);
		add(lable_name);
		add(lable_password);
		add(text_name);
		add(text_password);
		add(button_login);
		
		button_login.addActionListener(this);
	}

	//登陆界面唯一一个action，登陆
	public void actionPerformed(ActionEvent e) {
		try {
			String name = text_name.getText().trim();
			String password = text_password.getText().trim();
			String sql = "select id, role from user where name=? and password=?";
			List<Object> querys = new ArrayList<Object>();
			querys.add(name);
			querys.add(password);
			List<Map<String, Object>> result = DBHelper.executeQuery(sql, querys);
			if (result.size() == 0) {
				showErrorDialog(this, "账号密码错误！");
			} else {//登录成功
				Map<String, Object> map = result.get(0);
				Integer id = (Integer) map.get("id");
				String role = (String) map.get("role");//0 学生 1 管理员
				mainWindow.setUserId(id);//记录登录人员的id
				mainWindow.setRole(role);//记录登录人员的角色
				if ("1".endsWith(role)) {
					mainWindow.setPage(new UserManagePage(mainWindow));
				} else {
					mainWindow.setPage(new HandlePage(mainWindow));
				}
			}
		} catch (Exception exp) {
			showErrorDialog(this, "账号密码格式错误！");
		}
	}
}
