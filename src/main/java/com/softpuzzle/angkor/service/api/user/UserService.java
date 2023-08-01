package com.softpuzzle.angkor.service.api.user;

import com.softpuzzle.angkor.database.mapper.api.login.LoginMapper;
import com.softpuzzle.angkor.database.mapper.api.user.UserMapper;
import com.softpuzzle.angkor.gcache.GlobalObjects;
import com.softpuzzle.angkor.http.request.ORGReq020;
import com.softpuzzle.angkor.http.response.common.ResCommDTO;
import com.softpuzzle.angkor.http.response.common.ResTrDTO;
import com.softpuzzle.angkor.service.api.cert.CertService;
import com.softpuzzle.angkor.service.api.emailcert.CertEmailService;
import com.softpuzzle.angkor.service.api.smscert.CertSmsService;
import com.softpuzzle.angkor.utility.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
	@Autowired
	private CertSmsService certSmsService;
	@Autowired
	private CertEmailService certEmailService;
	@Autowired
	private UserMapper dao;
	@Autowired
	private LoginMapper loginDao;
	@Autowired
	private CertService certService;

	public ResCommDTO checkUserId(HttpServletRequest request, HashMap params) {

		if (StringUtils.isBlank((String)params.get("userId")) || StringUtils.isBlank((String)params.get("grantType"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		if (!(((String) params.get("grantType")).toLowerCase().equals("signin") || ((String) params.get("grantType")).toLowerCase().equals("signup"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.INVALID_PARAMETER.getErrorcode() + "", CommonErrorCode.INVALID_PARAMETER.getGmessage() + " : " + CommonErrorCode.INVALID_PARAMETER.getDmessage());
		}

		try {
			if(((String) params.get("grantType")).toLowerCase().equals("signup")) {

				String url = GlobalObjects.getUnionUrl() + "/user/findUserId/" + params.get("userId");

				HttpHeaders headers = new HttpHeaders();
				headers.add("AppKey", GlobalObjects.getAppKey());
				headers.setAccept(Collections.singletonList(MediaType.valueOf(request.getHeader("accept"))));
				headers.setContentLanguage(Locale.forLanguageTag(request.getHeader("Language")));
				HttpEntity<String> req = new HttpEntity<String>("", headers);

				ResCommDTO response = RestTemplateUtils.getRequest(url, req);

				if(!response.getCode().equals("0")){
					return CommonUtil.setResponseObject(response.getData(), response.getCode() + "", response.getMessage());
				}else{
					int result = dao.checkIsUserId(params);
					if (result > 0) {
						return CommonUtil.setResponseObject(null, CommonErrorCode.IS_USED_USERID.getErrorcode() + "", CommonErrorCode.IS_USED_USERID.getGmessage());
					} else {
						return CommonUtil.setResponseObject(null, CommonErrorCode.SUCCESS.getErrorcode() + "", CommonErrorCode.SUCCESS.getGmessage() + " : " + CommonErrorCode.SUCCESS.getDmessage());
					}
				}
			}else{
				HashMap map = dao.getMyId(params);
				if (map==null) {
					if(request.getHeader("Language").equals("en")){
						return CommonUtil.setResponseObject(null, CommonErrorCode.INVALID_ID.getErrorcode() + "", CommonErrorCode.INVALID_ID.getGmessage());
					}else{
						return CommonUtil.setResponseObject(null, CommonErrorKhmerCode.INVALID_ID.getErrorcode() + "", CommonErrorKhmerCode.INVALID_ID.getGmessage());
					}
				}else{
					if ((Integer) map.get("status") > 1){
						return CommonUtil.setResponseObject(null, CommonErrorCode.SUSPEND_USER.getErrorcode() + "", CommonErrorCode.SUSPEND_USER.getGmessage());
					} else{
						return CommonUtil.setResponseObject(null, CommonErrorCode.SUCCESS.getErrorcode() + "", CommonErrorCode.SUCCESS.getGmessage() + " : " + CommonErrorCode.SUCCESS.getDmessage());
					}
				}
			}
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode()+"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResTrDTO signUp(HttpServletRequest request, MultipartFile profileUrl, HashMap params) {

		List<ORGReq020> list = (List<ORGReq020>) params.get("agreeList");

		if(list==null){
			return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}else{
			for (ORGReq020 data : list) {
				if(StringUtils.isBlank(data.getTermOrd())){
					return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
				}
			}
		}

		/* userId 추가된 소스 - userId null 체크 -> 0615 userid 체크 제외(버전이 낮은 경우 userid 적용이 안되어 있음) */
		if (StringUtils.isBlank((String)params.get("language")) ||
				StringUtils.isBlank((String)params.get("password"))  || StringUtils.isBlank((String)params.get("userName"))) {
			return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		if (!(((String) params.get("language")).toLowerCase().equals("en") || ((String) params.get("language")).toLowerCase().equals("khr"))) {
			return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.INVALID_PARAMETER.getErrorcode() + "", CommonErrorCode.INVALID_PARAMETER.getGmessage() + " : " + CommonErrorCode.INVALID_PARAMETER.getDmessage());
		}

		if (StringUtils.isBlank((String)params.get("birth"))) {
			params.put("birth", null);
		}else{
			params.put("birthday", DateUtil.formatDateToyyyymmdd((String)params.get("birth")));
		}

		if (StringUtils.isBlank((String)params.get("email"))) {
			params.put("email", null);
		}

		if(!StringUtils.isBlank((String)params.get("gender"))){
			if(params.get("gender").equals("1")) params.put("gender","m");
			else if(params.get("gender").equals("2")) params.put("gender","f");
			else if(params.get("gender").equals("3")) params.put("gender","o");
		}

		String password;

		try {
			password = String.valueOf(Encrypt.encriptSHA256((String) params.get("password")));
		} catch (Exception e) {
			return CommonUtil.setResponseTrObject(null, 0,CommonErrorCode.INVALID_PARAMETER.getErrorcode() + "", CommonErrorCode.INVALID_PARAMETER.getGmessage() + " : " + CommonErrorCode.INVALID_PARAMETER.getDmessage());
		}
		params.put("password", password);
		params.put("grantType", "signup");
		if(((String) params.get("language")).toLowerCase().equals("en")){
			params.put("language", "D001");
		}else{
			params.put("language", "D002");
		}

		String phone = (String) params.get("phoneNumber");
		// 통합인증서버 -> 국가코드 포함 전화번호로 넘기기 phone으로
		params.put("phone", phone);

		String url = GlobalObjects.getUnionUrl() + "/user";

		HttpHeaders headers = new HttpHeaders();
		headers.add("AppKey", GlobalObjects.getAppKey());
		// 회원가입 시 AuthKey는 params의 uuid hash key 256 (소문자)
		try {
			String uuidHash256 = Encrypt.encriptSHA256((String) params.get("uuid")).toLowerCase();
			headers.add("AuthKey", uuidHash256);
		} catch (Exception e) {
			return CommonUtil.setResponseTrObject(null, 0,CommonErrorCode.INVALID_PARAMETER.getErrorcode() + "", CommonErrorCode.INVALID_PARAMETER.getGmessage() + " : " + CommonErrorCode.INVALID_PARAMETER.getDmessage());
		}
		headers.setContentType(MediaType.valueOf("application/json"));
		headers.setAccept(Collections.singletonList(MediaType.valueOf(request.getHeader("accept"))));
		headers.setContentLanguage(Locale.forLanguageTag(request.getHeader("Language")));
		HttpEntity<HashMap> req = new HttpEntity<>(params, headers);

		ResCommDTO response = RestTemplateUtils.postRequest(url, req);

		int cnt = 0;

		if (!response.getCode().equals("0")) {
			return CommonUtil.setResponseTrObject(response.getData(), cnt,  response.getCode() + "", response.getMessage());
		} else {
			try {
				// 발급된 AngkorId 가져오기
				HashMap mapRes = (HashMap) response.getData();
				params.put("angkorId", mapRes.get("angkorId"));

				// 1. update sendbird user info
				String profile_url = SendbirdUtils.updateUserBySendbird(params, profileUrl);

				if(StringUtils.isBlank(profile_url)){
					params.put("profileUrl", null);
				}else{
					params.put("profileUrl", profile_url);
				}

				// 국가코드 뺀 정상 번호
				String oriPhone = ((String) params.get("phoneNumber")).replaceAll("^" + params.get("phoneCode"), "");

				if (oriPhone.startsWith("10")) {
					oriPhone = "0" + oriPhone;
				}

				params.put("oriPhone", oriPhone);

				// 2. 등록된 profile url로 내부 서버 회원 가입
				cnt = dao.signUp(params);

				// 3. UUID 새로 발급받아 같은 번호로 다시 가입하는 경우 -> 이전 del_yn 'N'인 계정 찾아 삭제여부 변경 단계 필요
				dao.deleteSamePhoneUser(params);
				if(!params.get("deleteAngkorId").equals("N")){
					// 삭제시킨 angkorId 관련된 data 삭제
					dao.deleteFavorite(params);
					dao.deleteFriend(params);
					dao.deleteBlock(params);
				}

				// 4. 입력한 이메일을 이전에 누군가가 사용하고 있었던 경우 그 이전 사용자의 2차인증 해제
				if(!StringUtils.isBlank((String) params.get("email"))){
					dao.checkAndUpdateEmailIsUsed(params);
				}

				if (cnt > 0) {
					// insert ank_user_agreement
					for (ORGReq020 data : list) {
						HashMap map = new HashMap<>();
						map.put("guid", UUID.randomUUID().toString());
						map.put("termOrd", data.getTermOrd());
						map.put("agreement", data.isAgreement() ? 1 : 0);
						map.put("angkorId", params.get("angkorId"));

						dao.addUserAgree(map);
					}
					if(!StringUtils.isBlank((String)params.get("profileUrl"))){
						// insert ank_usericon
						HashMap map = new HashMap<>();
						map.put("angkorId", params.get("angkorId"));
						map.put("profileUrl", params.get("profileUrl"));
						dao.addUserIcon(map);
					}

					// insert (my system basic setting)
					dao.addSysVersion(params); // ank_sys_version
					dao.addSysNoti(params); // ank_sys_noti
					dao.addSysSecurity(params); // ank_sys_security
					dao.addSysFriends(params); // ank_sys_friends
					dao.addSysChatting(params); // ank_sys_chatting
					dao.addSysDisplay(params); // ank_sys_display

					Map<String, Map<String, Object>> settingData = new HashMap<>();
					HashMap<String, Object> chatting; chatting = loginDao.getChatting(params);
					HashMap<String, Object> display; display = loginDao.getDisplay(params);
					HashMap<String, Object> friend; friend = loginDao.getFriends(params);
					HashMap<String, Object> noti; noti = loginDao.getNoti(params);
					HashMap<String, Object> security; security = loginDao.getPrivacy(params);

					settingData.put("chatting", chatting);
					settingData.put("display", display);
					settingData.put("friend", friend);
					settingData.put("noti", noti);
					settingData.put("security", security);

					// 5. update sendbird user metadata info
					params.put("inOnlineRange", security.get("inOnlineRange").toString());
					params.put("inPhotoRange", security.get("inPhotoRange").toString());
					SendbirdUtils.updateUserMetaDataBySendbird(params);

					HashMap map = dao.getUser(params);

					HashMap commRes = (HashMap) response.getData();
					map.put("settingData", settingData);
					map.put("settingData", settingData);
					map.put("authKey", commRes.get("authKey"));
					map.put("accessToken", commRes.get("accessToken"));
					if(map.get("qrcodeUrl")!=null){
						map.put("qrcodeUrl", GlobalObjects.getAapiUrl() + "upload/qrcode/" + map.get("qrcodeUrl"));
					}

					return CommonUtil.setResponseTrObject(map, cnt, CommonErrorCode.SUCCESS.getErrorcode() + "", CommonErrorCode.SUCCESS.getGmessage() + " : " + CommonErrorCode.SUCCESS.getDmessage());
				} else {
					return CommonUtil.setResponseTrObject(response.getData(), cnt, CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage() + " : " + CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
				}
			} catch (Exception e) {
				return CommonUtil.setResponseTrObject(response.getData(), cnt, CommonErrorCode.INTERNAL_SERVER_ERROR + "", CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage() + " : " + CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
			}
		}

	}

	public ResCommDTO getEmail(HttpServletRequest request, HashMap params) {

		if (StringUtils.isBlank((String)params.get("email")) || StringUtils.isBlank((String)params.get("funcType"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		if (!(((String) params.get("funcType")).toLowerCase().equals("join") || ((String) params.get("funcType")).toLowerCase().equals("check"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.INVALID_PARAMETER.getErrorcode() + "", CommonErrorCode.INVALID_PARAMETER.getGmessage() + " : " + CommonErrorCode.INVALID_PARAMETER.getDmessage());
		}

		String url = GlobalObjects.getUnionUrl() + "/user/email";

		HttpHeaders headers = new HttpHeaders();
		headers.add("AppKey", GlobalObjects.getAppKey());
		headers.setContentType(MediaType.valueOf(request.getHeader("Content-Type")));
		headers.setAccept(Collections.singletonList(MediaType.valueOf(request.getHeader("accept"))));
		headers.setContentLanguage(Locale.forLanguageTag(request.getHeader("Language")));

		params.put("grantType", "password");
		HttpEntity<HashMap> req = new HttpEntity<>(params, headers);

		ResCommDTO response = RestTemplateUtils.postRequest(url, req);

		return response;
	}

	public ResCommDTO getUser(HttpServletRequest request, HashMap params) {

		if (StringUtils.isBlank((String)params.get("angkorId"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		String url = GlobalObjects.getUnionUrl() + "/user/" + params.get("angkorId");

		HttpHeaders headers = new HttpHeaders();
		headers.add("AppKey", GlobalObjects.getAppKey());
		headers.add("Authorization", request.getHeader("Authorization").toLowerCase());
		headers.setContentLanguage(Locale.forLanguageTag(request.getHeader("Language")));

		headers.setAccept(Collections.singletonList(MediaType.valueOf(request.getHeader("accept"))));
		HttpEntity<String> req = new HttpEntity<String>("", headers);

		ResCommDTO response = RestTemplateUtils.getRequest(url, req);

		HashMap map=null;

		if(!response.getCode().equals("0")){
			return CommonUtil.setResponseObject(response.getData(), response.getCode() + "", response.getMessage());
		}else{
			try {
				map = dao.getUser(params);

				HashMap mapRes = (HashMap) response.getData();
				map.put("authKey", mapRes.get("authKey"));

				return CommonUtil.setResponseObject(map,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
			} catch (Exception e) {
				return CommonUtil.setResponseObject(map,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode()+"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
			}
		}
	}

	public ResCommDTO findUserId(HttpServletRequest request, HashMap params) {

		if (StringUtils.isBlank((String)params.get("email")) || StringUtils.isBlank((String)params.get("certNumber"))) {
			return CommonUtil.setResponseObject(null,CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		try {
			// 1. email 인증 확인
			// certNum, certNumber
			HashMap emailReq = new HashMap();

			emailReq.put("email", params.get("email"));
			emailReq.put("certNumber", params.get("certNumber"));

			ResCommDTO emailRes = certEmailService.isAuthCode(request, emailReq);

			if (emailRes.getCode().equals("0")) {
				HashMap map = dao.getRegistEmailUser(params);
				if (map == null) {
					if(request.getHeader("Language").equals("en")){
						return CommonUtil.setResponseObject(null, CommonErrorCode.INVALID_ID.getErrorcode() + "", CommonErrorCode.INVALID_ID.getGmessage());
					}else{
						return CommonUtil.setResponseObject(null, CommonErrorKhmerCode.INVALID_ID.getErrorcode() + "", CommonErrorKhmerCode.INVALID_ID.getGmessage());
					}
				} else {
					// 가입되어 있는 회원일 경우 - userId 반환
					HashMap resMap = new HashMap();
					resMap.put("userId", map.get("userId"));
					return CommonUtil.setResponseObject(resMap, CommonErrorCode.SUCCESS.getErrorcode() + "", CommonErrorCode.SUCCESS.getGmessage() + " : " + CommonErrorCode.SUCCESS.getDmessage());
				}
			}else{
				return CommonUtil.setResponseObject(emailRes.getData(), emailRes.getCode() + "", emailRes.getMessage());
			}
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage() + " : " + CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResTrDTO setUserId(HttpServletRequest request, HashMap params) {

		if (StringUtils.isBlank((String)params.get("angkorId")) || StringUtils.isBlank((String)params.get("userId")) ||  StringUtils.isBlank((String)params.get("publicKey")) ) {
			return CommonUtil.setResponseTrObject(null, 0,CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		String url = GlobalObjects.getUnionUrl() + "/user/userid";

		HttpHeaders headers = new HttpHeaders();
		headers.add("AppKey", GlobalObjects.getAppKey());
		if(request.getHeader("Authorization").toLowerCase().contains("null")){
			String authorization = request.getHeader("Authorization").toLowerCase().replace("null", "").trim();
			headers.add("Authorization", "bearer " + authorization);
		}else{
			headers.add("Authorization", request.getHeader("Authorization").toLowerCase());
		}
		headers.setContentType(MediaType.valueOf(request.getHeader("Content-Type")));
		headers.setAccept(Collections.singletonList(MediaType.valueOf(request.getHeader("accept"))));
		headers.setContentLanguage(Locale.forLanguageTag(request.getHeader("Language")));

		params.put("grantType", "session");
		HttpEntity<HashMap> req = new HttpEntity<>(params, headers);

		ResCommDTO response = RestTemplateUtils.postRequest(url, req);

		int cnt = 0;

		if (!response.getCode().equals("0")) {
			return CommonUtil.setResponseTrObject(response.getData(), cnt,  response.getCode() + "", response.getMessage());
		}else {
			try {
				cnt = dao.updateUserId(params);
				if (cnt > 0) {
					return CommonUtil.setResponseTrObject(response.getData(), cnt, CommonErrorCode.SUCCESS.getErrorcode() + "", CommonErrorCode.SUCCESS.getGmessage() + " : " + CommonErrorCode.SUCCESS.getDmessage());
				} else {
					return CommonUtil.setResponseTrObject(response.getData(), cnt, CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage() + " : " + CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
				}
			} catch (Exception e) {
				return CommonUtil.setResponseTrObject(response.getData(), cnt, CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage() + " : " + CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
			}
		}
	}

	public ResTrDTO deleteUser(HttpServletRequest request, HashMap params) {

		if (StringUtils.isBlank((String)params.get("angkorId"))) {
			return CommonUtil.setResponseTrObject(null, 0,CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		String url = GlobalObjects.getUnionUrl() + "/user/" + params.get("angkorId");

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

		ResCommDTO response = RestTemplateUtils.deleteRequest(url, req);

		int cnt = 0;

		if (!response.getCode().equals("0")) {
			return CommonUtil.setResponseTrObject(response.getData(), cnt,  response.getCode() + "", response.getMessage());
		}else {
			try {
				cnt = dao.deleteUser(params);
				// 삭제시킨 angkorId 관련된 data 삭제
				params.put("deleteAngkorId", params.get("angkorId"));
				dao.deleteFavorite(params);
				dao.deleteFriend(params);
				dao.deleteBlock(params);
				if (cnt > 0) {
					// sendbird 탈퇴
					SendbirdUtils.deleteUserBySendbird(params);
					return CommonUtil.setResponseTrObject(response.getData(), cnt, CommonErrorCode.SUCCESS.getErrorcode() + "", CommonErrorCode.SUCCESS.getGmessage() + " : " + CommonErrorCode.SUCCESS.getDmessage());
				} else {
					return CommonUtil.setResponseTrObject(response.getData(), cnt, CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage() + " : " + CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
				}
			} catch (Exception e) {
				return CommonUtil.setResponseTrObject(response.getData(), cnt, CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage() + " : " + CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
			}
		}
	}

	public ResTrDTO updateEmail(HttpServletRequest request, HashMap params) {

		if (StringUtils.isBlank((String)params.get("angkorId")) || StringUtils.isBlank((String)params.get("email")) || StringUtils.isBlank((String)params.get("certNumber"))) {
			return CommonUtil.setResponseTrObject(null, 0,CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		try {
			// 1. email 인증 확인
			// certNum, certNumber
			HashMap emailReq = new HashMap();

			emailReq.put("email", params.get("email"));
			emailReq.put("certNumber", params.get("certNumber"));

			ResCommDTO emailRes = certEmailService.isAuthCode(request, emailReq);

			if (emailRes.getCode().equals("0")) {

				params.put("grantType", "session");

				String url = GlobalObjects.getUnionUrl() + "/user/emailupdate";

				HttpHeaders headers = new HttpHeaders();
				headers.add("AppKey", GlobalObjects.getAppKey());
				if(request.getHeader("Authorization").toLowerCase().contains("null")){
					String authorization = request.getHeader("Authorization").toLowerCase().replace("null", "").trim();
					headers.add("Authorization", "bearer " + authorization);
				}else{
					headers.add("Authorization", request.getHeader("Authorization").toLowerCase());
				}
				headers.setContentType(MediaType.valueOf(request.getHeader("Content-Type")));
				headers.setAccept(Collections.singletonList(MediaType.valueOf(request.getHeader("accept"))));
				headers.setContentLanguage(Locale.forLanguageTag(request.getHeader("Language")));
				HttpEntity<HashMap> req = new HttpEntity<>(params, headers);

				ResCommDTO response = RestTemplateUtils.postRequest(url, req);

				int cnt = 0;

				if (!response.getCode().equals("0")) {
					return CommonUtil.setResponseTrObject(response.getData(), cnt,  response.getCode() + "", response.getMessage());
				}else{
					cnt = dao.updateEmail(params);
					if (cnt > 0) {
						// 입력한 이메일을 이전에 누군가가 사용하고 있었던 경우 그 이전 사용자의 2차인증 해제
						dao.checkAndUpdateEmailIsUsed(params);
						return CommonUtil.setResponseTrObject(response.getData(), cnt, CommonErrorCode.SUCCESS.getErrorcode() + "", CommonErrorCode.SUCCESS.getGmessage() + " : " + CommonErrorCode.SUCCESS.getDmessage());
					} else {
						return CommonUtil.setResponseTrObject(response.getData(), cnt, CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage() + " : " + CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
					}
				}
			}else{
				return CommonUtil.setResponseTrObject(emailRes.getData(), 0,emailRes.getCode() + "", emailRes.getMessage());
			}
		} catch (Exception e) {
			return CommonUtil.setResponseTrObject(null, 0,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage() + " : " + CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResTrDTO updatePhone(HttpServletRequest request, HashMap params) {

		if (StringUtils.isBlank((String)params.get("phoneCode")) || StringUtils.isBlank((String)params.get("phoneNumber"))) {
			return CommonUtil.setResponseTrObject(null, 0,CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		if (StringUtils.isBlank((String)params.get("angkorId")) || StringUtils.isBlank((String)params.get("authKey"))) {
			return CommonUtil.setResponseTrObject(null, 0,CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		try {
			// 1. sms 인증 확인
			// certNum, grantType, phone, uuid
			HashMap smsReq = new HashMap();

			// authkey null 체크
			if(((String) params.get("authKey")).toLowerCase().contains("null")){
				String authorization = request.getHeader("Authorization").toLowerCase().replace("null", "").trim();
				params.put("authKey", authorization);
			}

			smsReq.put("authKey", params.get("authKey"));
			smsReq.put("certNumber", params.get("certNumber"));
			smsReq.put("grantType", params.get("grantType"));
			smsReq.put("phoneCode", params.get("phoneCode"));
			smsReq.put("phoneNumber", params.get("phoneNumber"));
			smsReq.put("uuid", request.getHeader("Uuid"));

			ResCommDTO smsRes = certSmsService.isAuthCode(request, smsReq);

			if (smsRes.getCode().equals("0")) {
				// 2. 확인되면 전화번호 통합인증 update
				String url = GlobalObjects.getUnionUrl() + "/user/phone";

				HttpHeaders headers = new HttpHeaders();
				headers.add("AppKey", GlobalObjects.getAppKey());
				if(request.getHeader("Authorization").toLowerCase().contains("null")){
					String authorization = request.getHeader("Authorization").toLowerCase().replace("null", "").trim();
					headers.add("Authorization", "bearer " + authorization);
				}else{
					headers.add("Authorization", request.getHeader("Authorization").toLowerCase());
				}
				headers.setContentType(MediaType.valueOf(request.getHeader("Content-Type")));
				headers.setAccept(Collections.singletonList(MediaType.valueOf(request.getHeader("accept"))));
				headers.setContentLanguage(Locale.forLanguageTag(request.getHeader("Language")));

				HashMap unionMap = new HashMap();
				unionMap.put("angkorId", params.get("angkorId"));
				unionMap.put("grantType", "session");
				unionMap.put("phone", params.get("phoneNumber"));

				HttpEntity<HashMap> req = new HttpEntity<>(unionMap, headers);

				ResCommDTO response = RestTemplateUtils.postRequest(url, req);

				int cnt = 0;

				if (!response.getCode().equals("0")) {
					return CommonUtil.setResponseTrObject(response.getData(), cnt,  response.getCode() + "", response.getMessage());
				}else{
					// 3. 내부 서버 update

					// 기존 사용중인 사용자 angkorId 비활성화 처리 후 관련 친구 모두 삭제
					HashMap dMap = new HashMap();
					dMap.put("phone", params.get("phoneNumber"));
					dMap.put("angkorId", params.get("angkorId"));
					dao.deleteSamePhoneUser(dMap);

					if(!dMap.get("deleteAngkorId").equals("N")){
						// 삭제시킨 angkorId 관련된 data 삭제
						dao.deleteFavorite(dMap);
						dao.deleteFriend(dMap);
						dao.deleteBlock(dMap);
					}

					// 4. update sendbird user metadata info
					SendbirdUtils.updateUserMetaDataBySendbird(params);

					// 국가코드 뺀 정상 번호
					String oriPhone = ((String) params.get("phoneNumber")).replaceAll("^" + params.get("phoneCode"), "");

					if (oriPhone.startsWith("10")) {
						oriPhone = "0" + oriPhone;
					}
					params.put("oriPhone", oriPhone);
					cnt = dao.updatePhone(params);

					if (cnt > 0) {
						return CommonUtil.setResponseTrObject(response.getData(), cnt, CommonErrorCode.SUCCESS.getErrorcode() + "", CommonErrorCode.SUCCESS.getGmessage() + " : " + CommonErrorCode.SUCCESS.getDmessage());
					} else {
						return CommonUtil.setResponseTrObject(response.getData(), cnt,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage() + " : " + CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
					}
				}
			}else{
				if(request.getHeader("Language").equals("en")){
					return CommonUtil.setResponseTrObject(smsRes.getData(), 0,CommonErrorCode.API_400603_SMS.getErrorcode()+"",CommonErrorCode.API_400603_SMS.getGmessage());
				}else{
					return CommonUtil.setResponseTrObject(smsRes.getData(), 0,CommonErrorKhmerCode.API_400603_SMS.getErrorcode() +"",CommonErrorKhmerCode.API_400603_SMS.getGmessage());
				}
			}
		} catch (Exception e) {
			return CommonUtil.setResponseTrObject(null, 0,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage() + " : " + CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResTrDTO updatePw(HttpServletRequest request, HashMap params) {

		if (StringUtils.isBlank((String)params.get("email")) || StringUtils.isBlank((String)params.get("certNumber"))) {
			return CommonUtil.setResponseTrObject(null, 0,CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		String url = GlobalObjects.getUnionUrl() + "/user/pw";
		String sha256Pw = "";
		HttpHeaders headers = new HttpHeaders();
		headers.add("AppKey", GlobalObjects.getAppKey());
		try {
			sha256Pw = String.valueOf(Encrypt.encriptSHA256(request.getHeader("AuthKey")));
			headers.add("AuthKey", sha256Pw);
		} catch (Exception e) {
			return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.INVALID_PARAMETER.getErrorcode() + "", CommonErrorCode.INVALID_PARAMETER.getGmessage() + " : " + CommonErrorCode.INVALID_PARAMETER.getDmessage());
		}
		headers.setContentType(MediaType.valueOf(request.getHeader("Content-Type")));
		headers.setAccept(Collections.singletonList(MediaType.valueOf(request.getHeader("accept"))));
		headers.setContentLanguage(Locale.forLanguageTag(request.getHeader("Language")));

		params.put("grantType", "password");
		HttpEntity<HashMap> req = new HttpEntity<>(params, headers);

		ResCommDTO response = RestTemplateUtils.postRequest(url, req);

		int cnt = 0;

		if (!response.getCode().equals("0")) {
			return CommonUtil.setResponseTrObject(response.getData(), cnt,  response.getCode() + "", response.getMessage());
		}else{
			try {
				params.put("newPassword", sha256Pw);
				cnt = dao.updatePw(params);
				if (cnt > 0) {
					return CommonUtil.setResponseTrObject(response.getData(), cnt, CommonErrorCode.SUCCESS.getErrorcode() + "", CommonErrorCode.SUCCESS.getGmessage() + " : " + CommonErrorCode.SUCCESS.getDmessage());
				} else {
					return CommonUtil.setResponseTrObject(response.getData(), cnt, CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage() + " : " + CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
				}
			} catch (Exception e) {
				return CommonUtil.setResponseTrObject(response.getData(), cnt, CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage() + " : " + CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
			}
		}
	}

	public ResTrDTO updatePwChange(HttpServletRequest request, HashMap params) {

		if (StringUtils.isBlank((String)params.get("angkorId")) || StringUtils.isBlank((String)params.get("newPassword")) || StringUtils.isBlank((String)params.get("oldPassword"))) {
			return CommonUtil.setResponseTrObject(null, 0,CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		String newPassword;
		String oldPassword;

		try {
			newPassword = String.valueOf(Encrypt.encriptSHA256((String) params.get("newPassword")));
			oldPassword = String.valueOf(Encrypt.encriptSHA256((String) params.get("oldPassword")));
		} catch (Exception e) {
			return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.INVALID_PARAMETER.getErrorcode() + "", CommonErrorCode.INVALID_PARAMETER.getGmessage() + " : " + CommonErrorCode.INVALID_PARAMETER.getDmessage());
		}
		params.put("newPassword", newPassword);
		params.put("oldPassword", oldPassword);
		params.put("grantType", "password");

		String url = GlobalObjects.getUnionUrl() + "/user/pwchange";

		HttpHeaders headers = new HttpHeaders();
		headers.add("AppKey", GlobalObjects.getAppKey());
		headers.add("Authorization", request.getHeader("Authorization").toLowerCase());
		headers.setContentType(MediaType.valueOf(request.getHeader("Content-Type")));
		headers.setAccept(Collections.singletonList(MediaType.valueOf(request.getHeader("accept"))));
		headers.setContentLanguage(Locale.forLanguageTag(request.getHeader("Language")));
		HttpEntity<HashMap> req = new HttpEntity<>(params, headers);

		ResCommDTO response = RestTemplateUtils.postRequest(url, req);

		int cnt = 0;

		if (!response.getCode().equals("0")) {
			return CommonUtil.setResponseTrObject(response.getData(), cnt,  response.getCode() + "", response.getMessage());
		}else{
			try {
				cnt = dao.updatePwChange(params);
				if (cnt > 0) {
					return CommonUtil.setResponseTrObject(response.getData(), cnt, CommonErrorCode.SUCCESS.getErrorcode() + "", CommonErrorCode.SUCCESS.getGmessage() + " : " + CommonErrorCode.SUCCESS.getDmessage());
				} else {
					return CommonUtil.setResponseTrObject(response.getData(), cnt, CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage() + " : " + CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
				}
			} catch (Exception e) {
				return CommonUtil.setResponseTrObject(response.getData(), cnt, CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage() + " : " + CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
			}
		}
	}

	public ResTrDTO updateInfo(HttpServletRequest request, MultipartFile profileUrl, HashMap params) {

		if (StringUtils.isBlank((String)params.get("angkorId")) || StringUtils.isBlank((String)params.get("userName"))  || StringUtils.isBlank((String)params.get("profileStatus"))) {
			return CommonUtil.setResponseTrObject(null, 0,CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		if (!(params.get("profileStatus").equals("1") || params.get("profileStatus").equals("2") || params.get("profileStatus").equals("3"))) {
			return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.INVALID_PARAMETER.getErrorcode() + "", CommonErrorCode.INVALID_PARAMETER.getGmessage() + " : " + CommonErrorCode.INVALID_PARAMETER.getDmessage());
		}

		if (StringUtils.isBlank((String)params.get("birth"))) {
			params.put("birth", null);
		}else{
			params.put("birthday", DateUtil.formatDateToyyyymmdd((String)params.get("birth")));
		}

		if(StringUtils.isBlank((String)params.get("profileMessage"))){
			params.put("profileMessage", null);
		}

		if(!StringUtils.isBlank((String)params.get("gender"))){
			if(params.get("gender").equals("1")) params.put("gender","m");
			else if(params.get("gender").equals("2")) params.put("gender","f");
			else if(params.get("gender").equals("3")) params.put("gender","o");
		}else{
			params.put("gender", null);
		}

		String url = GlobalObjects.getUnionUrl() + "/user/" + params.get("angkorId");

		HttpHeaders headers = new HttpHeaders();
		headers.add("AppKey", GlobalObjects.getAppKey());

		if(request.getHeader("Authorization").toLowerCase().contains("null")){
			String authorization = request.getHeader("Authorization").toLowerCase().replace("null", "").trim();
			headers.add("Authorization", "bearer " + authorization);
		}else{
			headers.add("Authorization", request.getHeader("Authorization").toLowerCase());
		}
		headers.setContentType(MediaType.valueOf("application/json"));
		headers.setAccept(Collections.singletonList(MediaType.valueOf(request.getHeader("accept"))));
		headers.setContentLanguage(Locale.forLanguageTag(request.getHeader("Language")));

		params.put("grantType", "modify");

		HttpEntity<HashMap> req = new HttpEntity<>(params, headers);

		ResCommDTO response = RestTemplateUtils.postRequest(url, req);

		int cnt = 0;

		if (!response.getCode().equals("0")) {
			return CommonUtil.setResponseTrObject(response.getData(), cnt,  response.getCode() + "", response.getMessage());
		} else {
			try {
				// 프로필 이미지 sendbird 선 수정 후 db 반영해야함
				String profile_url = null;
				try {
					if(params.get("profileStatus").equals("1")){
						// case 1 : 파일이 첨부된 경우 - 메인 프로필 이미지 변경
						profile_url = SendbirdUtils.updateUserBySendbird(params, profileUrl);
						if(StringUtils.isBlank(profile_url)){
							params.put("profileUrl", "");
						}else{
							params.put("profileUrl", profile_url);
						}
					}else if(params.get("profileStatus").equals("2")){
						// case 2 : 파일 첨부되지 않은 경우 - a. 프로필 이미지 변경을 안할 때 (닉네임은 변경하지 않아도 update)
						profile_url = SendbirdUtils.updateImageBySendbird(params);
					}else if(params.get("profileStatus").equals("3")) {
						// case 3 : 파일 첨부되지 않은 경우 - b. 현재 등록된 프로필 이미지 삭제하고 기본 이미지로 설정할 때
						params.put("profile_url", "");
						profile_url = SendbirdUtils.updateImageBySendbird(params);
						if(StringUtils.isBlank(profile_url)){
							params.put("profileUrl", "");
						}

					}
				} catch (IOException e) {
					return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.SEND_API_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.SEND_API_SERVER_ERROR.getGmessage() + " : " + CommonErrorCode.SEND_API_SERVER_ERROR.getDmessage());
				}

				cnt = dao.updateInfo(params);

				if (cnt > 0) {
					if(!StringUtils.isBlank((String)params.get("profileUrl"))){
						// insert ank_usericon
						HashMap map = new HashMap<>();
						map.put("angkorId", params.get("angkorId"));
						map.put("profileUrl", params.get("profileUrl"));
						dao.addUserIcon(map);
					}

					HashMap map = dao.getUser(params);

					// 최신 프로필 이미지 정보 get - app 요청
					String recentProfileUrl = dao.getMainProfileUrl(map);

					HashMap mapRes = (HashMap) response.getData();
					map.put("authKey", mapRes.get("authKey"));
					if(!StringUtils.isBlank(recentProfileUrl)) map.put("recent_profile_url", recentProfileUrl);
					else map.put("recent_profile_url", null);

					return CommonUtil.setResponseTrObject(map, cnt, CommonErrorCode.SUCCESS.getErrorcode() + "", CommonErrorCode.SUCCESS.getGmessage() + " : " + CommonErrorCode.SUCCESS.getDmessage());
				} else {
					return CommonUtil.setResponseTrObject(response.getData(), cnt, CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage() + " : " + CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
				}
			} catch (Exception e) {
				return CommonUtil.setResponseTrObject(response.getData(), cnt, CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage() + " : " + CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
			}
		}
	}

	public ResCommDTO uuid(HttpServletRequest request) {

		String url = GlobalObjects.getUnionUrl() + "/uuid";

		HttpHeaders headers = new HttpHeaders();
		headers.add("AppKey", GlobalObjects.getAppKey());
		headers.setAccept(Collections.singletonList(MediaType.valueOf(request.getHeader("accept"))));
		headers.setContentLanguage(Locale.forLanguageTag(request.getHeader("Language")));

		HttpEntity<String> req = new HttpEntity<String>("", headers);

		ResCommDTO response = RestTemplateUtils.getRequest(url, req);

		return CommonUtil.setResponseObject(response.getData(), response.getCode() + "", response.getMessage());

	}

	public ResTrDTO updatePosition(HashMap params) {

		int cnt=0;

		if (StringUtils.isBlank((String)params.get("angkorId"))) {
			return CommonUtil.setResponseTrObject(null, 0,CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		if (StringUtils.isBlank((String)params.get("latitude")) || StringUtils.isBlank((String)params.get("longitude"))) {
			return CommonUtil.setResponseTrObject(null, 0,CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		if(params.get("chkCd")==null){
			params.put("chkCd", 1);
		}

		try {
			int check = dao.checkPositionCnt(params);
			if(check>0){
				cnt = dao.updatePosition(params);
			}else{
				cnt= dao.insertPosition(params);
			}
			return CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
		} catch (Exception e) {
			return CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResTrDTO profileImage(HttpServletRequest request, HashMap params) {

		int cnt = 0;

		if (StringUtils.isBlank((String) params.get("angkorId")) || params.get("profileSeq") == null) {
			return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		if (!((Integer) params.get("status") == 1 || (Integer) params.get("status") == 2 || (Integer) params.get("status") == 3)) {
			return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.INVALID_PARAMETER.getErrorcode() + "", CommonErrorCode.INVALID_PARAMETER.getGmessage() + " : " + CommonErrorCode.INVALID_PARAMETER.getDmessage());
		}

		ResTrDTO authkeyRes;
		boolean updateYn = false;
		HashMap mapRes = new HashMap();
		try {
			// 나의 프로필 이미지인지 확인
			HashMap map = dao.checkMyProfileImage(params);
			if (map != null) {
				switch ((Integer) params.get("status")) {
					case 1:
						// update main profile image
						params.put("profile_url", map.get("icon"));
						// sendbird update
						String profile_url = SendbirdUtils.updateImageBySendbird(params);
						if (StringUtils.isBlank(profile_url)) {
							return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.SEND_API_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.SEND_API_SERVER_ERROR.getGmessage() + " : " + CommonErrorCode.SEND_API_SERVER_ERROR.getDmessage());
						} else {
							// 기존 이미지 create_dt 업데이트
							cnt = dao.updateProfile(params);
						}
						updateYn = true;
						break;
					case 2:
						// update hide profile image
						cnt = dao.hideProfile(params);
						// 프로필 이미지로 설정되어 있는 사진을 숨김/삭제 처리할 경우 메인 profile 이미지도 null처리
						if (map.get("currentProfileUrl")!=null && map.get("currentProfileUrl").equals(map.get("icon"))) {
							updateYn = true;
							map.put("icon", null);
						}
						break;
					case 3:
						// delete profile image
						cnt = dao.deleteProfile(params);
						// 프로필 이미지로 설정되어 있는 사진을 숨김/삭제 처리할 경우 메인 profile 이미지도 null처리
						if (map.get("currentProfileUrl")!=null && map.get("currentProfileUrl").equals(map.get("icon"))) {
							updateYn = true;
							map.put("icon", null);
						}
						break;
				}
				// aAPI profileUrl 업데이트가 필요한 경우
				if (updateYn) {
					// aAPI update
					HashMap info = dao.getUser(params);
					HashMap updateReq = new HashMap();
					updateReq.put("profile_url", map.get("icon"));
					updateReq.put("userName", info.get("userName"));
					updateReq.put("angkorId", info.get("angkorId"));
					authkeyRes = aApiUpdateInfo(updateReq);

					HashMap authkeyResMap = (HashMap) authkeyRes.getData();
					mapRes.put("recent_profile_url", authkeyResMap.get("recent_profile_url"));

					map.put("angkorId", info.get("angkorId"));

					if (!authkeyRes.getCode().equals("0")) {
						return CommonUtil.setResponseTrObject(null, cnt, authkeyRes.getCode() + "", authkeyRes.getMessage());
					}
					return CommonUtil.setResponseTrObject(mapRes, cnt, CommonErrorCode.SUCCESS.getErrorcode() + "", CommonErrorCode.SUCCESS.getGmessage() + " : " + CommonErrorCode.SUCCESS.getDmessage());
				} else {
					// 프로필 이미지가 변경될 경우 사용자 프로필 이미지 정보 get - app 요청
					String recentProfileUrl = dao.getMainProfileUrl(map);
					if(!StringUtils.isBlank(recentProfileUrl)) mapRes.put("recent_profile_url", recentProfileUrl);
					else mapRes.put("recent_profile_url", null);
					return CommonUtil.setResponseTrObject(mapRes, cnt, CommonErrorCode.SUCCESS.getErrorcode() + "", CommonErrorCode.SUCCESS.getGmessage() + " : " + CommonErrorCode.SUCCESS.getDmessage());
				}
			}else{
				return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.INVALID_PARAMETER.getErrorcode() + "", CommonErrorCode.INVALID_PARAMETER.getGmessage() + " : " + CommonErrorCode.INVALID_PARAMETER.getDmessage());
			}
		} catch (Exception e) {
			return CommonUtil.setResponseTrObject(null, cnt, CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage() + " : " + CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResTrDTO getDau() {

		int cnt=0;

		// 1. 해당 날짜의 데이터가 이미 등록되어 있는지 확인
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c1 = Calendar.getInstance();
		c1.add(Calendar.DATE, -1);
		String yesterday = sdf.format(c1.getTime());

		HashMap params = new HashMap();
		params.put("date", yesterday);

//		try {
//			int result = dao.checkDau(params);
//			if(result>0){
//				return CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.IS_DAU_YESTERDAY.getErrorcode() +"",CommonErrorCode.IS_DAU_YESTERDAY.getGmessage()+" : "+CommonErrorCode.IS_DAU_YESTERDAY.getDmessage());
//			}
//		} catch (Exception e) {
//			return CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
//		}

		

		try {
			
			HashMap response = SendbirdUtils.getDailyUsers(params);
						
			if(response != null && response.get("dau") != null){
				params.put("dau", response.get("dau"));
				params.put("dau_type", "d");
				cnt = dao.insertDau(params);
			}
			
			response = SendbirdUtils.getMonthUsers(params);
			
			if(response != null && response.get("mau") != null){
				params.put("dau", response.get("mau"));
				params.put("dau_type", "m");
				cnt = dao.insertDau(params);
			}
		} catch (Exception e) {
			//return CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
		
		
		// 매시간 Dau & Mau 요청하여 데이터 저장하기
		try {
			params.put("date", "");
			
			HashMap response = SendbirdUtils.getDailyUsers(params);
			
			HashMap response2 = SendbirdUtils.getMonthUsers(params);
			
			if(response != null && response.get("dau") != null){
				params.put("act_cnt", response.get("dau"));
				params.put("act_type", "d");
				cnt = dao.insertUserActivation(params);
			}
			
			if(response2 != null && response2.get("mau") != null){
				params.put("act_cnt", response2.get("mau"));
				params.put("act_type", "m");
				cnt = dao.insertUserActivation(params);
			}
		} catch (Exception e) {
			//return CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
		
		return CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
	}

	public void callLog(HashMap params){
		try {
			dao.insertTest(params);
			
			PushUtil pu = new PushUtil();
			pu.push((String)params.get("str"), dao);
			
		} catch (Exception e) {

		}
		
	}

	public ResTrDTO aApiUpdateInfo(HashMap params) {

		int cnt = 0;
		String profile_url;

		try {
			// 프로필 이미지 sendbird 선 수정 후 db 반영해야함
			if(StringUtils.isBlank((String)params.get("profile_url"))){
				params.put("profile_url", "");
			}

			profile_url = SendbirdUtils.updateImageBySendbird(params);

			if(StringUtils.isBlank(profile_url)){
				params.put("profileUrl", "");
			}else{
				params.put("profileUrl", profile_url);
			}

			cnt = dao.updateInfo(params);
			if (cnt > 0) {
				HashMap map = dao.getUser(params);

				// 프로필 이미지가 변경될 경우 최신 프로필 이미지 정보 get - app 요청
				String recentProfileUrl = dao.getMainProfileUrl(map);
				if(!StringUtils.isBlank(recentProfileUrl)) map.put("recent_profile_url", recentProfileUrl);
				else map.put("recent_profile_url", null);

				return CommonUtil.setResponseTrObject(map, cnt, CommonErrorCode.SUCCESS.getErrorcode() + "", CommonErrorCode.SUCCESS.getGmessage() + " : " + CommonErrorCode.SUCCESS.getDmessage());
			} else {
				return CommonUtil.setResponseTrObject(null, cnt, CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage() + " : " + CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
			}
		} catch (Exception e) {
			return CommonUtil.setResponseTrObject(null, cnt, CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage() + " : " + CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}

	}
	
	public ResCommDTO setChannelSound(HttpServletRequest request, HashMap params) {
		try {
	
			if (StringUtils.isBlank((String)params.get("channelUrl")) || StringUtils.isBlank((String)params.get("soundNm"))) {
				return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
			}
			
			Integer cnt = dao.updateUserChatSound(params);
			return CommonUtil.setResponseObject(params.get("soundNm"),CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode()+"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}
	
	
	public ResCommDTO getChannelSound(HttpServletRequest request, HashMap params) {
		try {
			
			if (StringUtils.isBlank((String)params.get("channelUrl"))) {
				return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
			}
			String soundNm = dao.getUserChatSound(params);
			if(soundNm == null) {
				soundNm = "angkor_angkor.mp3";
			}
			return CommonUtil.setResponseObject(soundNm,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode()+"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}
	
	public ResCommDTO setPushToken(HttpServletRequest request, HashMap params) {
		try {
			
			if (StringUtils.isBlank((String)params.get("os")) || StringUtils.isBlank((String)params.get("token"))) {
				return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
			}
			
			dao.insertUserPushToken(params);
			
//			HashMap tmp = dao.getUserPushToken(params);
			
			return CommonUtil.setResponseObject(null,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode()+"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}
	
	public ResCommDTO setOnOff(HttpServletRequest request, HashMap params) {
		try {
			params.put("onoff",request.getParameter("onoff"));
			Integer cnt = dao.updateUserOnoff(params);
			return CommonUtil.setResponseObject(cnt,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());

		} catch (Exception e) {
			return CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}
	
	
	public ResCommDTO getMessageLog(HttpServletRequest request, HashMap params) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseObject(null, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		if (StringUtils.isBlank((String)params.get("angkorId"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}
		
		try {
			params.put("createdAt", request.getParameter("CreatedAt"));
			params.put("customType", request.getParameter("CustomType"));
			List<Map<String, Object>>  list = new ArrayList<>();
			for(String str : dao.getMessagePayload(params)){
				
				JSONObject jsonObject = new JSONObject(str);
				Map<String, Object> newMap = jsonObject.toMap().entrySet().stream()
			            .collect(Collectors.toMap(entry -> StringUtil.convertToPascalCase(entry.getKey()), entry -> entry.getValue()));
				
				list.add(newMap);
			}
			
			return CommonUtil.setResponseObject(list,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());

		} catch (Exception e) {
			return CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}
	
	
	
	

}
