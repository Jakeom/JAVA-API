package com.softpuzzle.angkor.service.api.version;

import com.softpuzzle.angkor.database.mapper.api.version.VersionMapper;
import com.softpuzzle.angkor.gcache.GlobalObjects;
import com.softpuzzle.angkor.http.response.common.ResCommDTO;
import com.softpuzzle.angkor.http.response.common.ResTrDTO;
import com.softpuzzle.angkor.service.api.cert.CertService;
import com.softpuzzle.angkor.utility.*;
import io.swagger.models.auth.In;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VersionService {
	@Autowired
	private CertService certService;
	@Autowired
	private VersionMapper dao;

	public ResCommDTO getVersion(HttpServletRequest request, HashMap params) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseObject(null, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		HashMap map;
		ResCommDTO res;

		try {
			map = dao.getVersion(params);

			res = CommonUtil.setResponseObject(map,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
		} catch ( Exception e ) {
			res = CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode()+"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}

		return res;
	}

	public ResTrDTO setVersion(HttpServletRequest request, HashMap params) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		if (!ValidatorUtil.validateUUID(request.getHeader("Uuid"))){
			return CommonUtil.setResponseTrObject(null, 0,CommonErrorCode.HEADER_NOT_VALID_UUID.getErrorcode() + "", CommonErrorCode.HEADER_NOT_VALID_UUID.getGmessage() + " : " + CommonErrorCode.HEADER_NOT_VALID_UUID.getDmessage());
		}

		ResTrDTO res;
		int cnt = 0;

		try {
			HashMap map = dao.getRecentAppVersion(params);
			params.put("new_version", map.get("app_version"));
			cnt = dao.updateVersion(params);

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

	public ResCommDTO getOsVersion(HttpServletRequest request, HashMap params) {


		if (StringUtils.isBlank((String)params.get("osType"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		if (!(((String) params.get("osType")).toUpperCase().equals("ANDROID") || ((String) params.get("osType")).toUpperCase().equals("IOS"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.INVALID_PARAMETER.getErrorcode() + "", CommonErrorCode.INVALID_PARAMETER.getGmessage() + " : " + CommonErrorCode.INVALID_PARAMETER.getDmessage());
		}

		try {
			HashMap map = dao.getRecentAppVersion(params);
			return CommonUtil.setResponseObject(map,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
		} catch ( Exception e ) {
			return CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResCommDTO checkAppUpdate(HttpServletRequest request, String version) {


		if (StringUtils.isBlank(version)) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		if (!(request.getHeader("OS").toLowerCase().equals("android") || request.getHeader("OS").toLowerCase().equals("ios"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.INVALID_PARAMETER.getErrorcode() + "", CommonErrorCode.INVALID_PARAMETER.getGmessage() + " : " + CommonErrorCode.INVALID_PARAMETER.getDmessage());
		}

		try {
			String osType = request.getHeader("OS").toLowerCase();
			HashMap params = new HashMap();
			params.put("osType", osType);

			String recent_version = dao.checkAppUpdate(params);
			String current_version = version;

			String[] recentParts = recent_version.split("\\.");
			String[] currentParts = current_version.split("\\.");

			int rMajor = Integer.parseInt(recentParts[0]);
			int rMinor = Integer.parseInt(recentParts[1]);

			int cMajor = Integer.parseInt(currentParts[0]);
			int cMinor = Integer.parseInt(currentParts[1]);

			HashMap resMap = new HashMap<>();
			Boolean appUpdateYn = false;

			if(cMajor < rMajor || (cMajor==rMajor && cMinor < rMinor)){
				appUpdateYn = true;
			}

			log.info("os : {} , current : {} , recent : {}, updateYn : {}", osType,version,recent_version,appUpdateYn);

			resMap.put("app_update", appUpdateYn);

			return CommonUtil.setResponseObject(resMap,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
		} catch ( Exception e ) {
			return CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}
}
