package org.calendar;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

public class HCalendar {
	public static String[] getMonthFinalTime(int monthCount) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String[] result = new String[monthCount];
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		for (int i = 0; i < monthCount; i++) {
			calendar.add(Calendar.MONTH, -1);
			int monthday = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
			calendar.set(Calendar.DAY_OF_MONTH, monthday);
			result[monthCount - 1 -i] = sdf.format(calendar.getTime());
		}
		return result;
	}
	public static String[] getMonthFinalTime() {
		return getMonthFinalTime(12);
	}
	public static void main(String args[]) {
		System.out.println(Arrays.toString(getMonthFinalTime(5)));
	}
}
