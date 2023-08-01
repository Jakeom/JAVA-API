package com.softpuzzle.angkor.gcache;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.softpuzzle.angkor.gcache.UserData;

@Component
public  class GlobalObjects {
	private static final Logger log = LoggerFactory.getLogger(GlobalObjects.class);
		
	public static String comanme="";
	public static String url="";
	public static String app_key = "";
	public static String app_token = "";
	public static String union_url = "";
	public static String aApi_url = "";
	public static String link_android = "";
	public static String link_ios = "";
	public static String invite_url = "";
	public static String android_servicekey = "";
	public static String push_ios_p12_key = "";
	public static String push_ios_p12_path = "";
	public static boolean isPushDev = false; 
	
	public static boolean getIsPushDev() {
		return isPushDev;
	}

	@Value("${push.isdev}")
	public void setIsPushDev(boolean value) {
		isPushDev = value;
	}

	public static String getAndroid_servicekey() {
		return android_servicekey;
	}

	@Value("${push.android.servicekey}")
	public void setAndroid_servicekey(String value) {
		android_servicekey = value;
	}

	public static String getPush_ios_p12_key() {
		return push_ios_p12_key;
	}

	@Value("${push.ios.p12.key}")
	public void setPush_ios_p12_key(String value) {
		push_ios_p12_key = value;
	}
	
	public static String getPush_ios_p12_path() {
		return push_ios_p12_path;
	}

	@Value("${push.ios.p12.path}")
	public void setPush_ios_p12_path(String value) {
		push_ios_p12_path = value;
	}

	public static String getComname() {
		System.out.println(comanme);
		return comanme;
	}

	public static String getUrl() {
		System.out.println(url);
		return url;
	}

	@Value("${app.key}")
	public void setAppKey(String value) {
		app_key = value;
	}

	public static String getAppKey() {
		return app_key;
	}

	@Value("${app.token}")
	public void setAppToken(String value) {
		app_token = value;
	}

	public static String getAppToken() {
		return app_token;
	}

	@Value("${union.url}")
	public void setUnionUrl(String value) {
		union_url = value;
	}

	public static String getUnionUrl() {
		return union_url;
	}

	@Value("${aApi.url}")
	public void setAapiUrl(String value) {
		aApi_url = value;
	}

	public static String getAapiUrl() {
		return aApi_url;
	}
	@Value("${qr.link.android}")
	public void setQrLinkAndoid(String value) {
		link_android = value;
	}
	public static String getQrLinkAndoid() {
		return link_android;
	}

	@Value("${qr.link.ios}")
	public void setQrLinkIos(String value) {
		link_ios = value;
	}
	public static String getQrLinkIos() {
		return link_ios;
	}

	@Value("${invite.url}")
	public void setInviteurl(String value) {
		invite_url = value;
	}
	public static String getInviteurl() {
		return invite_url;
	}

	@Value("${company.name}")
    public void setCompantname(String value) {
		try {
			comanme=new String(value.getBytes("iso-8859-1"), "UTF-8") ;
		} catch (UnsupportedEncodingException e) {
			comanme="";
		}
    }

	@Value("${company.url}}")
    public void setUrl(String value) {
		try {
			url=new String(value.getBytes("iso-8859-1"), "UTF-8") ;
		} catch (UnsupportedEncodingException e) {
			url="";
		}
    }

    public static UserData getChartValue(String user) {
    	return null;
    }
	
	
	private static UserData  getHeaderData() {
		UserData obj = new UserData();
		return obj;
	}
}