package com.softpuzzle.angkor.service.api.cert;

import com.softpuzzle.angkor.database.mapper.api.agree.AgreeMapper;
import com.softpuzzle.angkor.database.mapper.api.cert.CertMapper;
import com.softpuzzle.angkor.gcache.GlobalObjects;
import com.softpuzzle.angkor.http.response.common.ResCommDTO;
import com.softpuzzle.angkor.http.response.common.ResTrDTO;
import com.softpuzzle.angkor.utility.CommonErrorCode;
import com.softpuzzle.angkor.utility.CommonUtil;
import com.softpuzzle.angkor.utility.RestTemplateUtils;
import com.softpuzzle.angkor.utility.ValidatorUtil;
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
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class CertService {
	
	@Autowired
	private CertMapper dao;

	public ResCommDTO getUuid(HttpServletRequest request) {

		String url = GlobalObjects.getUnionUrl() + "/uuid";

		HttpHeaders headers = new HttpHeaders();
		headers.add("AppKey", GlobalObjects.getAppKey());
		headers.setAccept(Collections.singletonList(MediaType.valueOf(request.getHeader("accept"))));
		headers.setContentLanguage(Locale.forLanguageTag("en"));
		HttpEntity<String> req = new HttpEntity<String>("", headers);

		ResCommDTO response = RestTemplateUtils.getRequest(url, req);

		return CommonUtil.setResponseObject(response.getData(), response.getCode() + "", response.getMessage());

	}

	public ResTrDTO updateUuid(HttpServletRequest request, HashMap params) {

		int cnt=0;

		if (StringUtils.isBlank((String)params.get("angkorId"))) {
			return CommonUtil.setResponseTrObject(null, 0,CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		if (StringUtils.isBlank(request.getHeader("Uuid"))) {
			return CommonUtil.setResponseTrObject(null, 0,CommonErrorCode.HEADER_NOT_FOUND.getErrorcode() + "", CommonErrorCode.HEADER_NOT_FOUND.getGmessage() + " : " + CommonErrorCode.HEADER_NOT_FOUND.getDmessage());
		}

		if (!ValidatorUtil.validateUUID(request.getHeader("Uuid"))){
			return CommonUtil.setResponseTrObject(null, 0,CommonErrorCode.HEADER_NOT_VALID_UUID.getErrorcode() + "", CommonErrorCode.HEADER_NOT_VALID_UUID.getGmessage() + " : " + CommonErrorCode.HEADER_NOT_VALID_UUID.getDmessage());
		}

		try {
			// 1. 유효한 아이디인지 확인
			params.put("uuid", request.getHeader("Uuid"));
			int isUser = dao.checkMyAngKorId(params);
			if(isUser>0) {
				cnt = dao.updateUuid(params);
				return CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
			}else{
				return CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.INVALID_USER.getErrorcode()+"",CommonErrorCode.INVALID_USER.getGmessage()+" : "+CommonErrorCode.INVALID_USER.getDmessage());
			}
		} catch (Exception e) {
			return CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResCommDTO cert(HttpServletRequest request, HashMap params) {

		if (StringUtils.isBlank((String) params.get("angkorId"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		String url = GlobalObjects.getUnionUrl() + "/cert/" + params.get("angkorId");;

		HttpHeaders headers = new HttpHeaders();
		headers.add("AppKey", GlobalObjects.getAppKey());
		headers.add("Authorization", request.getHeader("Authorization").toLowerCase());
		headers.setContentType(MediaType.valueOf(request.getHeader("Content-Type")));
		headers.setAccept(Collections.singletonList(MediaType.valueOf(request.getHeader("accept"))));
		headers.setContentLanguage(Locale.forLanguageTag(request.getHeader("Language")));

		params.put("grantType", "session");

		HttpEntity<HashMap> req = new HttpEntity<>(params, headers);

		ResCommDTO response = RestTemplateUtils.postRequest(url, req);

		if (!response.getCode().equals("0")) {
			return CommonUtil.setResponseObject(response.getData(), response.getCode() + "", response.getMessage());
		} else {
			try {
				dao.updateLoginDt(params);
				return CommonUtil.setResponseObject(response.getData(), CommonErrorCode.SUCCESS.getErrorcode() + "", CommonErrorCode.SUCCESS.getGmessage() + " : " + CommonErrorCode.SUCCESS.getDmessage());
			} catch (Exception e) {
				return CommonUtil.setResponseObject(response.getData(), CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage() + " : " + CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
			}
		}
	}

	public String validUuid(String uuid, HashMap params){

		String result = null;

		if(StringUtils.isBlank(uuid)){
			// header uuid null check
			result = "HEADER_NOT_FOUND";
		}else{
			// header uuid-angkorId valid check
			HashMap vMap = new HashMap<>();
			vMap.put("uuid", uuid);
			vMap.put("angkorId", params.get("angkorId"));
			try {
				if (!ValidatorUtil.validateUUID(uuid)){
					result = "HEADER_NOT_VALID_UUID";
				}
				int check = dao.isValidUuid(vMap);
				if(check==0){
					result = "HEADER_NOT_VALID_UUID";
				}
			} catch (Exception e) {
				result = "INTERNAL_SERVER_ERROR";
			}
		}
		return result;
	}
}
