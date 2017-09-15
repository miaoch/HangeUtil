package org.other.game.library.util;

import java.awt.Component;
import javax.swing.JOptionPane;

public class DialogUtil {
	/**
	 * ��ʾ����
	 * @param message
	 */
	public static void showErrorDialog(Component c, String message) {
		JOptionPane.showMessageDialog(c, message, "����", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * ��ʾ����
	 * @param message
	 */
	public static void showMessageDialog(Component c, String message) {
		JOptionPane.showMessageDialog(c, message, "��ʾ", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * ���봰��
	 * @param title
	 * @param tips
	 * @return
	 */
	public static String showInputDialog(Component c, String title, String tips) {
		return JOptionPane.showInputDialog(c, tips, title, JOptionPane.QUESTION_MESSAGE);
	}
}
