package org.other.game.chinesechess.util;

import javax.swing.JOptionPane;

public class DialogUtil {
	/**
	 * 提示窗口
	 * @param message
	 */
	public static void createErrorDialog(String message) {
		JOptionPane.showMessageDialog(null, message, "错误", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * 提示窗口
	 * @param message
	 */
	public static void createMessageDialog(String message) {
		JOptionPane.showMessageDialog(null, message, "提示", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * 输入窗口
	 * @param title
	 * @param tips
	 * @return
	 */
	public static String createInputDialog(String title, String tips) {
		return JOptionPane.showInputDialog(null, tips, title, JOptionPane.QUESTION_MESSAGE);
	}
	
	/**
	 * 输入窗口
	 * @param title
	 * @param tips
	 * @return
	 */
	public static String createInputDialog(String tips) {
		return JOptionPane.showInputDialog(null, tips, "请输入...", JOptionPane.QUESTION_MESSAGE);
	}
}
