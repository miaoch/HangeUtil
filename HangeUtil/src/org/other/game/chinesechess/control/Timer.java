package org.other.game.chinesechess.control;

import javax.swing.JTextField;

public class Timer extends Thread {
	private JTextField increment;
	
	public Timer(JTextField increment) {
		this.increment = increment;
	}
	
	@Override
	public void run() {
		while (true) {
			long i = Long.parseLong(increment.getText()) + 1L;
			increment.setText(String.valueOf(i));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}

