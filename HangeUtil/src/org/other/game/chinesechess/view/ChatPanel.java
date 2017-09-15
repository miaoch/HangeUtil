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
		chatSubmit = new JButton("提交");
		scroll.setBounds(0, 0, 300, 360);
		chatEdit.setBounds(0, 360, 220, 30);
		chatSubmit.setBounds(220, 360, 80, 30);
		add(scroll);
		add(chatEdit);
		add(chatSubmit);
		//设置监听事件
		setListener();
	}
	
	/**
	 * 设置监听事件
	 */
	private void setListener() {
		//点击提交发送信息
		chatSubmit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chatSubmit();
			}
		});
		
		//按Enter 发送信息
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
	 * 提交输入信息
	 */
	private void chatSubmit() {
		if (!chatEdit.getText().equals("")) {
			String message = ChessGame.player.getName() + ": " + chatEdit.getText();
			chatClient.sendMessage(message);
			chatEdit.setText("");
		} else {
			DialogUtil.createErrorDialog("聊天信息不能为空！");
		}
	}

	public void setChatClient(ChatClient myChat) {
		this.chatClient = myChat;
	}
	
	public JTextArea getChatArea() {
		return chatArea;
	}
}
