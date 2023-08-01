package com.softpuzzle.angkor.service.api.smscert;

import com.softpuzzle.angkor.database.mapper.api.cert.CertMapper;
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
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CertSmsService {

	@Autowired
	private CertMapper dao;
	@Autowired
	private LoginMapper loginDao;

	public ResCommDTO getAuthCode(HttpServletRequest request, HashMap params) {

		if (StringUtils.isBlank((String)params.get("phoneNumber"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		String url =  GlobalObjects.getUnionUrl() + "/phone/req";

		HttpHeaders headers = new HttpHeaders();
		headers.add("AppKey", GlobalObjects.getAppKey());
		// SMS 인증번호 요청 시 AuthKey는 params의 uuid hash key 256 (소문자)
		try {
			String uuidHash256 = Encrypt.encriptSHA256(params.get("uuid").toString()).toLowerCase();
			headers.add("AuthKey", uuidHash256);
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.INVALID_PARAMETER.getErrorcode() + "", CommonErrorCode.INVALID_PARAMETER.getGmessage() + " : " + CommonErrorCode.INVALID_PARAMETER.getDmessage());
		}
		headers.setContentType(MediaType.valueOf(request.getHeader("Content-Type")));
		headers.setAccept(Collections.singletonList(MediaType.valueOf(request.getHeader("accept"))));
		headers.setContentLanguage(Locale.forLanguageTag(request.getHeader("Language")));

		params.put("grantType", "session");
		HttpEntity<HashMap> req = new HttpEntity<>(params, headers);

		ResCommDTO response = RestTemplateUtils.postRequest(url, req);

		HashMap mapRes;

		if (response.getCode().equals("0")) {
			try {
				// ank_user_emailconfirm 에 insert
				mapRes = (HashMap) response.getData();
				mapRes.put("uuid", params.get("uuid"));

				params.put("comfirmWord", 0);
				params.put("confirmType", 2);

				int isData = dao.getEmailHist(params);
				if(isData>0){
					dao.updateEmailHist(params);
				}else{
					String guid = UUID.randomUUID().toString();
					params.put("guid", guid);
					dao.insertEmailHist(params);
				}
				return CommonUtil.setResponseObject(response.getData(), CommonErrorCode.SUCCESS.getErrorcode() + "", CommonErrorCode.SUCCESS.getGmessage() + " : " + CommonErrorCode.SUCCESS.getDmessage());
			} catch (Exception e) {
				return CommonUtil.setResponseObject(response.getData(), CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage() + " : " + CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
			}
		}else{
			return CommonUtil.setResponseObject(response.getData(), response.getCode() + "", response.getMessage());
		}
	}

	public ResCommDTO isAuthCode(HttpServletRequest request, HashMap params) throws Exception {

		if (StringUtils.isBlank((String)params.get("grantType")) || StringUtils.isBlank((String)params.get("phoneNumber"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		if (StringUtils.isBlank((String)params.get("certNumber")) || StringUtils.isBlank((String)params.get("uuid"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		if (!(params.get("grantType").equals("signin") || params.get("grantType").equals("signup") || params.get("grantType").equals("update"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.INVALID_PARAMETER.getErrorcode() + "", CommonErrorCode.INVALID_PARAMETER.getGmessage() + " : " + CommonErrorCode.INVALID_PARAMETER.getDmessage());
		}

		// 10번 이상 틀렸을 경우 10회 입력 오류 처리
		try {
			int tryCnt = dao.chkSmsTenOver(params);
			// 10번 이상 틀렸을 경우 10회 입력 오류 처리
			if(tryCnt > 9){
				return CommonUtil.setResponseObject(null, CommonErrorCode.MAIL_TRY_TEN_OVER.getErrorcode() + "", CommonErrorCode.MAIL_TRY_TEN_OVER.getGmessage() + " : " + CommonErrorCode.MAIL_TRY_TEN_OVER.getDmessage());
			}
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage() + " : " + CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}

		String url =  GlobalObjects.getUnionUrl() + "/phone/cert";

		HttpHeaders headers = new HttpHeaders();
		headers.add("AppKey", GlobalObjects.getAppKey());

		headers.setContentType(MediaType.valueOf(request.getHeader("Content-Type")));
		headers.setAccept(Collections.singletonList(MediaType.valueOf(request.getHeader("accept"))));
		headers.setContentLanguage(Locale.forLanguageTag(request.getHeader("Language")));

		HashMap reqParams = new HashMap();
		reqParams.put("phone", params.get("phoneNumber"));
		reqParams.put("certNum", params.get("certNumber"));

		if(params.get("grantType").equals("update")){
			reqParams.put("grantType", "signup");
			if(((String) params.get("authKey")).toLowerCase().contains("bearer")){
				headers.add("Authorization", (String) params.get("authKey"));
			}else{
				headers.add("Authorization","bearer " + (String) params.get("authKey"));
			}
		}else{
			reqParams.put("grantType", params.get("grantType"));
			headers.add("Authorization", request.getHeader("Authorization").toLowerCase());
		}

		reqParams.put("uuid", params.get("uuid"));

		HttpEntity<HashMap> req = new HttpEntity<>(reqParams, headers);

		ResCommDTO response = RestTemplateUtils.postRequest(url, req);
		params.put("confirmType", 2);
		if(!response.getCode().equals("0")){
			// 입력한 인증번호가 틀렸을 경우 ank_user_emailconfirm 에 try_cnt update
			if (response.getCode().equals("400603")) {
				params.put("angkorId", params.get("phoneNumber"));

				dao.updateCntEmailHist(params);

				HashMap mapRes = new HashMap();
				mapRes.put("tryCnt", params.get("tryCnt"));

				if(request.getHeader("Language").equals("en")){
					return CommonUtil.setResponseObject(mapRes, CommonErrorCode.API_400603_SMS.getErrorcode()+"",CommonErrorCode.API_400603_SMS.getGmessage());
				}else{
					return CommonUtil.setResponseObject(mapRes, CommonErrorKhmerCode.API_400603_SMS.getErrorcode() +"",CommonErrorKhmerCode.API_400603_SMS.getGmessage());
				}
			}else{
				return response;
			}
		}else{
			params.put("angkorId", params.get("phoneNumber"));
			dao.cleanCntEmailHist(params);

			if(params.get("grantType").equals("signin")) {
				HashMap map = dao.getUser(params);

				// 서버 uuid update
				HashMap uMap = new HashMap<>();
				uMap.put("angkorId", map.get("angkorId"));
				uMap.put("uuid", params.get("uuid"));
				uMap.put("publicKey", params.get("publicKey"));

				dao.updateUuid(uMap);

				// 로그인 기록 update
				dao.updateLoginDtAndPublicKey(uMap);

				Map<String, Map<String, Object>> settingData = new HashMap<>();
				HashMap<String, Object> chatting; chatting = loginDao.getChatting(map);
				HashMap<String, Object> display; display = loginDao.getDisplay(map);
				HashMap<String, Object> friend; friend = loginDao.getFriends(map);
				HashMap<String, Object> noti; noti = loginDao.getNoti(map);
				HashMap<String, Object> security; security = loginDao.getPrivacy(map);

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

				return CommonUtil.setResponseObject(map, CommonErrorCode.SUCCESS.getErrorcode() + "", CommonErrorCode.SUCCESS.getGmessage() + " : " + CommonErrorCode.SUCCESS.getDmessage());
			}else{
				return CommonUtil.setResponseObject(response.getData(), CommonErrorCode.SUCCESS.getErrorcode() + "", CommonErrorCode.SUCCESS.getGmessage() + " : " + CommonErrorCode.SUCCESS.getDmessage());
			}
		}
	}

	public ResCommDTO isAuthCodeToLogin(HttpServletRequest request, HashMap params) throws Exception {

		if (StringUtils.isBlank((String)params.get("grantType")) || StringUtils.isBlank((String)params.get("phoneNumber"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		if (StringUtils.isBlank((String)params.get("userId")) || StringUtils.isBlank((String)params.get("certNumber")) || StringUtils.isBlank((String)params.get("uuid"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		if (!(params.get("grantType").equals("signin") || params.get("grantType").equals("signup") || params.get("grantType").equals("update"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.INVALID_PARAMETER.getErrorcode() + "", CommonErrorCode.INVALID_PARAMETER.getGmessage() + " : " + CommonErrorCode.INVALID_PARAMETER.getDmessage());
		}

		// 10번 이상 틀렸을 경우 10회 입력 오류 처리
		try {
			int tryCnt = dao.chkSmsTenOver(params);
			// 10번 이상 틀렸을 경우 10회 입력 오류 처리
			if(tryCnt > 9){
				return CommonUtil.setResponseObject(null, CommonErrorCode.MAIL_TRY_TEN_OVER.getErrorcode() + "", CommonErrorCode.MAIL_TRY_TEN_OVER.getGmessage() + " : " + CommonErrorCode.MAIL_TRY_TEN_OVER.getDmessage());
			}
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage() + " : " + CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}

		String url =  GlobalObjects.getUnionUrl() + "/phone/certlogin/" + params.get("userId");

		HttpHeaders headers = new HttpHeaders();
		headers.add("AppKey", GlobalObjects.getAppKey());

		headers.setContentType(MediaType.valueOf(request.getHeader("Content-Type")));
		headers.setAccept(Collections.singletonList(MediaType.valueOf(request.getHeader("accept"))));
		headers.setContentLanguage(Locale.forLanguageTag(request.getHeader("Language")));

		HashMap reqParams = new HashMap();
		reqParams.put("phone", params.get("phoneNumber"));
		reqParams.put("certNum", params.get("certNumber"));

		if(params.get("grantType").equals("update")){
			reqParams.put("grantType", "signup");
			if(((String) params.get("authKey")).toLowerCase().contains("bearer")){
				headers.add("Authorization", (String) params.get("authKey"));
			}else{
				headers.add("Authorization","bearer " + (String) params.get("authKey"));
			}
		}else{
			reqParams.put("grantType", params.get("grantType"));
			headers.add("Authorization", request.getHeader("Authorization").toLowerCase());
		}

		reqParams.put("uuid", params.get("uuid"));

		HttpEntity<HashMap> req = new HttpEntity<>(reqParams, headers);

		ResCommDTO response = RestTemplateUtils.postRequest(url, req);
		params.put("confirmType", 2);
		if(!response.getCode().equals("0")){
			// 입력한 인증번호가 틀렸을 경우 ank_user_emailconfirm 에 try_cnt update
			if (response.getCode().equals("400603")) {
				params.put("angkorId", params.get("phoneNumber"));

				dao.updateCntEmailHist(params);

				HashMap mapRes = new HashMap();
				mapRes.put("tryCnt", params.get("tryCnt"));

				if(request.getHeader("Language").equals("en")){
					return CommonUtil.setResponseObject(mapRes, CommonErrorCode.API_400603_SMS.getErrorcode()+"",CommonErrorCode.API_400603_SMS.getGmessage());
				}else{
					return CommonUtil.setResponseObject(mapRes, CommonErrorKhmerCode.API_400603_SMS.getErrorcode() +"",CommonErrorKhmerCode.API_400603_SMS.getGmessage());
				}
			}else{
				return response;
			}
		}else{
			params.put("angkorId", params.get("phoneNumber"));
			dao.cleanCntEmailHist(params);

			if(params.get("grantType").equals("signin")) {

				HashMap map = dao.getUserById(params);

				// to-do: map 이 null 일 경우
				/*if(map==null){

				}*/

				if(!map.get("phonenumber").equals(params.get("phoneNumber"))){
					// 전화번호가 변경된 상태에서 sms 인증을 받는 경우 userId에 해당하는 사용자의 전화번호도 자동으로 변경 처리
					String oriPhone = ((String) params.get("phoneNumber")).replaceAll("^" + map.get("phonecode"), "");

					if (oriPhone.startsWith("10")) {
						oriPhone = "0" + oriPhone;
					}
					params.put("phoneCode", map.get("phonecode"));
					params.put("oriPhone", oriPhone);
					dao.updatePhoneByUserId(params);

					// update sendbird user metadata info
					params.put("angkorId", map.get("angkorId"));
					SendbirdUtils.updateUserMetaDataBySendbird(params);

					map.put("phonenumber", params.get("phoneNumber"));
				}

				// 서버 uuid update
				HashMap uMap = new HashMap<>();
				uMap.put("angkorId", map.get("angkorId"));
				uMap.put("uuid", params.get("uuid"));
				uMap.put("publicKey", params.get("publicKey"));

				dao.updateUuid(uMap);

				// 로그인 기록 update
				dao.updateLoginDtAndPublicKey(uMap);

				Map<String, Map<String, Object>> settingData = new HashMap<>();
				HashMap<String, Object> chatting; chatting = loginDao.getChatting(map);
				HashMap<String, Object> display; display = loginDao.getDisplay(map);
				HashMap<String, Object> friend; friend = loginDao.getFriends(map);
				HashMap<String, Object> noti; noti = loginDao.getNoti(map);
				HashMap<String, Object> security; security = loginDao.getPrivacy(map);

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

				return CommonUtil.setResponseObject(map, CommonErrorCode.SUCCESS.getErrorcode() + "", CommonErrorCode.SUCCESS.getGmessage() + " : " + CommonErrorCode.SUCCESS.getDmessage());
			}else{
				return CommonUtil.setResponseObject(response.getData(), CommonErrorCode.SUCCESS.getErrorcode() + "", CommonErrorCode.SUCCESS.getGmessage() + " : " + CommonErrorCode.SUCCESS.getDmessage());
			}
		}
	}
}
