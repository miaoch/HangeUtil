package org.other.game.library.root;

import static org.other.game.library.util.DialogUtil.*;
import static org.other.game.library.util.DateUtil.*;

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

public class BookManagePage extends JPanel implements ActionListener {
	private MainWindow window;
	private JButton button_info = new JButton("�鼮��Ϣ");
	private JButton button_add = new JButton("����鼮");
	private JButton button_remove = new JButton("ɾ���鼮");
	private JButton button_log = new JButton("�軹��Ϣ");
	private JTextArea area_card_info = new JTextArea(10, 20);
	private JScrollPane jsp_area_card= new JScrollPane(area_card_info);
	
	public BookManagePage(MainWindow mw) {
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
		button_info.setBounds(10, 40, 90, 30);
		button_add.setBounds(10, 80, 90, 30);
		button_remove.setBounds(10, 120, 90, 30);
		button_log.setBounds(10, 160, 90, 30);
		jsp_area_card.setBounds(120, 20, 300, 200);
		add(button_info);
		add(button_add);
		add(button_remove);
		add(button_log);
		add(jsp_area_card);
		button_info.addActionListener(this);
		button_add.addActionListener(this);
		button_remove.addActionListener(this);
		button_log.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button_info) {
			setBookList();
		} else if (e.getSource() == button_add) {
			String msg = showInputDialog(this, "����鼮", "�������鼮���ͳ�ʼ�������ÿո����");
			if (msg != null) {
				try {
					String[] info = msg.trim().split("\\s");
					if (!addBook(info[0], info[1])) {
						showErrorDialog(this, "����鼮ʧ�ܣ�");
					}
					setBookList();
				} catch (Exception e1) {
					showErrorDialog(this, "������Ϣ����");
				}
			}
		} else if (e.getSource() == button_remove) {
			String msg = showInputDialog(this, "ɾ���鼮", "�������鼮ID");
			if (msg != null) {
				try {
					String ID = msg.trim();
					if (!deleteBook(ID)) {
						showErrorDialog(this, "ɾ���鼮ʧ�ܣ�");
					}
					setBookList();
				} catch (Exception e1) {
					showErrorDialog(this, "������Ϣ����");
				}
			}
		} else if (e.getSource() == button_log) {
			setLogList();
		}
	}
	
	private boolean deleteBook(String ID) {
		String sql = "delete from book where id=?";
		List<Object> infos = new ArrayList<Object>();
		infos.add(ID);
		return DBHelper.executeUpdate(sql, infos) != -1;
	}

	private boolean addBook(String bookname, String count) throws Exception {
		String sql = "insert into book(bookname, count) values(?, ?)";
		List<Object> infos = new ArrayList<Object>();
		infos.add(bookname);
		infos.add(count);
		return DBHelper.executeUpdate(sql, infos) != -1;
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
			try {
				String sql = "select ub.bookid,u.name,b.bookname,ub.state,ub.createtime,ub.returntime" +
								" from user_book ub" +
								" inner JOIN user u on u.id = ub.userid" +
								" inner JOIN book b on b.id = ub.bookid" +
								" ORDER BY ub.state";
				List<Map<String, Object>> result = DBHelper.executeQuery(sql, null);
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

