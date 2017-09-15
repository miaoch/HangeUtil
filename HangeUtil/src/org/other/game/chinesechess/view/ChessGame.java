package org.other.game.chinesechess.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import org.other.game.chinesechess.control.chat.ChatClient;
import org.other.game.chinesechess.control.chat.ChatServer;
import org.other.game.chinesechess.control.game.GameClient;
import org.other.game.chinesechess.control.game.GameServer;
import org.other.game.chinesechess.model.Command;
import org.other.game.chinesechess.model.Player;
import org.other.game.chinesechess.util.DialogUtil;
import org.other.game.chinesechess.Config;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * ��Ϸ������
 * @author Administrator
 *
 */
public class ChessGame extends JFrame implements ActionListener {
	public static Player player;//�����Ϣ
	private Thread chatServerThread = null;
	private Thread gameServerThread = null;
	public GameClient gameClient;
	public GamePanel gamePanel;//��Ϸ���
	public InfoPanel infoPanel;//��ս��Ϣ���
	private ChatPanel chatPanel;//�������
	private JMenuBar menubar;
	private JMenu menu;
	private JMenuItem createGame;
	private JMenuItem joinGame;
	public ChessGame() {
		//���ò���Ϊnull��ͨ�����껭ͼ��
		setLayout(null);
        setSize(825, 630);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        initMenuBar();
        createGame.addActionListener(this);
        joinGame.addActionListener(this);
	}
	
	/**
	 * ��ʼ������
	 */
	private void initLayout(int team) {
		String name = DialogUtil.createInputDialog("���������Ĵ�����");
		player = new Player(name != null?name:Config.DEFAULT_PLAYER_NAME, team);
		chatPanel = new ChatPanel();
		chatPanel.setBounds(523, 190, 300, 400);
		add(chatPanel);
		gamePanel = new GamePanel(team);
		gamePanel.setBounds(1, 1, 522, 578);
		add(gamePanel);
		infoPanel = new InfoPanel();
		infoPanel.setBounds(523, 1, 300, 190);
		add(infoPanel);
	}

	/**
	 * ��ʼ���˵�
	 */
	private void initMenuBar() {
		menubar = new JMenuBar();
		menu = new JMenu("�˵�");
		createGame = new JMenuItem("������Ϸ");
		joinGame = new JMenuItem("������Ϸ");
		menu.add(createGame);
		menu.add(joinGame);
		menubar.add(menu);
		setJMenuBar(menubar);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == createGame) {
			if (chatServerThread == null && gameServerThread == null) {
				chatServerThread = new ChatServer();
				gameServerThread = new GameServer();
				chatServerThread.start();
				gameServerThread.start();
				DialogUtil.createMessageDialog("������Ϸ�ɹ�");
				//������Ϸ���Զ�����
				try {
					initLayout(Config.TEAM_RED);//������ϷĬ��Ϊ�췽
					ChatClient chatClient = new ChatClient(Config.DEFAULT_IP_ADDRESS, chatPanel.getChatArea());
					chatPanel.setChatClient(chatClient);
					gameClient = new GameClient(Config.DEFAULT_IP_ADDRESS, this);
					gamePanel.setGameClient(gameClient);
					gamePanel.getListener().valid = true;//�췽��������
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else {
				DialogUtil.createErrorDialog("�Ѿ�������Ϸ��");
			}
			DialogUtil.createMessageDialog("�ȴ������û����룡");
		} else if (e.getSource() == joinGame) {
			String ip = DialogUtil.createInputDialog("������Է�ip��ַ��");
			if (ip != null) {
				if (chatServerThread != null) {
					chatServerThread.stop();
					chatServerThread = null;
				}
				try {
					initLayout(Config.TEAM_BLACK);//������ϷĬ��Ϊ�ڷ�
					ChatClient myChat = new ChatClient(ip, chatPanel.getChatArea());
					chatPanel.setChatClient(myChat);
					gameClient = new GameClient(ip, this);
					gamePanel.setGameClient(gameClient);
					//���ͼ�����Ϸ��Ϣ
					gameClient.sendCommand(new Command(Config.COMMAND_JOINGAME, player, Config.TEAM_BLACK));
					DialogUtil.createMessageDialog("����ɹ���");
				} catch (IOException e1) {
					DialogUtil.createErrorDialog("����ʧ�ܣ�");
					e1.printStackTrace();
				}
			}
		}
	}
}
