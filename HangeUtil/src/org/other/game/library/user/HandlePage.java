package org.other.game.library.user;

import static org.other.game.library.util.DateUtil.Long2String;
import static org.other.game.library.util.DialogUtil.showErrorDialog;
import static org.other.game.library.util.DialogUtil.showInputDialog;

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

public class HandlePage extends JPanel implements ActionListener {
	private MainWindow window;
	private JButton button_info = new JButton("�鼮��Ϣ");
	private JButton button_binfo = new JButton("���ü�¼");
	private JButton button_borrow = new JButton("�����鼮");
	private JButton button_return = new JButton("�黹�鼮");
	private JButton button_brlog = new JButton("�軹��¼");
	private JTextArea area_card_info = new JTextArea(10, 20);
	private JScrollPane jsp_area_card= new JScrollPane(area_card_info);
	
	public HandlePage(MainWindow mw) {
		window = mw;
		//���ò���Ϊnull��ͨ�����껭ͼ��
		setLayout(null);
        setSize(450, 300);
        init();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	setBookList();//�������ʼ�����û��б�� �����Ч����֪��Ϊʲô
            }
        });
	}

	private void init() {
		area_card_info.setEditable(false);
		button_info.setBounds(10, 20, 90, 30);
		button_binfo.setBounds(10, 60, 90, 30);
		button_borrow.setBounds(10, 100, 90, 30);
		button_return.setBounds(10, 140, 90, 30);
		button_brlog.setBounds(10, 180, 90, 30);
		jsp_area_card.setBounds(120, 20, 300, 200);
		add(button_info);
		add(button_binfo);
		add(button_borrow);
		add(button_return);
		add(button_brlog);
		add(jsp_area_card);
		button_info.addActionListener(this);
		button_binfo.addActionListener(this);
		button_borrow.addActionListener(this);
		button_return.addActionListener(this);
		button_brlog.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button_info) {
			setBookList();
		} else if (e.getSource() == button_binfo) {
			setBorrowBookList();
		} else if (e.getSource() == button_borrow) {
			String msg = showInputDialog(this, "�����鼮", "�������鼮ID");
			if (msg != null) {
				try {
					String bookid = msg.trim();
					if (!canBorrow(bookid)) {
						showErrorDialog(this, "���Ѿ�������鼮�����߸��鼮�Ѿ���գ�");
					} else {
						borrowBook(bookid);
					}
					setBorrowBookList();
				} catch (Exception e1) {
					showErrorDialog(this, "������Ϣ����");
				}
			}
		} else if (e.getSource() == button_return) {
			String msg = showInputDialog(this, "�黹�鼮", "�������鼮ID");
			if (msg != null) {
				try {
					String bookid = msg.trim();
					returnBook(bookid);
					setBorrowBookList();
				} catch (Exception e1) {
					showErrorDialog(this, "������Ϣ����");
				}
			}
		} else if (e.getSource() == button_brlog) {
			setLogList();
		}
	}
	
	//�ж��û��Ƿ��ܽ����鼮 ,����ӽ����¼
	private boolean canBorrow(String bookid) {
		int userId = window.getUserId();
		try {
			String sql = "select 1" +
							" from user_book ub" +
							" inner JOIN user u on u.id = ub.userid" +
							" inner JOIN book b on b.id = ub.bookid" +
							" where ub.userid=? and ub.bookid=? and ub.state = '0'";
			List<Object> query = new ArrayList<Object>();
			query.add(userId);
			query.add(bookid);
			List<Map<String, Object>> result = DBHelper.executeQuery(sql, query);
			sql = "select 1" +
					" from book b" +
					" where b.id=? and b.count <= 0";
			query.remove(0);
			List<Map<String, Object>> result2 = DBHelper.executeQuery(sql, query);
			return result.isEmpty() && result2.isEmpty();
		} catch (Exception exp) {
			return false;
		}
	}
	
	private boolean borrowBook(String bookid) {
		int userId = window.getUserId();
		try {
			String sql = "insert into user_book(userid,bookid,state,createtime)" + 
					" values(?,?,'0',?)";
			List<Object> sqlValues = new ArrayList<Object>();
			sqlValues.add(userId);
			sqlValues.add(bookid);
			sqlValues.add(System.currentTimeMillis());
			DBHelper.executeUpdate(sql, sqlValues);
			sql = "update book set count = count - 1 where id = ?";
			sqlValues.clear();
			sqlValues.add(bookid);
			DBHelper.executeUpdate(sql, sqlValues);
			return true;
		} catch (Exception exp) {
			return false;
		}
	}
	
	//�黹�鼮
	private boolean returnBook(String bookid) {
		int userId = window.getUserId();
		try {
			String sql = "update user_book set state='1',returntime=? where userid=? and bookid=?";
			List<Object> sqlValues = new ArrayList<Object>();
			sqlValues.add(System.currentTimeMillis());
			sqlValues.add(userId);
			sqlValues.add(bookid);
			DBHelper.executeUpdate(sql, sqlValues);
			sql = "update book set count = count + 1 where id = ?";
			sqlValues.clear();
			sqlValues.add(bookid);
			DBHelper.executeUpdate(sql, sqlValues);
			return true;
		} catch (Exception exp) {
			return false;
		}
	}

	//�����б�
	private void setBorrowBookList() {
		int userId = window.getUserId();
		try {
			String sql = "select ub.bookid,u.name,b.bookname,ub.state,ub.createtime,ub.returntime" +
							" from user_book ub" +
							" inner JOIN user u on u.id = ub.userid" +
							" inner JOIN book b on b.id = ub.bookid" +
							" where ub.state = '0' and ub.userid = ?" +
							" ORDER BY ub.state";
			List<Object> query = new ArrayList<Object>();
			query.add(userId);
			List<Map<String, Object>> result = DBHelper.executeQuery(sql, query);
			StringBuilder sb = new StringBuilder();
			for (Map<String, Object> map : result) {
				int bookid = (Integer) map.get("bookid");
				String bookname = (String) map.get("bookname");
				Long createtime = (Long) map.get("createtime");
				sb.append(String.format("�鼮ID:%d\t�鼮��:%s\n����ʱ��:%s\n" +
						"----------------------------\n",
						bookid, bookname, Long2String(createtime)));
			}
			area_card_info.setText(sb.toString());
		} catch (Exception exp) {
			showErrorDialog(this, "��ȡ�����б����");
		}
	}

	private void setBookList() {
		try {
			String sql = "select id, bookname, count from book order by id";
			List<Map<String, Object>> result = DBHelper.executeQuery(sql, null);
			StringBuilder sb = new StringBuilder();
			for (Map<String, Object> map : result) {
				Integer id = (Integer) map.get("id");
				String bookname = (String) map.get("bookname");
				Integer count = (Integer) map.get("count");
				sb.append(String.format("ID:%d, ����:%s, ���:%s\n", id, bookname, count));
			}
			area_card_info.setText(sb.toString());
		} catch (Exception exp) {
			showErrorDialog(this, "��ȡ�鼮�б����");
		}
	}
	
	//��־��ʽ
	private void setLogList() {
		int userId = window.getUserId();
		try {
			String sql = "select ub.bookid,u.name,b.bookname,ub.state,ub.createtime,ub.returntime" +
							" from user_book ub" +
							" inner JOIN user u on u.id = ub.userid" +
							" inner JOIN book b on b.id = ub.bookid" +
							" where ub.userid = ?" +
							" ORDER BY ub.state";
			List<Object> query = new ArrayList<Object>();
			query.add(userId);
			List<Map<String, Object>> result = DBHelper.executeQuery(sql, query);
			StringBuilder sb = new StringBuilder();
			for (Map<String, Object> map : result) {
				int bookid = (Integer) map.get("bookid");
				String bookname = (String) map.get("bookname");
				String state = (String) map.get("state");
				Long createtime = (Long) map.get("createtime");
				Long returntime = (Long) map.get("returntime");
				sb.append(String.format("�鼮ID:%s\t�鼮:%s\t״̬:%s\n����ʱ��:%s\n�黹ʱ��:%s\n"+
										"----------------------------\n",
						bookid, bookname, "1".equals(state) ? "�ѹ黹":"������",
								createtime == 0 ? "" : Long2String(createtime), 
								returntime == 0 ? "" : Long2String(returntime)));
			}
			area_card_info.setText(sb.toString());
		} catch (Exception exp) {
			showErrorDialog(this, "��ȡ�軹�鼮�б����");
		}
	}
}
