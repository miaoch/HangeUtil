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
 * ʹ��html����pdf������
 * @author miaoch
 *
 */
public class PDFReport extends JFrame implements ActionListener {
	private JLabel info;
	private JFileChooser fileDialog = new JFileChooser();
	private JMenuBar menubar = new JMenuBar();
	private JMenu menu = new JMenu("�ļ�");
	private JMenuItem itemOpen = new JMenuItem("����PDF�ļ�");//���ļ�
	
	public PDFReport() {
		//���ò���Ϊnull��ͨ�����껭ͼ��
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
        //�����ļ����� ����html�ļ�
        fileDialog.setFileFilter(new FileFilter(){
			@Override
			public boolean accept(File f) {
				if (f.isDirectory())return true;
			    return f.getName().endsWith(".html");  //����Ϊѡ����.classΪ��׺���ļ�
			}
			@Override
			public String getDescription() {
				return ".html";
			}
        });
        
        info = new JLabel("title");    //title��ָ��ǩ������
        info.setFont(new Font("����", Font.BOLD, 18));
        info.setText("<html>"
        		+ "<body>"
        		+ "1. �뵼��html�ļ���<br/>"
        		+ "2. ������������ڿ�ͷʹ��css"
        		+ "����body{font-family: SimSun;}<br/>"
        		+ "3. ������������⣬����ϵQQ: 476864279"
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
			//���ļ�
			int state = fileDialog.showOpenDialog(this);
			while (state == JFileChooser.APPROVE_OPTION) {
				File dir = fileDialog.getCurrentDirectory();
				String oldfilepath = fileDialog.getSelectedFile().getAbsolutePath();
				String name = fileDialog.getSelectedFile().getName();
				if (!name.endsWith(".html")) {
					showMessageDialog("�뵼��html�ļ���" ,"������");
					state = fileDialog.showOpenDialog(this);
					continue;
				}
				String str = JOptionPane.showInputDialog(this, "�����������ļ���:\n", "PDF����", JOptionPane.QUESTION_MESSAGE);
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
					showMessageDialog("���ɳɹ���" ,"�ɹ�");
				} catch (DocumentException | IOException e1) {
					showMessageDialog("����ʧ�ܣ���鿴˵����" ,"������");
					e1.printStackTrace();
				}
				return;
			}
		}
	}
	
	/**
	 * ��ʾ����
	 * @param message
	 */
	private void showMessageDialog(String message, String title) {
		JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
	public static void main(String args[]) {
		new PDFReport();
	}
}
