package com.softpuzzle.angkor.utility;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class NmpUtils {
	public static final String OS_NAME			= System.getProperty("os.name");
	public static final String FILE_SEPARATOR	= System.getProperty("file.separator");
	public static final String USER_HOME		= System.getProperty("user.home");
	public static final String UPLOAD_PATH		= USER_HOME + FILE_SEPARATOR + "uploads";

	private static int getItem() {
		LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMdd-HHmmss-SSS");
		String formatted = formatter.format(currentDateTime);
		String items[] = formatted.split("-");
		int temp = 0;
		for ( int i = 0; i < items.length; i++ ) {
			if ( i < items.length-1 ) {
				temp += Integer.parseInt(items[i]);
			} else {
				temp = Integer.parseInt(String.valueOf(temp) + (items[items.length - 1].substring(-0,1)));
			}
		}
		return temp;
	}
	
	public static String getNumberCode() {
		return String.valueOf(getItem());
	}

	public static String getMixedCode() {
		return Integer.toHexString(getItem());
	}
	
	public static String timestampToString(Timestamp ts) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ts);
	}
	
	public static Timestamp stringTotimestamp(String dateString) throws ParseException {
		return new Timestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString).getTime());
	}
	
	public static Map<String, Long> getCountDownToMap(Timestamp ts) {
		Map<String, Long> resultMap = new HashMap<String, Long>();
		
		Timestamp nowDate	= new Timestamp(System.currentTimeMillis()); 
		Timestamp endDate	= ts;
		long diff			= 0;
		long isDone			= 1;
		
		if (nowDate.before(endDate)) {
			isDone = 0;
			diff = endDate.getTime() - nowDate.getTime();
		} else {
			diff = nowDate.getTime() - endDate.getTime();
		}
		
		diff				= diff / 1000;
		long sub			= diff;
		long hours			= sub / (60 * 60);
		sub					= sub % (60 * 60);
		long minutes		= sub / (60);
		long seconds		= sub % (60);
		
		resultMap.put("endTime"	, endDate.getTime() / 1000);
		resultMap.put("diff"	, diff);
		resultMap.put("hours"	, hours);
		resultMap.put("minutes"	, minutes);
		resultMap.put("seconds"	, seconds);
		resultMap.put("isDone"	, isDone);
		
		return resultMap;
	}
	
	public static String getUniqueName() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	public static String getTodayString() {
		return new SimpleDateFormat("yyyyMMdd").format(new Date());
	}

	/**
	 * Multipart 의 ContentType 값에서 / 이후 확장자만 잘라냅니다.
	 * @param contentType ex) image/png
	 * @return ex) png
	 */
	public static String getFormat(String contentType) {
		if (StringUtils.hasText(contentType)) {
			return contentType.substring(contentType.lastIndexOf('/') + 1);
		}
		return null;
	}

	/**
	 * 파일의 전체 경로를 생성합니다.
	 * @param fileId 생성된 파일 고유 ID
	 * @param format 확장자
	 */
	public static String createPath(String fileId, String format, String dir) {
		return String.format("%s/%s.%s", dir, fileId, format);
	}
}
