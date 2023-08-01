package com.softpuzzle.angkor.service.api.noti;

import com.softpuzzle.angkor.database.mapper.api.noti.NotiMapper;
import com.softpuzzle.angkor.database.mapper.api.version.VersionMapper;
import com.softpuzzle.angkor.http.response.common.ResCommDTO;
import com.softpuzzle.angkor.http.response.common.ResTrDTO;
import com.softpuzzle.angkor.service.api.cert.CertService;
import com.softpuzzle.angkor.utility.CommonErrorCode;
import com.softpuzzle.angkor.utility.CommonUtil;
import com.softpuzzle.angkor.utility.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotiService {
	@Autowired
	private CertService certService;
	private final NotiMapper dao;

	public ResCommDTO getNoti(HttpServletRequest request, HashMap params) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseObject(null, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		HashMap map;
		ResCommDTO res;

		try {
			map = dao.getNoti(params);

			res = CommonUtil.setResponseObject(map,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
		} catch ( Exception e ) {
			res = CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode()+"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}

		return res;
	}

	public ResTrDTO setNoti(HttpServletRequest request, HashMap params) {

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
			cnt = dao.updateNoti(params);

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
