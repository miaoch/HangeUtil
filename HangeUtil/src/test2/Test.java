package test2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Test extends JFrame {
	@Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D)g; //ǿת��2D
        ImageIcon ii1 = new ImageIcon("1.png");
        //dimision.width�Ǵ���Ŀ�ȣ�dimision.height�Ǵ���ĸ߶�
        g2.drawImage(ii1.getImage(), 0, 0, 1024, 745, null);

    }
	public Test() {
		setUndecorated(true);
		setVisible(true);
		setBounds(200, 0, 1024, 745);
		setBackground(new Color(0, 0, 0, 0));
	}
	public static void main(String args[]) throws Exception {
		Test t = new Test();
		
	}
}
