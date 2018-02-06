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
	 * 作者:fishing
	 * 窗口
	 */
	public static void main(String[] args) throws IOException{
		new PlaneGame();
	}

	//找到游戏的资源目录
	public static String path = System.getProperty("user.dir")+"\\File\\indi\\fishing\\PlaneGame\\photos";
	//存储图片文件
	public static HashMap<String,BufferedImage> maps = new HashMap<>();
	//加载游戏资源
	static{
		//拿到该目录下面所有的文件
		File[] files = new File(path).listFiles();
		for (int i = 0; i < files.length; i++){
			//保存起来
			try {
				maps.put(files[i].getName(),ImageIO.read(files[i]));
			} catch (IOException e) {
				e.printStackTrace();
			}
			//System.out.println(files[i].getName());
		}
	}
	/*创建一个构造方法*/
	PlaneGameClassOne jp;
	public PlaneGame(){
		//设置标题
		this.setTitle("fishing 打飞机v1");
		//设置窗口居中
		//this.setLocationRelativeTo(null);
		//点击x结束运行
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//设置窗体大小
		this.setSize(640, 700);
		//设置窗口不可改动大小
		this.setResizable(false);
		//创建一个小面板
		PlaneGameClassOne jp = new PlaneGameClassOne();
		this.setContentPane(jp);
		this.addKeyListener(new KeyListener());
		//设置窗口可见
		this.setVisible(true);
	}
	/**添加一个适配器*/
	class KeyListener extends KeyAdapter{
		public void keyPressed(KeyEvent e) {
			jp.keyPressed(e);
		}
	}
}
/**
 * 作者:fishing
 * 容器
 */
@SuppressWarnings("serial")
class PlaneGameClassOne extends JPanel { //throws NullPointerException 
	/*按下键盘*/
	public void keyPressed(KeyEvent e) throws NullPointerException{
		//键码值
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

	/*初始化坐标*/
	//背景坐标
	Point bgPoint = new Point(0, 0);
	//飞机坐标
	Point planePoint = new Point(270, 550);
	//启动线程
	public PlaneGameClassOne() {
		new Thread(new BgThread()).start();
	}
	@Override
	public void paint(Graphics g){
		//重叠
		super.paint(g);
		//创建一个绘制容器
		BufferedImage image = new BufferedImage(640, 700, BufferedImage.TYPE_INT_RGB);
		//创建一个画笔
		Graphics2D gs = image.createGraphics();
		//绘制背景
		gs.drawImage(PlaneGame.maps.get("city.jpg"), bgPoint.x, bgPoint.y, this);
		gs.drawImage(PlaneGame.maps.get("city.jpg"), bgPoint.x, bgPoint.y-715, this);
		//绘制飞机
		gs.drawImage(PlaneGame.maps.get("plane.jpg"), planePoint.x, planePoint.y, this);
		//更新画板
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