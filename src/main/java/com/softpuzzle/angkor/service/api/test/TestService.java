package com.softpuzzle.angkor.service.api.test;

import com.softpuzzle.angkor.database.mapper.api.login.LoginMapper;
import com.softpuzzle.angkor.database.mapper.api.test.TestMapper;
import com.softpuzzle.angkor.database.mapper.api.user.UserMapper;
import com.softpuzzle.angkor.gcache.GlobalObjects;
import com.softpuzzle.angkor.http.request.ORGReq020;
import com.softpuzzle.angkor.http.request.ORGReq100;
import com.softpuzzle.angkor.http.response.common.ResCommDTO;
import com.softpuzzle.angkor.http.response.common.ResTrDTO;
import com.softpuzzle.angkor.utility.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestService {

	@Autowired
	private TestMapper dao;
	@Autowired
	private UserMapper userDao;
	@Autowired
	private LoginMapper loginDao;

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

		/* userId 추가된 소스 - userId null 체크 */
		if (StringUtils.isBlank((String)params.get("language")) || StringUtils.isBlank((String)params.get("userId")) ||
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

				// user public key 개행 문자 제거
				String pKey = (String) params.get("publicKey");
				if (!StringUtils.isBlank(pKey)) {
					// 개행 문자 제거
					String publicKey = pKey.replaceAll("\\s", "");
					params.put("publicKey", publicKey);
				}

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

					HashMap map = dao.getUser(params);

					HashMap commRes = (HashMap) response.getData();
					map.put("settingData", settingData);
					map.put("settingData", settingData);
					map.put("authKey", commRes.get("authKey"));
					map.put("accessToken", commRes.get("accessToken"));

					return CommonUtil.setResponseTrObject(map, cnt, CommonErrorCode.SUCCESS.getErrorcode() + "", CommonErrorCode.SUCCESS.getGmessage() + " : " + CommonErrorCode.SUCCESS.getDmessage());
				} else {
					return CommonUtil.setResponseTrObject(response.getData(), cnt, CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage() + " : " + CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
				}
			} catch (Exception e) {
				return CommonUtil.setResponseTrObject(response.getData(), cnt, CommonErrorCode.INTERNAL_SERVER_ERROR + "", CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage() + " : " + CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
			}
		}

	}

	public ResCommDTO signIn(HttpServletRequest request, HashMap params) {

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
			if (!response.getCode().equals("0")) {
				map = dao.getMyId(params);

				// uuid가 일치하지 않을 경우
				// 국가코드 뺀 정상 번호
				String phone = ((String) map.get("phoneNumber")).replaceAll("^" + map.get("phoneCode"), "");

				HashMap resMap = new HashMap();
				resMap.put("phonecode", map.get("phoneCode"));
				resMap.put("phonenumber", phone);

				response.setData(resMap);
				return response;
			} else {
				map = dao.getUser2(params);

				// 로그인 기록 및 개인 공유키 update
				HashMap uMap = new HashMap();
				uMap.put("angkorId", map.get("angkorId"));

				// user public key 개행 문자 제거
				String pKey = (String) params.get("publicKey");
				if (!StringUtils.isBlank(pKey)) {
					// 개행 문자 제거
					String publicKey = pKey.replaceAll("\\s", "");
					uMap.put("publicKey", publicKey);
				}

				dao.updateLoginDtAndPublicKey(uMap);

				Map<String, Map<String, Object>> settingData = new HashMap<>();
				HashMap<String, Object> chatting;
				chatting = dao.getChatting(map);
				HashMap<String, Object> display;
				display = dao.getDisplay(map);
				HashMap<String, Object> friend;
				friend = dao.getFriends(map);
				HashMap<String, Object> noti;
				noti = dao.getNoti(map);
				HashMap<String, Object> security;
				security = dao.getPrivacy(map);

				settingData.put("chatting", chatting);
				settingData.put("display", display);
				settingData.put("friend", friend);
				settingData.put("noti", noti);
				settingData.put("security", security);

				HashMap mapRes = (HashMap) response.getData();
				map.put("authKey", mapRes.get("authKey"));
				map.put("accessToken", mapRes.get("accessToken"));
				map.put("settingData", settingData);

				return CommonUtil.setResponseObject(map, CommonErrorCode.SUCCESS.getErrorcode() + "", CommonErrorCode.SUCCESS.getGmessage() + " : " + CommonErrorCode.SUCCESS.getDmessage());
			}
		}catch (Exception e) {
			return CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode()+"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	/*public ResTrDTO signUpAll(HttpServletRequest request, List<ORGReq100> rdtoList) throws Exception {

		Map<String, Map<String, Object>> signUpData = new HashMap<>();
		int resultCnt = 0;
		for(ORGReq100 rdto : rdtoList){

			HashMap params = CommonUtil.Object2Hashmap(rdto);
			String uuidRes;

			// 1. uuid 발급
			uuidRes = uuid(request);

			Thread.sleep(1500); //1.5초 대기

			params.put("uuid", uuidRes);
			params.put("profileMessage", "testuser");
			params.put("version", "1.0");
			params.put("password", "testuser1!");
			params.put("gender", "1");
			params.put("osType", "ANDROID");
			params.put("language", "en");

			List<ORGReq020> list = new ArrayList<>();
			list.add(ORGReq020.builder().termOrd("1").agreement(true).build());
			list.add(ORGReq020.builder().termOrd("2").agreement(true).build());
			list.add(ORGReq020.builder().termOrd("3").agreement(true).build());

			if (StringUtils.isBlank((String) params.get("language")) || StringUtils.isBlank((String) params.get("password")) || StringUtils.isBlank((String) params.get("userName"))) {
				return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
			}

			if (StringUtils.isBlank((String) params.get("gender")) || StringUtils.isBlank((String) params.get("osType")) || StringUtils.isBlank((String) params.get("version"))) {
				return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
			}

			if (!(params.get("gender").equals("1") || params.get("gender").equals("2") || params.get("gender").equals("3"))) {
				return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.INVALID_PARAMETER.getErrorcode() + "", CommonErrorCode.INVALID_PARAMETER.getGmessage() + " : " + CommonErrorCode.INVALID_PARAMETER.getDmessage());
			}

			if (!(((String) params.get("language")).toLowerCase().equals("en") || ((String) params.get("language")).toLowerCase().equals("khr"))) {
				return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.INVALID_PARAMETER.getErrorcode() + "", CommonErrorCode.INVALID_PARAMETER.getGmessage() + " : " + CommonErrorCode.INVALID_PARAMETER.getDmessage());
			}

			if (StringUtils.isBlank((String) params.get("birth"))) {
				params.put("birth", null);
			}

			if (StringUtils.isBlank((String) params.get("email"))) {
				params.put("email", null);
			}

			if(params.get("gender").equals("1")) params.put("gender","m");
			else if(params.get("gender").equals("2")) params.put("gender","f");
			else if(params.get("gender").equals("3")) params.put("gender","o");

			String password;

			try {
				password = String.valueOf(Encrypt.encriptSHA256((String) params.get("password")));
			} catch (Exception e) {
				return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.INVALID_PARAMETER.getErrorcode() + "", CommonErrorCode.INVALID_PARAMETER.getGmessage() + " : " + CommonErrorCode.INVALID_PARAMETER.getDmessage());
			}
			params.put("password", password);
			params.put("grantType", "signup");
			if (((String) params.get("language")).toLowerCase().equals("en")) {
				params.put("language", "D001");
			} else {
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
				return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.INVALID_PARAMETER.getErrorcode() + "", CommonErrorCode.INVALID_PARAMETER.getGmessage() + " : " + CommonErrorCode.INVALID_PARAMETER.getDmessage());
			}
			headers.setContentType(MediaType.valueOf("application/json"));
			headers.setAccept(Collections.singletonList(MediaType.valueOf(request.getHeader("accept"))));
			HttpEntity<HashMap> req = new HttpEntity<>(params, headers);

			ResCommDTO response = RestTemplateUtils.postRequest(url, req);

			int cnt = 0;

			if (!response.getCode().equals("0")) {
				return CommonUtil.setResponseTrObject(response.getData(), cnt, response.getCode() + "", response.getMessage());
			} else {
				try {
					// 발급된 AngkorId 가져오기
					HashMap mapRes = (HashMap) response.getData();
					params.put("angkorId", mapRes.get("angkorId"));
					params.put("profileUrl", null);

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
						// insert (my system basic setting)
						dao.addSysVersion(params); // ank_sys_version
						dao.addSysNoti(params); // ank_sys_noti
						dao.addSysSecurity(params); // ank_sys_security
						dao.addSysFriends(params); // ank_sys_friends
						dao.addSysChatting(params); // ank_sys_chatting
						dao.addSysDisplay(params); // ank_sys_display

						Map<String, Map<String, Object>> settingData = new HashMap<>();
						HashMap<String, Object> chatting;
						chatting = loginDao.getChatting(params);
						HashMap<String, Object> display;
						display = loginDao.getDisplay(params);
						HashMap<String, Object> friend;
						friend = loginDao.getFriends(params);
						HashMap<String, Object> noti;
						noti = loginDao.getNoti(params);
						HashMap<String, Object> security;
						security = loginDao.getPrivacy(params);

						settingData.put("chatting", chatting);
						settingData.put("display", display);
						settingData.put("friend", friend);
						settingData.put("noti", noti);
						settingData.put("security", security);

						HashMap map = dao.getUser(params);

						// 3. sendbird 유저 정보 update
						updateInfoBySendbird((String) map.get("angkorId"), (String) map.get("userName"));

						HashMap commRes = (HashMap) response.getData();
						commRes.put("settingData", settingData);
						commRes.put("phoneCode", map.get("phonecode"));
						commRes.put("phoneNumber", map.get("phonenumber"));
						commRes.put("birthday_dt", map.get("birthday_dt"));
						commRes.put("gender", map.get("gender"));
						commRes.put("privacy_profile", map.get("privacy_profile"));
						commRes.put("email", map.get("email"));
						commRes.put("userName", map.get("userName"));
						commRes.put("profileUrl", map.get("profileUrl"));
						commRes.put("profileMessage", map.get("profileMessage"));
						commRes.put("uuid", uuidRes);

						signUpData.put(map.get("angkorId").toString(), commRes);
						resultCnt++;
					}
				} catch (Exception e) {
					HashMap mapRes = (HashMap) response.getData();
					signUpData.put(mapRes.get("angkorId").toString(), null);
				}
			}
		}
		return CommonUtil.setResponseTrObject(signUpData, resultCnt, CommonErrorCode.SUCCESS.getErrorcode() + "", CommonErrorCode.SUCCESS.getGmessage() + " : " + CommonErrorCode.SUCCESS.getDmessage());
	}

	public String uuid(HttpServletRequest request) {

		String url = GlobalObjects.getUnionUrl() + "/uuid";

		HttpHeaders headers = new HttpHeaders();
		headers.add("AppKey", GlobalObjects.getAppKey());
		headers.setAccept(Collections.singletonList(MediaType.valueOf(request.getHeader("accept"))));
		HttpEntity<String> req = new HttpEntity<String>("", headers);

		ResCommDTO response = RestTemplateUtils.getRequest(url, req);
		HashMap mapRes = (HashMap) response.getData();
		log.info("=====test====== new uuid >> " + mapRes.get("uuid"));
		return (String) mapRes.get("uuid");

	}

	public static void updateInfoBySendbird(String angkorId, String nickname) {

		HashMap params = new HashMap();
		params.put("nickname", nickname);

		String url = "https://api-" + GlobalObjects.getAppKey() +".sendbird.com/v3/users/" + angkorId;

		HttpHeaders headers = new HttpHeaders();
		headers.add("Api-Token", GlobalObjects.getAppToken());
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<HashMap> req = new HttpEntity<>(params, headers);
		ResCommDTO response = RestTemplateUtils.putRequestImageSendbird(url, req);

		HashMap map = (HashMap) response.getData();

		String result = (String) map.get("nickname");
		log.info("=====test====== update sendbird >> " + result);

	}*/
	

}
