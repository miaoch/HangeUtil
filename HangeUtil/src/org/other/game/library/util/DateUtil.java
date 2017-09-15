package org.other.game.library.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
	public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final SimpleDateFormat DEFAULT_SDF = new SimpleDateFormat(DEFAULT_FORMAT);
	
	public static String getCurrentDate(String format) {
		return Long2String(System.currentTimeMillis(), format);
	}
	
	public static String getCurrentDate() {
		return getCurrentDate(DEFAULT_FORMAT);
	}
	
	public static String Long2String(long longtime) {
		return DEFAULT_SDF.format(new Date(longtime));
	}
	
	public static String Long2String(long longtime, String format) {
		return new SimpleDateFormat(format).format(new Date(longtime));
	}
	
	public static long String2Long(String date) throws ParseException {
		return String2Date(date).getTime();
	}
	
	public static long String2Long(String date, String format) throws ParseException {
		return String2Date(date, format).getTime();
	}
	
	public static Date String2Date(String date) throws ParseException {
		return DEFAULT_SDF.parse(date);
	}
	
	public static Date String2Date(String date, String format) throws ParseException {
		return new SimpleDateFormat(format).parse(date);
	}
	
	public static Calendar getZeroCalendar(String data, String format) throws ParseException {
		Date d = String2Date(data, format);
		Calendar calendar = Calendar.getInstance();
		calendar.set(1900 + d.getYear(), d.getMonth(), d.getDate(), 0, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}
	
	public static Calendar getZeroCalendar(String data) throws ParseException {
		return getZeroCalendar(data, DEFAULT_FORMAT);
	}
	
	public static Calendar getZeroCalendar(long datatime) throws ParseException {
		Date d = new Date(datatime);
		Calendar calendar = Calendar.getInstance();
		calendar.set(1900 + d.getYear(), d.getMonth(), d.getDate(), 0, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}
	
	public static Calendar getZeroCalendar() throws ParseException {
		return getZeroCalendar(getCurrentDate(DEFAULT_FORMAT));
	}
	
	public static Calendar getZeroCalendarFormat(String format) throws ParseException {
		return getZeroCalendar(getCurrentDate(format), format);
	}
}
