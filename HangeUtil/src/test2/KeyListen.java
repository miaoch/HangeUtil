package test2;

import java.awt.Button;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class KeyListen extends JFrame {
	KeyListen() {
		super("¼üÅÌ¼àÌý");
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode()) {
					case 37 : System.out.println("left");break;
					case 38 : System.out.println("up");break;
					case 39 : System.out.println("right");break;
					case 40 : System.out.println("down");break;
				}
			}
		});
		add(new Button("yes"));
	}
	public static void main(String args[]) {
		KeyListen k = new KeyListen();
		k.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		k.setVisible(true);
		k.setSize(200, 100);
		k.requestFocus();
	}
}
