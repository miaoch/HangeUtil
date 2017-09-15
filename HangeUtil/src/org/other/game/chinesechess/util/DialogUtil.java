package org.other.game.chinesechess.util;

import javax.swing.JOptionPane;

public class DialogUtil {
	/**
	 * ��ʾ����
	 * @param message
	 */
	public static void createErrorDialog(String message) {
		JOptionPane.showMessageDialog(null, message, "����", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * ��ʾ����
	 * @param message
	 */
	public static void createMessageDialog(String message) {
		JOptionPane.showMessageDialog(null, message, "��ʾ", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * ���봰��
	 * @param title
	 * @param tips
	 * @return
	 */
	public static String createInputDialog(String title, String tips) {
		return JOptionPane.showInputDialog(null, tips, title, JOptionPane.QUESTION_MESSAGE);
	}
	
	/**
	 * ���봰��
	 * @param title
	 * @param tips
	 * @return
	 */
	public static String createInputDialog(String tips) {
		return JOptionPane.showInputDialog(null, tips, "������...", JOptionPane.QUESTION_MESSAGE);
	}
}
