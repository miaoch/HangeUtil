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
	private JPanel currentPage;//��ǰҳ��
	private BankSystem bs;//��̨����ϵͳ �䵱������
	private JMenuBar menubar = new JMenuBar();
	private JMenu menu_caozuo = new JMenu("����");
	private JMenu menu_shuoming = new JMenu("˵��");
	private JMenuItem homePage = new JMenuItem("��ҳ");
	private JMenuItem author_info = new JMenuItem("����˵��");
	
	public MainWindow() {
		//���ò���Ϊnull��ͨ�����껭ͼ��
		setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        init();
        setTitle("����cat���������һ��С����");
		setVisible(true);
    }

	private void init() {
		bs = new BankSystem(this);//�½�һ����̨���������
		setPage(new HelloPage(bs)); //��ʼ����¼ҳ��
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
		repaint();//ˢ��
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == homePage) {
			setPage(new HelloPage(bs));
		} else if (e.getSource() == author_info) {
			JOptionPane.showMessageDialog(this, "����cat����", "����cat����", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
