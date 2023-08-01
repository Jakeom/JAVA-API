package com.softpuzzle.angkor.utility;

import com.softpuzzle.angkor.http.response.common.ResCommDTO;
import com.softpuzzle.angkor.http.response.common.ResSimpleDTO;
import com.softpuzzle.angkor.http.response.common.ResTrDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.Reader;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.sql.Clob;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("CommonUtil")
@Slf4j
public class CommonUtil
{
	/**
	 * CamelCase : SnakeCase to CamelCase.
	 * @param paramStr
	 * @return String
	 */
	public static String toCamelCase(String paramStr)
	{
		String parts[] = paramStr.split("_");
		String camelCaseString = "";
		for(String part : parts)
		{
			if(camelCaseString.equals(""))
			{
				camelCaseString = part.toLowerCase();
			}
			else
			{
				camelCaseString = camelCaseString + (part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase());
			}
		}

		return camelCaseString;
	}

	/**
	 * CamelCase : SnakeCase to CamelCase.
	 * @param paramMap
	 * @return HashMap
	 */
	public static HashMap<String, Object> toCamelCase(HashMap<String, Object> paramMap)
	{
		Iterator<String> iterator = paramMap.keySet().iterator();
		HashMap<String, Object> resultMap = new HashMap<>();
		while(iterator.hasNext())
		{
			String key = iterator.next();
			resultMap.put(toCamelCase(key), paramMap.get(key));
		}

		return resultMap;
	}

	/**
	 * CamelCase : SnakeCase to CamelCase.
	 * @param paramList
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public static List<Object> toCamelCase(List<Object> paramList)
	{
		List<Object> resultList = new ArrayList<>();

		for(int i = 0; i < paramList.size(); i++)
		{
			resultList.add(toCamelCase((HashMap<String, Object>)paramList.get(i)));
		}

		return resultList;
	}

	/**
	 * IP 암호화.
	 * ex) 192.168.0.1 to 192.xxx.0.1
	 * @param plainIp
	 * @return String
	 */
	public static String encryptIp(Object plainIp) 
	{
		String ip = null2Str(plainIp, "0.0.0.0");
		String temp[] = ip.split("\\.");

		StringBuffer buf = new StringBuffer();

		for(int i = 0; i < temp.length; i++)
		{
			if(i == 1)
			{
				buf.append("x.");
			}
			else if(i == 3)
			{
				buf.append(temp[i]);
			}
			else
			{
				buf.append(temp[i] + ".");
			}
		}

		return buf.toString();
	}

	/**
	 * BigDecimal 연산.
	 * @param left
	 * @param right
	 * @param operator
	 * @return String
	 */
	public static String calculateBigDecimal(String left, String right, String operator)
	{
		BigDecimal leftNum = new BigDecimal(left);
		BigDecimal rightNum = new BigDecimal(right);

		if(operator.equals("+"))
		{
			return leftNum.add(rightNum).toString();
		}
		else if(operator.equals("-"))
		{
			return leftNum.subtract(rightNum).toString();
		}
		else if(operator.equals("*"))
		{
			return leftNum.multiply(rightNum).toString();
		}
		else if(operator.equals("/"))
		{
			return leftNum.divide(rightNum, 2, BigDecimal.ROUND_FLOOR).toString();
		}
		else
		{
			return null;
		}
	}

	/**
	 * 문자열 변환 : CLOB to String. DTO 가 EgovMap.
	 * @param paramMap
	 * @param paramKey
	 * @return String
	 */
	public static String convertClobToString(Map paramMap, String paramKey) throws Exception
	{
		Clob clob = (Clob)paramMap.get(paramKey);
		Reader reader = clob.getCharacterStream();
		StringBuffer out = new StringBuffer();
		char buff[] = new char[1024];
		int nchars = 0;

		while((nchars = reader.read(buff)) > 0)
		{
			out.append(buff, 0, nchars);
		}

		return out.toString();
	}

	/**
	 * 문자열 변환 : 문자열 중 일치하는 문자 치환.
	 * @param str
	 * @param oldStr
	 * @param newStr
	 * @return String
	 */
	public static String replaceAll(String str, String oldStr, String newStr)
	{
		if(str == null) return "";

		Pattern p = Pattern.compile(oldStr);
		Matcher m = p.matcher(str);

		StringBuffer sb = new StringBuffer();
		boolean result = m.find();

		while(result)
		{
			m.appendReplacement(sb, newStr);
			result = m.find();
		}

		m.appendTail(sb);

		return sb.toString();
	}

	/**
	 * 문자열 변환 : null to strDefault.
	 * @param paramObject
	 * @param returnStr
	 * @return String
	 */
	public static String null2Str(Object paramObject, String returnStr)
	{
		if(paramObject == null || paramObject.toString().length() == 0)
		{
		    return returnStr;
		}
		else
		{
			return paramObject.toString();
		}
	}

	/**
	 * 문자열 변환 : 특수문자 및 공백 제거.
	 * @param paramStr
	 * @return String
	 */
	public static String repalceSpecialChar(String paramStr)
	{
		StringBuffer result = new StringBuffer();

		if(paramStr != null)
		{
			int l = paramStr.length();

			for(int i = 0; i < l; i++)
			{
				if(paramStr.charAt(i) == ' ' || paramStr.charAt(i) == '~' || paramStr.charAt(i) == '`' || paramStr.charAt(i) == '!' || paramStr.charAt(i) == '@' ||
						paramStr.charAt(i) == '#' || paramStr.charAt(i) == '$' || paramStr.charAt(i) == '%' || paramStr.charAt(i) == '^' || paramStr.charAt(i) == '&' ||
						paramStr.charAt(i) == '*' || paramStr.charAt(i) == '-' || paramStr.charAt(i) == '=' || paramStr.charAt(i) == '+' || paramStr.charAt(i) == '\\' ||
						paramStr.charAt(i) == '|' || paramStr.charAt(i) == '[' || paramStr.charAt(i) == '{' || paramStr.charAt(i) == '}' || paramStr.charAt(i) == ']' ||
						paramStr.charAt(i) == ';' || paramStr.charAt(i) == ':' || paramStr.charAt(i) == '\'' || paramStr.charAt(i) == ',' || paramStr.charAt(i) == '<' ||
						paramStr.charAt(i) == '.' || paramStr.charAt(i) == '>' || paramStr.charAt(i) == '/' || paramStr.charAt(i) == '?')
				{
					result.append("");
				}
				else
				{
					result.append(paramStr.charAt(i));
				}
			}
		}

		return result.toString();
	}
	
	/**
	 * 문자열이 비어있는지 검사.
	 * @param str 검사할 문자열
	 * @return 문자열이 비어있으면 true, 아니면 false
	 */
	public static boolean isEmpty(String str) {
		if(str == null || str == "null") {
			return true;
		} else {
			if(str.trim().length() <= 0) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public static boolean isAddrEmpty(String addr) {

		String[] split = addr.split("\\|");
		int length = 0;
		
		for(int i = 0; i < split.length; i++) {
			length += split[i].trim().length();
		}
		
		if(length > 0) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
     * 문자열 A에서 Z사이의 랜덤 문자열을 구하는 기능을 제공 시작문자열과 종료문자열 사이의 랜덤 문자열을 구하는 기능
     *
     * @param startChr
     *            - 첫 문자
     * @param endChr
     *            - 마지막문자
     * @return 랜덤문자
     * @exception MyException
     * @see
     */
    public static String getRandomStr(char startChr, char endChr) {

		int randomInt;
		String randomStr = null;
	
		// 시작문자 및 종료문자를 아스키숫자로 변환한다.
		int startInt = Integer.valueOf(startChr);
		int endInt = Integer.valueOf(endChr);
	
		// 시작문자열이 종료문자열보가 클경우
		if (startInt > endInt) {
		    throw new IllegalArgumentException("Start String: " + startChr + " End String: " + endChr);
		}
	
		try {
		    // 랜덤 객체 생성
		    SecureRandom rnd = new SecureRandom();
	
		    do {
			// 시작문자 및 종료문자 중에서 랜덤 숫자를 발생시킨다.
			randomInt = rnd.nextInt(endInt + 1);
		    } while (randomInt < startInt); // 입력받은 문자 'A'(65)보다 작으면 다시 랜덤 숫자 발생.
	
		    // 랜덤 숫자를 문자로 변환 후 스트링으로 다시 변환
		    randomStr = (char)randomInt + "";
		} catch (Exception e) {
		    //e.printStackTrace();
		    throw new RuntimeException(e);	// 2011.10.10 보안점검 후속조치
		}
	
		// 랜덤문자열를 리턴
		return randomStr;
    }
	

	 /**
	* 특정숫자 집합에서 랜덤 숫자를 구하는 기능 시작숫자와 종료숫자 사이에서 구한 랜덤
	* 숫자를 반환한다
	* @param startNum
	*        - 시작숫자
	* @param endNum
	*        - 종료숫자
	* @return 랜덤숫자
	* @exception MyException
	* @see
	*/
	public static int getRandomNum(int startNum, int endNum) {
	
	  int randomNum = 0;
	
	  try {
	
	      // 랜덤 객체 생성
	      SecureRandom rnd = new SecureRandom();
	
	      do {
	
	          // 종료숫자내에서 랜덤 숫자를 발생시킨다.
	          randomNum = rnd.nextInt(endNum + 1);
	
	      } while (randomNum < startNum); 
	
	  } catch (Exception e) {
	
	      //log.debug(e.getMessage());
	      //log.trace(e.getMessage());
	
	  }
	
	  return randomNum;
	
	}
	
	public static  String getParentPath() {
		String fileName = "";
		fileName = System.getProperty("user.dir");

		File f = new File(fileName);
		fileName = f.getParent().toString();

		return fileName;
	}
	
	public static String getClientIp(HttpServletRequest request) {

		String remoteAddr = "";

		if (request != null) {
			remoteAddr = request.getHeader("X-FORWARDED-FOR");
			if (remoteAddr == null || "".equals(remoteAddr)) {
				remoteAddr = request.getRemoteAddr();
			}
		}
		return remoteAddr;
	}
	
	public static String getMethodName() {
		String methodName = "";
		methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
		
		//log.info("클래스명 : "+Thread.currentThread().getStackTrace()[1].getClassName());	//호출된함수
		//log.info("클래스명 : "+Thread.currentThread().getStackTrace()[2].getClassName());	//호출된함수의 상위
		//log.info("메소드명 : "+Thread.currentThread().getStackTrace()[1].getMethodName());
		//log.info("줄번호 : "+Thread.currentThread().getStackTrace()[1].getLineNumber());
		//log.info("파일명 : "+Thread.currentThread().getStackTrace()[1].getFileName());
		return methodName;
	}
	

	public static ResCommDTO setResponseObject(List<HashMap> obj, String code, String message) {
		
		ResCommDTO res = ResCommDTO.builder()
			    .code(code)
			    .data(obj)
			    .message(message)
			    .resTime(DateUtil.getCurrDateWithformat())
			    .build();
		
		
		if (obj == null)
			res.setTotal(0);
		else
			res.setTotal(obj.size());

		return res;
	}

	public static ResCommDTO setResponseObject(Object obj, String code, String message) {

		
		ResCommDTO res = ResCommDTO.builder()
			    .code(code)
			    .data(obj)
			    .message(message)
			    .resTime(DateUtil.getCurrDateWithformat())
			    .build();

		return res;
	}

	public static ResSimpleDTO setResponseSimpleObject(Object obj, String code, String message) {


		ResSimpleDTO res = ResSimpleDTO.builder()
				.code(code)
				.data(obj)
				.message(message)
				.resTime(DateUtil.getCurrDateWithformat())
				.build();

		return res;
	}

	public static ResTrDTO setResponseTrObject(Object obj, int trcnt, String code, String message) {

		ResTrDTO res = ResTrDTO.builder()
				.data(obj)
			    .code(code)
			    .message(message)
			    .trdate(DateUtil.getCurrDateWithformat())
			    .trcnt(trcnt)
			    .build();

		return res;
	}


	public static ResCommDTO setResponseObject(List<HashMap> obj, String code, String message, int pageSize, int current, int cnt) {

		ResCommDTO res = new ResCommDTO();
		res.setCode(code);
		res.setData(obj);
		res.setMessage(message);
		res.setPageSize(pageSize);
		res.setCurrent(current);
		res.setResTime(DateUtil.getCurrDateWithformat());
		
		
		if (obj == null) {
			res.setTotal(0);
		} else {
			res.setTotal(cnt);
		}

		return res;
	}

	public static HashMap<String, Object> Object2Hashmap(Object obj) {
		HashMap<String, Object> ret = new HashMap<>();
		for (Field field : obj.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			try {
				ret.put(field.getName(), field.get(obj));
			} catch (IllegalArgumentException e) {
				ret.put(field.getName(), null);
			} catch (IllegalAccessException e) {
				ret.put(field.getName(), null);
			}
		}

		return ret;
	}

	public static String getRequestIpAddr(HttpServletRequest request) {
		String ip = "";
		String guid = "";
		try {
			ip = request.getHeader("X-Forwarded-For");

			if (request.getHeader("X-Real-IP") != null) {
				System.out.println("Client IP" + request.getHeader("X-Real-IP"));
				ip = request.getHeader("X-Real-IP");
			}

			if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			}
			if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
				ip = request.getHeader("HTTP_X_FORWARDED");
			}
			if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
				ip = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP");
			}
			if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
				ip = request.getHeader("HTTP_CLIENT_IP");
			}
			if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
				ip = request.getHeader("HTTP_FORWARDED_FOR");
			}
			if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
				ip = request.getHeader("HTTP_FORWARDED");
			}
			if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
				ip = request.getHeader("x-real-ip");
			}
			if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
				ip = request.getHeader("HTTP_VIA");
			}
			if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
				ip = request.getHeader("x-real-ip");
			}
			if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
				ip = request.getHeader("REMOTE_ADDR");
			}
			if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
				ip = request.getRemoteAddr();
			}

			if (ip.contains(",")) {
				int cnt = 0;
				String[] ips = ip.split(",");
				cnt = ips.length;
				ip = ips[cnt - 2].trim();
			}
		} catch (Exception e) {
			log.error("getRequestIpAddr Exception ", e.getLocalizedMessage());
		}

		if (ip.contains("0:0:0:0:0:0:0")) {
			log.info("getRequestIpAddr is ipVersion ..... ");
			// 임시로
			ip = "127.0.0.1";
		}
		return ip;
	}

	public static HashMap phoneCode(String number) {

		String countryCode = "";
		String phoneNumber = "";
		String fullPhoneNumber = number;
		HashMap map = new HashMap<>();

		if (fullPhoneNumber.length() == 10) {
			// 국가번호 코드가 없는 경우
			countryCode = "";
			phoneNumber = fullPhoneNumber;
		} else if (fullPhoneNumber.length() == 11) {
			// 국가번호 코드의 길이가 1자리인 경우
			countryCode = fullPhoneNumber.substring(0, 1);
			phoneNumber = fullPhoneNumber.substring(1);
		} else if (fullPhoneNumber.length() == 12) {
			// 국가번호 코드의 길이가 2자리인 경우
			countryCode = fullPhoneNumber.substring(0, 2);
			phoneNumber = fullPhoneNumber.substring(2);
		} else if (fullPhoneNumber.length() == 13) {
			// 국가번호 코드의 길이가 3자리인 경우
			countryCode = fullPhoneNumber.substring(0, 3);
			phoneNumber = fullPhoneNumber.substring(3);
		} else {
			// 예외 처리: 국가번호 코드의 길이가 올바르지 않은 경우
			return null;
		}
		map.put("countryCode", countryCode);
		map.put("phoneNumber", phoneNumber);
		return map;
	}
}