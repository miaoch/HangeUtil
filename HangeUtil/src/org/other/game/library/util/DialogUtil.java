package org.other.game.library.util;

import java.awt.Component;
import javax.swing.JOptionPane;

public class DialogUtil {
	/**
	 * 提示窗口
	 * @param message
	 */
	public static void showErrorDialog(Component c, String message) {
		JOptionPane.showMessageDialog(c, message, "错误", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * 提示窗口
	 * @param message
	 */
	public static void showMessageDialog(Component c, String message) {
		JOptionPane.showMessageDialog(c, message, "提示", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * 输入窗口
	 * @param title
	 * @param tips
	 * @return
	 */
	public static String showInputDialog(Component c, String title, String tips) {
		return JOptionPane.showInputDialog(c, tips, title, JOptionPane.QUESTION_MESSAGE);
	}
}
