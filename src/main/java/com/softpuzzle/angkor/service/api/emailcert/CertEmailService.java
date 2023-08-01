package com.softpuzzle.angkor.service.api.emailcert;

import com.softpuzzle.angkor.database.mapper.api.cert.CertMapper;
import com.softpuzzle.angkor.gcache.GlobalObjects;
import com.softpuzzle.angkor.http.response.common.ResCommDTO;
import com.softpuzzle.angkor.utility.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CertEmailService {

	@Autowired
	private CertMapper dao;
	@Autowired
	private MailUtil mailUtil;


	public ResCommDTO getAuthCode(HttpServletRequest request, HashMap params){

		if (StringUtils.isBlank((String)params.get("email"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		// 10회 오류 틀렸을 경우 24시간동안 인증 요청 금지
		try {
			int check = dao.chkEmail24Hours(params);
			if(check>0) {
				return CommonUtil.setResponseObject(null, CommonErrorCode.MAIL_24HOURS_FORBID.getErrorcode() + "", CommonErrorCode.MAIL_24HOURS_FORBID.getDmessage());
			}
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage() + " : " + CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}

		String url =  GlobalObjects.getUnionUrl() + "/mail/req";
		HttpHeaders headers = new HttpHeaders();
		headers.add("AppKey", GlobalObjects.getAppKey());
		headers.setContentType(MediaType.valueOf(request.getHeader("Content-Type")));
		headers.setAccept(Collections.singletonList(MediaType.valueOf(request.getHeader("accept"))));
		headers.setContentLanguage(Locale.forLanguageTag(request.getHeader("Language")));

		params.put("grantType", "authorization_code");
		HttpEntity<HashMap> req = new HttpEntity<>(params, headers);

		ResCommDTO response = RestTemplateUtils.postRequest(url, req);

		if (response.getCode().equals("0")) {
			try {
				// 1. 인증번호 메일 발송
				HashMap mapRes = (HashMap) response.getData();
				mailUtil.joinEmail(mapRes);

				params.put("comfirmWord", mapRes.get("certNumber"));
				params.put("confirmType", 1);

				// 3. ank_user_emailconfirm 에 hist 쌓기
				int isData = dao.getEmailHist(params);
				if(isData>0){
					dao.updateEmailHist(params);
				}else{
					String guid = UUID.randomUUID().toString();
					params.put("guid", guid);
					dao.insertEmailHist(params);
				}
			} catch (Exception e) {
				response = CommonUtil.setResponseObject(response.getData(), CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage() + " : " + CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
			}
		}

		return response;

	}

	public ResCommDTO isAuthCode(HttpServletRequest request, HashMap params) throws Exception {

		if (StringUtils.isBlank((String)params.get("email")) || StringUtils.isBlank((String)params.get("certNumber"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		// 3분 유효시간이 지났는지 먼저 확인
		try {
			int timeOver = dao.chkEmailTimeOver(params);
			if(timeOver==0) {
				if(request.getHeader("Language").equals("en")){
					return CommonUtil.setResponseObject(null, CommonErrorCode.MAIL_TIME_OVER.getErrorcode() + "", CommonErrorCode.MAIL_TIME_OVER.getGmessage());
				}else{
					return CommonUtil.setResponseObject(null, CommonErrorKhmerCode.MAIL_TIME_OVER.getErrorcode() + "", CommonErrorKhmerCode.MAIL_TIME_OVER.getGmessage());
				}
			}
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage() + " : " + CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}

		String url =  GlobalObjects.getUnionUrl() + "/mail/cert";

		HttpHeaders headers = new HttpHeaders();
		headers.add("AppKey", GlobalObjects.getAppKey());
		headers.setContentType(MediaType.valueOf(request.getHeader("Content-Type")));
		headers.setAccept(Collections.singletonList(MediaType.valueOf(request.getHeader("accept"))));
		headers.setContentLanguage(Locale.forLanguageTag(request.getHeader("Language")));

		params.put("grantType", "authorization_code");
		HttpEntity<HashMap> req = new HttpEntity<>(params, headers);

		ResCommDTO response = RestTemplateUtils.postRequest(url, req);

		params.put("confirmType", 1);

		if(!response.getCode().equals("0")){
			// 입력한 인증번호가 틀렸을 경우 ank_user_emailconfirm 에 try_cnt update
			if (response.getCode().equals("400603")) {

				params.put("angkorId", params.get("email"));

				dao.updateCntEmailHist(params);

				HashMap mapRes = new HashMap();
				mapRes.put("tryCnt", params.get("tryCnt"));

				// 10번 이상 틀렸을 경우 10회 입력 오류 처리
				if((Integer) params.get("tryCnt") > 9){
					return CommonUtil.setResponseObject(response.getData(),CommonErrorCode.MAIL_TRY_TEN_OVER.getErrorcode()+"", CommonErrorCode.MAIL_TRY_TEN_OVER.getDmessage());
				}
				return CommonUtil.setResponseObject(mapRes, response.getCode() + "", response.getMessage());
			}else{
				return response;
			}
		}else{
			params.put("angkorId", params.get("email"));
			dao.cleanCntEmailHist(params);

			return CommonUtil.setResponseObject(response.getData(),CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
		}
	}

}
