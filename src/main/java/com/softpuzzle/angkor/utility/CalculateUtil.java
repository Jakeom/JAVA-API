package com.softpuzzle.angkor.utility;

import org.apache.commons.lang3.RandomStringUtils;

import java.time.Instant;
import java.util.Random;

public class CalculateUtil {
	
    private static final String RANDOM_STR="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";  

	public static int getNext(int min, int max) {
		Random random = new Random();
		int s = random.nextInt(max) % (max - min + 1) + min;
		return s;
	}
	
	public static int getNext(int max) {
		Random random = new Random();
		int s = random.nextInt(max) ;
		return s;
	}

	public static String generateAngkorId(){

		// angkorId 생성규칙 - ak{4자리 random string}{unixtime 10자리}
		String ramdomString = RandomStringUtils.randomAlphabetic(4).toLowerCase();
		long unixTime = Instant.now().getEpochSecond();
		String id = "ak"+ ramdomString + unixTime;

		return id;
	}

	public static String generateDigitRandomCode(int sum){
		Random rd = new Random();
		String n = "";
		int getNum;
		do {
			getNum = Math.abs(rd.nextInt(Integer.MAX_VALUE)) % 10 + 48;
			char num1 = (char) getNum;
			String dn = Character.toString(num1);
			n += dn;
		} while (n.length() < sum);

		return n;
	}
	
	public static String generateMixRandomCode(int sum){  
        Random random = new Random();  
        StringBuffer sb = new StringBuffer();  
          
        for(int i = 0 ; i < sum; ++i){  
            int number = random.nextInt(62);//[0,62)  
              
            sb.append(RANDOM_STR.charAt(number));  
        }  
        return sb.toString();  
    }  
	
	
	public static long ipAddressToLong(String ipAddress) {
		long ipInt = 0;
		if (ValidatorUtil.isIPv4Address(ipAddress)) {
			String[] ipArr = ipAddress.split("\\.");
			if (ipArr.length == 3) {
				ipAddress = ipAddress + ".0";
			}
			ipArr = ipAddress.split("\\.");
			long p1 = Long.parseLong(ipArr[0]) * 256 * 256 * 256;
			long p2 = Long.parseLong(ipArr[1]) * 256 * 256;
			long p3 = Long.parseLong(ipArr[2]) * 256;
			long p4 = Long.parseLong(ipArr[3]);
			ipInt = p1 + p2 + p3 + p4;
		}
		return ipInt;
	}

}
