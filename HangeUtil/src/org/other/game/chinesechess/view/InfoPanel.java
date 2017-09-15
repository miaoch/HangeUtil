package org.other.game.chinesechess.view;

import org.other.game.chinesechess.control.Timer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class InfoPanel extends JPanel {
	public Timer timer_red;
	public Timer timer_black;
	private JLabel red = new JLabel();
	private JLabel black = new JLabel();
	private JTextField red_time = new JTextField(5);
	private JTextField black_time = new JTextField(5);
	
	public InfoPanel() {
		setLayout(null);
		add(red);
		add(black);
		add(red_time);
		add(black_time);
		red.setBounds(1, 20, 100, 20);
		red_time.setBounds(110, 20, 50, 20);
		black.setBounds(1, 50, 100, 20);
		black_time.setBounds(110, 50, 50, 20);
		red_time.setEditable(false);
		black_time.setEditable(false);
		red_time.setText("0");
		black_time.setText("0");
		
		timer_red = new Timer(red_time);
		timer_black = new Timer(black_time);
	}
	
	public void setRed(String text) {
		red.setText(text);
	}
	
	public void setBlack(String text) {
		black.setText(text);
	}
}
