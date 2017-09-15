package org.other.game.library.root;

import static org.other.game.library.util.DialogUtil.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.other.game.library.MainWindow;
import org.other.game.library.db.DBHelper;

public class UserManagePage extends JPanel implements ActionListener {
	private MainWindow window;
	private JTextArea area_cards = new JTextArea(10, 20);
	private JScrollPane jsp_area_cards = new JScrollPane(area_cards);
	private JButton button_add = new JButton("添加");
	private JButton button_remove = new JButton("删除");
	private JButton button_show = new JButton("查看");
	public UserManagePage(MainWindow mw) {
		window = mw;
		//设置布局为null，通过坐标画图。
		setLayout(null);
        setSize(450, 300);
        init();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	setUserList();//本来想初始加载用户列表的 结果无效，不知道为什么
            }
        });
	}

	private void init() {
		area_cards.setEditable(false);
		button_add.setBounds(125, 20, 60, 30);
		button_remove.setBounds(195, 20, 60, 30);
		button_show.setBounds(265, 20, 60, 30);
		jsp_area_cards.setBounds(45, 60, 380, 180);
		add(button_add);
		add(button_remove);
		add(button_show);
		add(jsp_area_cards);
		button_add.addActionListener(this);
		button_remove.addActionListener(this);
		button_show.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button_show) {
			setUserList();
		} else if (e.getSource() == button_add) {
			String msg = showInputDialog(this, "添加用户", "请输入用户名和密码，用空格隔开");
			if (msg != null) {
				try {
					String[] info = msg.trim().split("\\s");
					if (!addUser(info[0], info[1])) {
						showErrorDialog(this, "添加用户失败！");
					}
					setUserList();
				} catch (Exception e1) {
					showErrorDialog(this, "输入信息有误！");
				}
			}
		} else if (e.getSource() == button_remove) {
			String msg = showInputDialog(this, "删除用户", "请输入用户ID");
			if (msg != null) {
				try {
					String ID = msg.trim();
					if (!deleteUser(ID)) {
						showErrorDialog(this, "删除用户失败！");
					}
					setUserList();
				} catch (Exception e1) {
					showErrorDialog(this, "输入信息有误！");
				}
			}
		}
	}
	
	private boolean deleteUser(String ID) {
		String sql = "delete from user where id=?";
		List<Object> infos = new ArrayList<Object>();
		infos.add(ID);
		return DBHelper.executeUpdate(sql, infos) != -1;
	}

	private boolean addUser(String name, String password) throws Exception {
		String sql = "insert into user(name, password, role) values(?, ?, '0')";
		List<Object> infos = new ArrayList<Object>();
		infos.add(name);
		infos.add(password);
		return DBHelper.executeUpdate(sql, infos) != -1;
	}

	
	private void setUserList() {
		try {
			String sql = "select id, name, password from user where role='0' order by id";
			List<Map<String, Object>> result = DBHelper.executeQuery(sql, null);
			StringBuilder sb = new StringBuilder();
			for (Map<String, Object> map : result) {
				Integer id = (Integer) map.get("id");
				String name = (String) map.get("name");
				String password = (String) map.get("password");
				sb.append(String.format("ID:%d, 用户名:%s, 密码:%s\n", id, name, password));
			}
			area_cards.setText(sb.toString());
		} catch (Exception exp) {
			showErrorDialog(this, "获取用户列表错误！");
		}
	}
}
