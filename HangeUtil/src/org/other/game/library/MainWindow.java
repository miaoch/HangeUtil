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
	private JPanel currentPage;//��ǰҳ��
	private String role = "2";//2:δ��¼ 0:�û� 1:����Ա չʾ�����˵���ͬ
	private int userId = -1;//�û�id
	private JMenuBar menubar = new JMenuBar();
	private JMenu menu_caozuo = new JMenu("����");
	private JMenu menu_shuoming = new JMenu("˵��");
	private JMenuItem homePage = new JMenuItem("��ҳ");//��չʾ
	private JMenuItem bookManagePage = new JMenuItem("�鼮����");//����Աչʾ
	private JMenuItem userManagePage = new JMenuItem("�û�����");//����Աչʾ
	private JMenuItem managePage = new JMenuItem("�軹����");//�û�չʾ
	private JMenuItem author_info = new JMenuItem("����˵��");
	
	public MainWindow() {
		//���ò���Ϊnull��ͨ�����껭ͼ��
		setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        init();
        setTitle("����ģ��ͼ��ݹ���ϵͳ");
		setVisible(true);
    }

	private void init() {
		setPage(new HelloPage(this)); //��ʼ����¼ҳ��
		//TODO
		//setRole("0");//������ �ǵ�ɾ
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
		repaint();//ˢ��
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == homePage) {//�˻���ҳ
			setRole("2");//�˻���ҳ��յ�¼��ɫ��Ϣ
			setUserId(-1);//�˻���ҳ��յ�¼��ɫ��Ϣ
			setPage(new HelloPage(this));
		} else if (e.getSource() == author_info) {//������Ϣ
			JOptionPane.showMessageDialog(this, "΢΢����ʿ", "ͼ�����ϵͳ", JOptionPane.INFORMATION_MESSAGE);
		} else if (e.getSource() == managePage) {//�û��鼮�������
			setPage(new HandlePage(this));
		} else if (e.getSource() == bookManagePage) {//����Ա�鼮�������
			setPage(new BookManagePage(this));
		} else if (e.getSource() == userManagePage) {//����Ա�û��������
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
