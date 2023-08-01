package com.softpuzzle.angkor.service.api.display;

import com.softpuzzle.angkor.database.mapper.api.chatting.ChattingMapper;
import com.softpuzzle.angkor.database.mapper.api.display.DisplayMapper;
import com.softpuzzle.angkor.http.response.common.ResCommDTO;
import com.softpuzzle.angkor.http.response.common.ResTrDTO;
import com.softpuzzle.angkor.service.api.cert.CertService;
import com.softpuzzle.angkor.utility.CommonErrorCode;
import com.softpuzzle.angkor.utility.CommonUtil;
import com.softpuzzle.angkor.utility.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DisplayService {
	@Autowired
	private CertService certService;
	private final DisplayMapper dao;

	public ResCommDTO getDisplay(HttpServletRequest request, HashMap params) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseObject(null, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		HashMap map;
		ResCommDTO res;

		try {
			map = dao.getDisplay(params);

			res = CommonUtil.setResponseObject(map,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
		} catch ( Exception e ) {
			res = CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode()+"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}

		return res;
	}

	public ResTrDTO setDisplay(HttpServletRequest request, HashMap params) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		ResTrDTO res;
		int cnt = 0;

		try {
			/**
			 * TODO
			 * DB 테이블상 존재하는 컬럼과 기획문서 매칭되지 않음
			 * BadRequest시 처리 방식 논의
			 */
			cnt = dao.updateDisplay(params);

			if ( cnt > 0 ) {
				res = CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
			} else {
				res = CommonUtil.setResponseTrObject(null, cnt, CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage() + " : " + CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
			}

		} catch ( Exception e ) {
			res = CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}

		return res;
	}

	public ResCommDTO getBackgroundColor(HttpServletRequest request, HashMap params) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseObject(null, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		ResCommDTO res;

		try {
			List<HashMap> list = dao.getBackgroundColor(params);
			res = CommonUtil.setResponseObject(list,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage(), 0,0, list.size());
		} catch ( Exception e ) {
			res = CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode()+"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}

		return res;
	}

	public ResTrDTO setBackgroundColor(HttpServletRequest request, HashMap params) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		if (params.get("backgroundSeq") == null) {
			return CommonUtil.setResponseTrObject(null, 0,CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		ResTrDTO res;
		int cnt = 0;

		try {
			cnt = dao.setBackgroundColor(params);
			if ( cnt > 0 ) {
				res = CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
			} else {
				res = CommonUtil.setResponseTrObject(null, cnt, CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage() + " : " + CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
			}
		} catch ( Exception e ) {
			res = CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}

		return res;
	}

	public ResTrDTO setFontSize(HttpServletRequest request, HashMap params) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		if (StringUtils.isBlank((String)params.get("inFontsize"))) {
			return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}else{
			if (!Arrays.asList("10", "12", "14", "16", "18", "20").contains(params.get("inFontsize"))) {
				return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.INVALID_PARAMETER.getErrorcode() + "", CommonErrorCode.INVALID_PARAMETER.getGmessage() + " : " + CommonErrorCode.INVALID_PARAMETER.getDmessage());
			}
		}


		ResTrDTO res;
		int cnt = 0;

		try {
			cnt = dao.setFontSize(params);
			if ( cnt > 0 ) {
				res = CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
			} else {
				res = CommonUtil.setResponseTrObject(null, cnt, CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage() + " : " + CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
			}
		} catch ( Exception e ) {
			res = CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}

		return res;
	}

}
