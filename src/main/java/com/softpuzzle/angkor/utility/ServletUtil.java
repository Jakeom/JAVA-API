package com.softpuzzle.angkor.utility;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServletUtil {

	private static String hostName = "";
	private static final String RESPONSE_CONTENTTYPE = "application/json";
	private static final String RESPONSE_CHARACTERENCODING = "utf-8";
	private static final String BIZ_NAME = "";
	
	private static Logger log = LoggerFactory.getLogger(ServletUtil.class);

	static {
		try {
			InetAddress netAddress = InetAddress.getLocalHost();
			hostName = netAddress.getHostName();
		} catch (UnknownHostException e) {
			log.error("netAddress.getHostName failed", e);
		}
	}

	public static String createSysErrorResponse(HttpServletResponse response) {
		final String code = "INTERNAL_SERVER_ERROR";
		String message = "서버 내부 오류 ";
		return createErrorResponse(500, 500, code, message, response);
	}

	public static String createParamErrorResponse(HttpServletResponse response) {
		final String code = "REQUIRE_ARGUMENT";
		String message = "매개변수에러";
		return createErrorResponse(400, 400, code, message, response);
	}

	public static String createParamErrorResponse(String param, HttpServletResponse response) {
		final String code = "REQUIRE_ARGUMENT";
		String message = "매개변수에러 :" + param;
		return createErrorResponse(400, 400, code, message, response);
	}

	public static String createAuthorizationErrorResponse(HttpServletResponse response) {
		final String code = "AUTH_INVALID_TOKEN";
		String message = "인증 요청이 실패했습니다!";
		return createErrorResponse(401, 401, code, message, response);
	}

	public static String createAuthorizeErrorResponse(HttpServletResponse response) {
		final String code = "AUTH_DENIED";
		String message = "요청 실패, 리소스 액세스 없음";
		return createErrorResponse(403, 403, code, message, response);
	}

	public static String createAuthorizeErrorResponse(HttpServletResponse response, String message) {
		final String code = "AUTH_DENIED";
		return createErrorResponse(403, 403, code, message, response);
	}

	public static String createNotFoundErrorResponse(HttpServletResponse response) {
		final String code = "NOT_FOUND";
		String message = "요청한 URL 경로가 없습니다";
		return createErrorResponse(404, 404, code, message, response);
	}

	

	public static String createErrorResponse(Integer httpStatus, Object code, String message, HttpServletResponse response) {
		code = BIZ_NAME + code;
		PrintWriter printWriter = null;
		String jsonString = "";
		try {
			response.setCharacterEncoding(RESPONSE_CHARACTERENCODING);
			response.setContentType(RESPONSE_CONTENTTYPE);
			response.setStatus(httpStatus);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("code", code);
			map.put("message", message);
			// map.put("request_id", requestId==null?"":requestId);
			// map.put("host_id", hostName);
			map.put("server_time", DateUtil.formatISO8601DateString(new Date()));

			printWriter = response.getWriter();
			jsonString = JSON.toJSONString(map, SerializerFeature.WriteMapNullValue);
			printWriter.write(jsonString);
			printWriter.flush();
		} catch (Exception e) {
			log.error("createResponse failed", e);
		} finally {
			if (null != printWriter)
				printWriter.close();
		}

		return jsonString;
	}

	public static String createErrorResponse(Integer httpStatus, Integer res_code, Object code, String message, HttpServletResponse response) {
		code = BIZ_NAME + code;
		PrintWriter printWriter = null;
		String jsonString = "";
		try {
			response.setCharacterEncoding(RESPONSE_CHARACTERENCODING);
			response.setContentType(RESPONSE_CONTENTTYPE);
			response.setStatus(httpStatus);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("code", code);
			map.put("message", message);
			// map.put("request_id", requestId==null?"":requestId);
			// map.put("host_id", hostName);
			map.put("res_code", res_code);
			map.put("server_time", DateUtil.formatISO8601DateString(new Date()));

			printWriter = response.getWriter();
			jsonString = JSON.toJSONString(map, SerializerFeature.WriteMapNullValue);
			printWriter.write(jsonString);
			printWriter.flush();
		} catch (Exception e) {
			log.error("createResponse failed", e);
		} finally {
			if (null != printWriter)
				printWriter.close();
		}

		return jsonString;
	}

	public static String createSuccessResponse(Integer httpCode, Object result, HttpServletResponse response) {

		return createSuccessResponse(httpCode, result, SerializerFeature.WriteMapNullValue, null, response);

	}

	public static String createSuccessResponse(Integer httpCode, String message, Object result, HttpServletResponse response) {

		return createSuccessResponse(httpCode, message, result, SerializerFeature.WriteMapNullValue, null, response);

	}

	public static String createSuccessResponse(Integer httpCode, Object result, SerializeFilter filter, HttpServletResponse response) {

		return createSuccessResponse(httpCode, result, SerializerFeature.PrettyFormat, filter, response);

	}

	public static String createSuccessResponse(Integer httpCode, Object result, SerializerFeature serializerFeature, HttpServletResponse response) {

		return createSuccessResponse(httpCode, result, serializerFeature, null, response);

	}

	public static String createSuccessResponse(Integer httpCode, Object result, SerializerFeature serializerFeature, SerializeFilter filter, HttpServletResponse response) {
		PrintWriter printWriter = null;
		String jsonString = "";
		try {
			response.setCharacterEncoding(RESPONSE_CHARACTERENCODING);
			response.setContentType(RESPONSE_CONTENTTYPE);
			response.setStatus(httpCode);
			printWriter = response.getWriter();
			if (null != result) {
				if (null != filter) {
					jsonString = JSONObject.toJSONString(result, filter, serializerFeature);
				} else {
					jsonString = JSONObject.toJSONStringWithDateFormat(result, "yyyy-MM-dd HH:mm:ss", serializerFeature);
				}
				printWriter.write(jsonString);
			}
			printWriter.flush();

		} catch (Exception e) {
			log.error("createResponse failed", e);
		} finally {
			if (null != printWriter)
				printWriter.close();
		}
		return jsonString;
	}

	public static String createSuccessResponse(Integer httpCode, String message, Object result, SerializerFeature serializerFeature, SerializeFilter filter, HttpServletResponse response) {
		PrintWriter printWriter = null;
		String jsonString = "";
		try {

			response.setCharacterEncoding(RESPONSE_CHARACTERENCODING);
			response.setContentType(RESPONSE_CONTENTTYPE);
			response.setStatus(httpCode);
			printWriter = response.getWriter();
			SerializeConfig config = new SerializeConfig();
			config.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd"));
			Map<String, Object> map = new HashMap<String, Object>();
			if (null != result) {
				map.put("res_code", httpCode);
				map.put("message", message);
				map.put("data", result);
				if (null != filter) {
					jsonString = JSONObject.toJSONString(map, filter, serializerFeature);
				} else {
//					jsonString = JSONObject.toJSONString(map,config,serializerFeature);
					jsonString = JSONObject.toJSONStringWithDateFormat(map, "yyyy-MM-dd");

				}
				printWriter.write(jsonString);
			}
			printWriter.flush();

		} catch (Exception e) {
			log.error("createResponse failed", e);
		} finally {
			if (null != printWriter)
				printWriter.close();
		}
		return jsonString;
	}

	public static String getRemortIP(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}

		if (ip.startsWith(",")) {
			ip = ip.substring(1, ip.length());
		}

		return ip;
	}

	public static String getUrlWithParams(HttpServletRequest request) {
		String url = request.getRequestURI();

		if (!StringUtil.isNull(request.getQueryString())) {
			url = url + "?" + request.getQueryString();
		}

		return url;
	}

	public static String getAccessToken(HttpServletRequest request) {
		String accessToken = null;

		String authorization = request.getHeader("Authorization");
		if (StringUtil.isNull(authorization)) {
			return accessToken;
		}

		if (authorization.startsWith("MAC")) {
			Pattern p = Pattern.compile("MAC id=\"(.*)\",nonce=\"(.*)\",mac=\"(.*)\"");
			Matcher m = p.matcher(authorization);
			if (m.find() && !StringUtil.isNull(m.group(1))) {
				return m.group(1);
			}
		} else if (authorization.startsWith("Bearer")) {
			Pattern p = Pattern.compile("Bearer \"(.*)\"");
			Matcher m = p.matcher(authorization);
			if (m.find() && !StringUtil.isNull(m.group(1))) {
				return m.group(1);
			}
		}

		return accessToken;
	}

	public static boolean isExistMacToken(HttpServletRequest request) {
		String authorization = request.getHeader("Authorization");
		if (!StringUtil.isNull(authorization) && authorization.startsWith("MAC id=")) {
			return true;
		}

		return false;
	}

	public static void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName) {
		try {
			// 한글파일지원
			String encodedfileName = java.net.URLEncoder.encode(fileName, "UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName + "\"");
			response.setContentType("application/force-download");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
