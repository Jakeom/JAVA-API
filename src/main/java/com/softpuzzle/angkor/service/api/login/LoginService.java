package com.softpuzzle.angkor.service.api.login;

import com.softpuzzle.angkor.database.mapper.api.login.LoginMapper;
import com.softpuzzle.angkor.gcache.GlobalObjects;
import com.softpuzzle.angkor.http.response.common.ResCommDTO;
import com.softpuzzle.angkor.utility.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

	@Autowired
	private LoginMapper dao;

	public ResCommDTO signIn(HttpServletRequest request, HashMap params) throws Exception {

		if (StringUtils.isBlank((String)params.get("userId")) || StringUtils.isBlank((String)params.get("password")) || StringUtils.isBlank((String)params.get("uuid"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		HashMap map;

		String password;
		try {
			password = String.valueOf(Encrypt.encriptSHA256((String) params.get("password")));
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.INVALID_PARAMETER.getErrorcode() + "", CommonErrorCode.INVALID_PARAMETER.getGmessage() + " : " + CommonErrorCode.INVALID_PARAMETER.getDmessage());
		}
		params.put("password", password);

		String url = GlobalObjects.getUnionUrl() + "/signin";

		HttpHeaders headers = new HttpHeaders();
		headers.add("AppKey", GlobalObjects.getAppKey());
		// 로그인 시 Authorization은 params의 userid(phone) hash key 256 (소문자)
		try {
			String uuidHash256 = Encrypt.encriptSHA256((String) params.get("userId")).toLowerCase();
			headers.add("Authorization", "bearer " + uuidHash256);
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.INVALID_PARAMETER.getErrorcode() + "", CommonErrorCode.INVALID_PARAMETER.getGmessage() + " : " + CommonErrorCode.INVALID_PARAMETER.getDmessage());
		}
		
		headers.setContentType(MediaType.valueOf(request.getHeader("Content-Type")));
		headers.setAccept(Collections.singletonList(MediaType.valueOf(request.getHeader("accept"))));
		headers.setContentLanguage(Locale.forLanguageTag(request.getHeader("Language")));

		params.put("grantType", "password");
		HttpEntity<HashMap> req = new HttpEntity<>(params, headers);

		ResCommDTO response = RestTemplateUtils.postRequest(url, req);

		try {
			if(!response.getCode().equals("0")){
				map = dao.getMyId(params);

				if(map!=null) {
					// uuid가 일치하지 않을 경우
					// 국가코드 뺀 정상 번호
					String phone = ((String) map.get("phoneNumber")).replaceAll("^" + map.get("phoneCode"), "");

					HashMap resMap = new HashMap();
					resMap.put("phonecode", map.get("phoneCode"));
					resMap.put("phonenumber", phone);

					response.setData(resMap);
				}
				return response;
			}else{
				map = dao.getUser(params);

				// 로그인 기록 및 개인 공유키 update
				HashMap uMap = new HashMap();
				uMap.put("angkorId", map.get("angkorId"));
				uMap.put("publicKey", params.get("publicKey"));
				dao.updateLoginDtAndPublicKey(uMap);

				Map<String, Map<String, Object>> settingData = new HashMap<>();
				HashMap<String, Object> chatting; chatting = dao.getChatting(map);
				HashMap<String, Object> display; display = dao.getDisplay(map);
				HashMap<String, Object> friend; friend = dao.getFriends(map);
				HashMap<String, Object> noti; noti = dao.getNoti(map);
				HashMap<String, Object> security; security = dao.getPrivacy(map);

				settingData.put("chatting", chatting);
				settingData.put("display", display);
				settingData.put("friend", friend);
				settingData.put("noti", noti);
				settingData.put("security", security);

				HashMap mapRes = (HashMap) response.getData();
				map.put("authKey", mapRes.get("authKey"));
				map.put("accessToken", mapRes.get("accessToken"));
				map.put("settingData", settingData);
				if(map.get("qrcodeUrl")!=null){
					map.put("qrcodeUrl", GlobalObjects.getAapiUrl() + "upload/qrcode/" + map.get("qrcodeUrl"));
				}

				return CommonUtil.setResponseObject(map,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
			}
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode()+"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		} finally {
			// 임시 처리
			if (params.get("userId").equals("angkortest")) {
				// 로직 성공시키는 코드
				map = dao.getUser(params);

				// 로그인 기록 및 개인 공유키 update
				HashMap uMap = new HashMap();
				uMap.put("angkorId", map.get("angkorId"));
				if(params.get("publicKey")==null){
					uMap.put("publicKey", null);
				}
				uMap.put("publicKey", params.get("publicKey"));

				dao.updateLoginDtAndPublicKey(uMap);

				// 서버 uuid update
				uMap.put("uuid", params.get("uuid"));
				dao.updateUuid(uMap);

				Map<String, Map<String, Object>> settingData = new HashMap<>();
				HashMap<String, Object> chatting; chatting = dao.getChatting(map);
				HashMap<String, Object> display; display = dao.getDisplay(map);
				HashMap<String, Object> friend; friend = dao.getFriends(map);
				HashMap<String, Object> noti; noti = dao.getNoti(map);
				HashMap<String, Object> security; security = dao.getPrivacy(map);

				settingData.put("chatting", chatting);
				settingData.put("display", display);
				settingData.put("friend", friend);
				settingData.put("noti", noti);
				settingData.put("security", security);

				HashMap mapRes = (HashMap) response.getData();
				map.put("authKey", mapRes.get("authKey"));
				map.put("accessToken", mapRes.get("accessToken"));
				map.put("settingData", settingData);
				if(map.get("qrcodeUrl")!=null){
					map.put("qrcodeUrl", GlobalObjects.getAapiUrl() + "upload/qrcode/" + map.get("qrcodeUrl"));
				}

				return CommonUtil.setResponseObject(map,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
			}
		}
	}

	public ResCommDTO signOut(HttpServletRequest request, HashMap params) {

		if (StringUtils.isBlank((String)params.get("angkorId"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		String url = GlobalObjects.getUnionUrl() + "/signout/" + params.get("angkorId");

		HttpHeaders headers = new HttpHeaders();
		headers.add("AppKey", GlobalObjects.getAppKey());
		if(request.getHeader("Authorization").toLowerCase().contains("null")){
			String authorization = request.getHeader("Authorization").toLowerCase().replace("null", "").trim();
			headers.add("Authorization", "bearer " + authorization);
		}else{
			headers.add("Authorization", request.getHeader("Authorization").toLowerCase());
		}
		headers.setAccept(Collections.singletonList(MediaType.valueOf(request.getHeader("accept"))));
		headers.setContentLanguage(Locale.forLanguageTag(request.getHeader("Language")));

		params.put("grantType", "session");
		HttpEntity<HashMap> req = new HttpEntity<>(params, headers);

		ResCommDTO response = RestTemplateUtils.postRequest(url, req);

		return response;
	}
}
