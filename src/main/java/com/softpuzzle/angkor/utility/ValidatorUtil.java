package com.softpuzzle.angkor.utility;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtil {

	
	public static boolean isMobile(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		if (StringUtils.isBlank(str)) {
			return b;
		}
		p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches();
		return b;
	}


	public static boolean isNumeric(String str) {
		if (StringUtils.isBlank(str)) {
			return false;
		}
		
		if (str.matches("\\d*")) {
			return true;
		} else {
			return false;
		}
	}
	
	
	public static boolean isPasswd(String passwd){
		if(StringUtils.isBlank(passwd))return false;
		if(passwd.length()<6 || passwd.length()>12)return false;
		String regEx="^[A-Za-z0-9_]+$";
	    Pattern p=Pattern.compile(regEx);
	    Matcher m=p.matcher(passwd);
	    return m.matches();
	}
	
	public static boolean isIPv4Address(String input) {
		Pattern IPV4_PATTERN = Pattern
				.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");
		return IPV4_PATTERN.matcher(input).matches();
	}
	
	
	public static boolean isMac(String mac){
		if(StringUtils.isNotBlank(mac)){
			mac = mac.toUpperCase();
		    String patternMac="^[A-F0-9]{2}(:[A-F0-9]{2}){5}$";  
		    if(Pattern.compile(patternMac).matcher(mac).find()){  
		    	return true;
	        } 
		}
	    return false;
	}

	
	public static boolean isUsername(String username){
		if(StringUtils.isBlank(username))return false;
		String regEx="^[A-Za-z0-9_]+$";
	    Pattern p=Pattern.compile(regEx);
	    Matcher m=p.matcher(username);
	    return m.matches();
	}
	
	
	public static boolean isContainsChinese(String source){
		final String regEx = "[\u4e00-\u9fa5]";
		final Pattern pat = Pattern.compile(regEx);
		boolean flag = false;
		Matcher matcher = pat.matcher(source);
		if(matcher.find()) {
			flag = true;
		}
		return flag;
	}
	
	public static boolean isEmail(String email){
		final String regEx="^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		final Pattern pat = Pattern.compile(regEx);
		boolean flag = false;
		Matcher matcher = pat.matcher(email);
		flag = matcher.matches();
		
		return flag;
	}

	public static boolean validateUUID(String uuidStr) {
		try {
			UUID uuid = UUID.fromString(uuidStr);
			return uuid != null;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
}
