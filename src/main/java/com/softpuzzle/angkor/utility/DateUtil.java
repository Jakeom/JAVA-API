package com.softpuzzle.angkor.utility;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	private static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static String formatDateToyyyymmddhhmm() {
		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		String currentDateTimeStr = currentDateTime.format(formatter);
		return  currentDateTimeStr;
	}

	public static String formatISO8601DateString(Date date) {
		String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
		return  DateFormatUtils.format(date, pattern);
	}
	
	public static String formatDateToyyyymmdd(String paramDate){
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

		LocalDate date = LocalDate.parse(paramDate, inputFormatter);
		String outputString = date.format(outputFormatter);

		return outputString;
	}

	public static Long getCurrentReverseTime(){
		long longTime = System.currentTimeMillis()*1000000 + CalculateUtil.getNext(999999);
		return Long.MAX_VALUE - longTime;
	}
	
	public static Long recoverReverseTime(Long reverseTime){
		long longTime = Long.MAX_VALUE - reverseTime;
		return longTime/1000000;
	}
	public static String formatNormalDateString(Date date){
		String pattern = "yyyy-MM-dd HH:mm:ss";
		return DateFormatUtils.format(date, pattern);
	}
	
	public static  String getCurrDateWithformat() {
		
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
				
		Date time = new Date();			
		String time1 = format1.format(time);
			
		return time1;
	}

public static  String getCurrHourNmin() {
		
		SimpleDateFormat format1 = new SimpleDateFormat ("HHmm");
				
		Date time = new Date();			
		String time1 = format1.format(time);
			
		return time1;
	}

	
	public static  String getCurrDateWithformatPreSec(int sec) {
		
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
		
		Calendar cal = Calendar.getInstance();
						
		Date time = new Date();
		
		cal.setTime(time);
		cal.add(Calendar.SECOND, sec);
		
		String time1 = format1.format(cal.getTime());
			
		return time1;
	}
	


	public static  String getCurrDateWithformatPre(int sec) {
		
		Calendar cal = Calendar.getInstance();
		
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
		
		cal.add(Calendar.SECOND, sec);
		
		Date time = cal.getTime();
		
		String time1 = format1.format(time);
			
		return time1;
	}


	public static  String getCurrDateWithformat2() {

			SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd");

			Date time = new Date();
			String time1 = format1.format(time);

			return time1;
		}


	public static  String getToDateWithformat(int pre) {
		Calendar cal = Calendar.getInstance();
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

		cal.add(cal.DATE, pre); // 7일(일주일)을 뺀다
		String dateStr = format.format(cal.getTime());
		
		return dateStr;
	}
	
	public static String getToNtpDateWithformat(int pre) {
		Calendar cal = Calendar.getInstance();
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss.SSS");

		cal.add(cal.DATE, pre); // 7일(일주일)을 뺀다
		String dateStr = format.format(cal.getTime());

		return dateStr;
	}


	public static String toString(Date date) {
		return toString(DEFAULT_DATETIME_FORMAT, date);
	}

	public static String toString(String pattern, Date date) {
		String dateStr = null;
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			dateStr = sdf.format(date);
		}
		return dateStr;
	}

	public static  String getToDateWithformat2(int pre) {
		Calendar cal = Calendar.getInstance();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		cal.add(cal.DATE, pre); // 7일(일주일)을 뺀다
		String dateStr = format.format(cal.getTime());

		return dateStr;
	}


	public String getAddMonth(int month) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();

		cal.add(Calendar.MONTH, month);
		String dateStr = sdf.format(cal.getTime());



		return dateStr;
	}

	public static String setStringToDate(String beforeDateFormat, String afterDateFormat, String currentDate) {
		SimpleDateFormat transFormat = new SimpleDateFormat(beforeDateFormat);
		String retStr = "";

		try {
			Date to = transFormat.parse(currentDate);
			transFormat = new SimpleDateFormat(afterDateFormat);
			retStr = transFormat.format(to);
		} catch (ParseException e) {
		}
		return retStr;
	}


}
