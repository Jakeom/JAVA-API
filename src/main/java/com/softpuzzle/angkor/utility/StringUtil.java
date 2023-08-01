package com.softpuzzle.angkor.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class StringUtil {
	public static boolean isNull(String obj) {
		if (obj == null){
			return true;
		}else if (obj.toString().trim().equals("")){
			return true;
		}else if(obj.toString().trim().toLowerCase().equals("null")){
			return true;
		}
		
		return false;
	}
/*	public static String setLanguage(String language){
		if(StringUtils.isBlank(language)){
			language = "en";
		}

		if (!(language.toLowerCase().equals("en") || language.toLowerCase().equals("khr"))) {
			language = "en";
		}

		return language;
	}*/

	public static boolean isNumber(String str) {
		Pattern pattern = Pattern.compile("[+-]?[0-9]+[0-9]*(\\.[0-9]+)?");
		Matcher match = pattern.matcher(str);
		
		return match.matches();
	}
    public static byte[] longToBytes(long l) {  
        byte[] b = new byte[8];  
        b[0] = (byte) (l >>> 56);  
        b[1] = (byte) (l >>> 48);  
        b[2] = (byte) (l >>> 40);  
        b[3] = (byte) (l >>> 32);  
        b[4] = (byte) (l >>> 24);  
        b[5] = (byte) (l >>> 16);  
        b[6] = (byte) (l >>> 8);  
        b[7] = (byte) (l);  
        return b;  
    }
    
    
    public static String getEncode(String str) {
    	
    	/*
    	String sret="";
    	try {
			sret=Encrypt.encryptAES256(str);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	*/
    	return str;
    }
    
    // null To space
    public static String nTs(String str) {
    	return str == null ? "" : str;
    }
    
    public static String convertToPascalCase(String str){
    		str = str.replaceAll("profile_images", "profile-images");
	        str = Pattern.compile("_([a-z])")
	                .matcher(str)
	                .replaceAll(m -> m.group(1).toUpperCase());
	        str = str.replaceAll("profile-images", "profile_images");
        return str;
    }
    
    
}
