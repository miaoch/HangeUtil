package org.other.game.chinesechess.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import org.other.game.chinesechess.control.chat.ChatClient;
import org.other.game.chinesechess.util.DialogUtil;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatPanel extends JPanel {
	private ChatClient chatClient;
	private JTextArea chatArea;
	private JTextField chatEdit;
	private JButton chatSubmit;
	
	public ChatPanel() {
		setLayout(null);
		chatArea = new JTextArea();
		JScrollPane scroll = new JScrollPane(chatArea);
		chatEdit = new JTextField();
		chatSubmit = new JButton("�ύ");
		scroll.setBounds(0, 0, 300, 360);
		chatEdit.setBounds(0, 360, 220, 30);
		chatSubmit.setBounds(220, 360, 80, 30);
		add(scroll);
		add(chatEdit);
		add(chatSubmit);
		//���ü����¼�
		setListener();
	}
	
	/**
	 * ���ü����¼�
	 */
	private void setListener() {
		//����ύ������Ϣ
		chatSubmit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chatSubmit();
			}
		});
		
		//��Enter ������Ϣ
		chatEdit.addKeyListener(new KeyAdapter() {  
			@Override
            public void keyPressed(KeyEvent e)  {     
                if(e.getKeyCode()==KeyEvent.VK_ENTER) {
                	chatSubmit();
                }
            }  
        });
	}

	/**
	 * �ύ������Ϣ
	 */
	private void chatSubmit() {
		if (!chatEdit.getText().equals("")) {
			String message = ChessGame.player.getName() + ": " + chatEdit.getText();
			chatClient.sendMessage(message);
			chatEdit.setText("");
		} else {
			DialogUtil.createErrorDialog("������Ϣ����Ϊ�գ�");
		}
	}

	public void setChatClient(ChatClient myChat) {
		this.chatClient = myChat;
	}
	
	public JTextArea getChatArea() {
		return chatArea;
	}
}
