package org.pdf;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;

/**
 * 使用html生成pdf工具类
 * @author miaoch
 *
 */
public class PDFReport extends JFrame implements ActionListener {
	private JLabel info;
	private JFileChooser fileDialog = new JFileChooser();
	private JMenuBar menubar = new JMenuBar();
	private JMenu menu = new JMenu("文件");
	private JMenuItem itemOpen = new JMenuItem("生成PDF文件");//打开文件
	
	public PDFReport() {
		//设置布局为null，通过坐标画图。
		setLayout(null);
        setSize(400, 300);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        init();
        setVisible(true);
	}
	
	private void init() {
		menu.add(itemOpen);
        menubar.add(menu);
        setJMenuBar(menubar);
        itemOpen.addActionListener(this);
        //设置文件过滤 过滤html文件
        fileDialog.setFileFilter(new FileFilter(){
			@Override
			public boolean accept(File f) {
				if (f.isDirectory())return true;
			    return f.getName().endsWith(".html");  //设置为选择以.class为后缀的文件
			}
			@Override
			public String getDescription() {
				return ".html";
			}
        });
        
        info = new JLabel("title");    //title是指标签的名称
        info.setFont(new Font("宋体", Font.BOLD, 18));
        info.setText("<html>"
        		+ "<body>"
        		+ "1. 请导入html文件！<br/>"
        		+ "2. 如包含中文请在开头使用css"
        		+ "设置body{font-family: SimSun;}<br/>"
        		+ "3. 如果有其他问题，请联系QQ: 476864279"
        		+ "</body>"
        		+ "</html>");
        info.setBounds(50, -100, 300, 400);
        add(info);
	}

	public void createPDF(String inputFile, String outputFile) throws DocumentException, IOException {
        String url = new File(inputFile).toURI().toURL().toString();  
        OutputStream os = new FileOutputStream(outputFile);  
        ITextRenderer renderer = new ITextRenderer();  
        renderer.setDocument(url);  
        ITextFontResolver fontResolver = renderer.getFontResolver(); 
        fontResolver.addFont("C:/Windows/fonts/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED); 
        renderer.layout();
        renderer.createPDF(os);  
        os.close();  
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == itemOpen) {
			//打开文件
			int state = fileDialog.showOpenDialog(this);
			while (state == JFileChooser.APPROVE_OPTION) {
				File dir = fileDialog.getCurrentDirectory();
				String oldfilepath = fileDialog.getSelectedFile().getAbsolutePath();
				String name = fileDialog.getSelectedFile().getName();
				if (!name.endsWith(".html")) {
					showMessageDialog("请导入html文件！" ,"出错了");
					state = fileDialog.showOpenDialog(this);
					continue;
				}
				String str = JOptionPane.showInputDialog(this, "请输入生成文件名:\n", "PDF生成", JOptionPane.QUESTION_MESSAGE);
				if (str == null) {
					state = fileDialog.showOpenDialog(this);
					continue;
				}
				str = str.toLowerCase();
				if (!str.endsWith(".pdf")) {
					str += ".pdf";
				}
				String newfilepath = dir.getAbsolutePath() + "/" + str;
				try {
					createPDF(oldfilepath, newfilepath);
					showMessageDialog("生成成功！" ,"成功");
				} catch (DocumentException | IOException e1) {
					showMessageDialog("生成失败，请查看说明！" ,"出错了");
					e1.printStackTrace();
				}
				return;
			}
		}
	}
	
	/**
	 * 提示窗口
	 * @param message
	 */
	private void showMessageDialog(String message, String title) {
		JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
	public static void main(String args[]) {
		new PDFReport();
	}
}
