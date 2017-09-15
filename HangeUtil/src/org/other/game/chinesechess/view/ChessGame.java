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
 * 游戏窗口类
 * @author Administrator
 *
 */
public class ChessGame extends JFrame implements ActionListener {
	public static Player player;//玩家信息
	private Thread chatServerThread = null;
	private Thread gameServerThread = null;
	public GameClient gameClient;
	public GamePanel gamePanel;//游戏面板
	public InfoPanel infoPanel;//对战信息面板
	private ChatPanel chatPanel;//聊天面板
	private JMenuBar menubar;
	private JMenu menu;
	private JMenuItem createGame;
	private JMenuItem joinGame;
	public ChessGame() {
		//设置布局为null，通过坐标画图。
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
	 * 初始化布局
	 */
	private void initLayout(int team) {
		String name = DialogUtil.createInputDialog("请输入您的大名：");
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
	 * 初始化菜单
	 */
	private void initMenuBar() {
		menubar = new JMenuBar();
		menu = new JMenu("菜单");
		createGame = new JMenuItem("创建游戏");
		joinGame = new JMenuItem("加入游戏");
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
				DialogUtil.createMessageDialog("创建游戏成功");
				//创建游戏后自动连接
				try {
					initLayout(Config.TEAM_RED);//创建游戏默认为红方
					ChatClient chatClient = new ChatClient(Config.DEFAULT_IP_ADDRESS, chatPanel.getChatArea());
					chatPanel.setChatClient(chatClient);
					gameClient = new GameClient(Config.DEFAULT_IP_ADDRESS, this);
					gamePanel.setGameClient(gameClient);
					gamePanel.getListener().valid = true;//红方可以先走
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else {
				DialogUtil.createErrorDialog("已经创建游戏！");
			}
			DialogUtil.createMessageDialog("等待其他用户加入！");
		} else if (e.getSource() == joinGame) {
			String ip = DialogUtil.createInputDialog("请输入对方ip地址：");
			if (ip != null) {
				if (chatServerThread != null) {
					chatServerThread.stop();
					chatServerThread = null;
				}
				try {
					initLayout(Config.TEAM_BLACK);//加入游戏默认为黑方
					ChatClient myChat = new ChatClient(ip, chatPanel.getChatArea());
					chatPanel.setChatClient(myChat);
					gameClient = new GameClient(ip, this);
					gamePanel.setGameClient(gameClient);
					//发送加入游戏信息
					gameClient.sendCommand(new Command(Config.COMMAND_JOINGAME, player, Config.TEAM_BLACK));
					DialogUtil.createMessageDialog("加入成功！");
				} catch (IOException e1) {
					DialogUtil.createErrorDialog("连接失败！");
					e1.printStackTrace();
				}
			}
		}
	}
}
