package com.softpuzzle.angkor.service.api.agree;

import com.softpuzzle.angkor.database.mapper.api.agree.AgreeMapper;
import com.softpuzzle.angkor.http.response.common.ResCommDTO;
import com.softpuzzle.angkor.utility.CommonErrorCode;
import com.softpuzzle.angkor.utility.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AgreeService {

	@Autowired
	private AgreeMapper dao;
	
	public ResCommDTO getAgreeList(HttpServletRequest request) {

		HashMap params = new HashMap<>();
		params.put("language", request.getHeader("Language"));

		if (StringUtils.isBlank((String)params.get("language"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		if (!(((String) params.get("language")).toLowerCase().equals("en") || ((String) params.get("language")).toLowerCase().equals("khr"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.INVALID_PARAMETER.getErrorcode() + "", CommonErrorCode.INVALID_PARAMETER.getGmessage() + " : " + CommonErrorCode.INVALID_PARAMETER.getDmessage());
		}

		try {
			List<HashMap> list = dao.getAgreeList(params);
			return CommonUtil.setResponseObject(list, CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}
}
