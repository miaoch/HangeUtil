import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;


@SuppressWarnings("serial")
public class PlaneGame extends JFrame {
	/**
	 * ����:fishing
	 * ����
	 */
	public static void main(String[] args) throws IOException{
		new PlaneGame();
	}

	//�ҵ���Ϸ����ԴĿ¼
	public static String path = System.getProperty("user.dir")+"\\File\\indi\\fishing\\PlaneGame\\photos";
	//�洢ͼƬ�ļ�
	public static HashMap<String,BufferedImage> maps = new HashMap<>();
	//������Ϸ��Դ
	static{
		//�õ���Ŀ¼�������е��ļ�
		File[] files = new File(path).listFiles();
		for (int i = 0; i < files.length; i++){
			//��������
			try {
				maps.put(files[i].getName(),ImageIO.read(files[i]));
			} catch (IOException e) {
				e.printStackTrace();
			}
			//System.out.println(files[i].getName());
		}
	}
	/*����һ�����췽��*/
	PlaneGameClassOne jp;
	public PlaneGame(){
		//���ñ���
		this.setTitle("fishing ��ɻ�v1");
		//���ô��ھ���
		//this.setLocationRelativeTo(null);
		//���x��������
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//���ô����С
		this.setSize(640, 700);
		//���ô��ڲ��ɸĶ���С
		this.setResizable(false);
		//����һ��С���
		PlaneGameClassOne jp = new PlaneGameClassOne();
		this.setContentPane(jp);
		this.addKeyListener(new KeyListener());
		//���ô��ڿɼ�
		this.setVisible(true);
	}
	/**���һ��������*/
	class KeyListener extends KeyAdapter{
		public void keyPressed(KeyEvent e) {
			jp.keyPressed(e);
		}
	}
}
/**
 * ����:fishing
 * ����
 */
@SuppressWarnings("serial")
class PlaneGameClassOne extends JPanel { //throws NullPointerException 
	/*���¼���*/
	public void keyPressed(KeyEvent e) throws NullPointerException{
		//����ֵ
		try {
			if(e.getKeyCode() == KeyEvent.VK_W){
				planePoint.y -= 10;
			}if(e.getKeyCode() == KeyEvent.VK_S){
				planePoint.y += 10; 
			}if(e.getKeyCode() == KeyEvent.VK_A){
				planePoint.x -= 10;
			}if(e.getKeyCode() == KeyEvent.VK_D){
				planePoint.x += 10;
			}
		}catch(NullPointerException ex) {
			ex.printStackTrace();
		}
		repaint();
	}

	/*��ʼ������*/
	//��������
	Point bgPoint = new Point(0, 0);
	//�ɻ�����
	Point planePoint = new Point(270, 550);
	//�����߳�
	public PlaneGameClassOne() {
		new Thread(new BgThread()).start();
	}
	@Override
	public void paint(Graphics g){
		//�ص�
		super.paint(g);
		//����һ����������
		BufferedImage image = new BufferedImage(640, 700, BufferedImage.TYPE_INT_RGB);
		//����һ������
		Graphics2D gs = image.createGraphics();
		//���Ʊ���
		gs.drawImage(PlaneGame.maps.get("city.jpg"), bgPoint.x, bgPoint.y, this);
		gs.drawImage(PlaneGame.maps.get("city.jpg"), bgPoint.x, bgPoint.y-715, this);
		//���Ʒɻ�
		gs.drawImage(PlaneGame.maps.get("plane.jpg"), planePoint.x, planePoint.y, this);
		//���»���
		g.drawImage(image, 0, 0, this);

	}
	class BgThread implements Runnable{
		@Override
		public void run() {
			while (true) {
				if(bgPoint.y == 700) {
					bgPoint.y = -15;
				}
				bgPoint.y += 1;
				try {
					Thread.sleep(10);
				} catch(InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
				repaint();
			}
		}
	}
}