package com.softpuzzle.angkor.service.api.code;

import com.softpuzzle.angkor.database.mapper.api.code.CodeMapper;
import com.softpuzzle.angkor.http.response.common.ResCommDTO;
import com.softpuzzle.angkor.http.response.common.ResTrDTO;
import com.softpuzzle.angkor.utility.CommonErrorCode;
import com.softpuzzle.angkor.utility.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CodeService {

	@Autowired
	private CodeMapper dao;
	
	public ResCommDTO getCodeAllList(HashMap params) {
		try {
			List<HashMap> list = dao.getCodeAllList(params);
			return CommonUtil.setResponseObject(list,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResCommDTO getListByCode(HashMap params) {
		try {
			List<HashMap> list = dao.getListByCode(params);
			return CommonUtil.setResponseObject(list,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResCommDTO getInfoCode(HashMap params) {

		if (StringUtils.isBlank((String)params.get("code_name"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		try {
			List<HashMap> list= dao.getInfoByGroupChild(params);
			return CommonUtil.setResponseObject(list,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResTrDTO addGroupCode(HashMap params) {

		int cnt = 0;

		if (StringUtils.isBlank((String)params.get("group_code")) || StringUtils.isBlank((String)params.get("group_code_name_en")) || StringUtils.isBlank((String)params.get("group_code_name_khmr"))) {
			return CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}
		if (StringUtils.isBlank((String)params.get("remark")) || StringUtils.isBlank((String)params.get("create_id"))) {
			return CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}
		if (params.get("group_code_type")==null) {
			return CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		try {
			cnt = dao.addGroupCode(params);
			if(cnt>0){
				return CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
			}else{
				return CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.EXIST_GROUP_CODE_NAME.getErrorcode()+"",CommonErrorCode.EXIST_GROUP_CODE_NAME.getGmessage()+" : "+CommonErrorCode.EXIST_GROUP_CODE_NAME.getDmessage());
			}
		} catch (Exception e) {
			return CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResTrDTO deleteGroupCode(HashMap params) {

		int cnt = 0;

		if (StringUtils.isBlank((String)params.get("code"))) {
			return CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		try {
			cnt = dao.deleteGroupCode(params);
			if(cnt>0){
				return CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
			}else{
				return CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.EXIST_GROUP_CODE_NAME.getErrorcode()+"",CommonErrorCode.EXIST_GROUP_CODE_NAME.getGmessage()+" : "+CommonErrorCode.EXIST_GROUP_CODE_NAME.getDmessage());
			}
		} catch (Exception e) {
			return CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}
}
