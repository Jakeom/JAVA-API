package com.softpuzzle.angkor.service.api.call;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.softpuzzle.angkor.database.mapper.api.call.CallMapper;
import com.softpuzzle.angkor.http.response.common.ResCommDTO;
import com.softpuzzle.angkor.service.api.cert.CertService;
import com.softpuzzle.angkor.utility.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CallService {
	@Autowired
	private CertService certService;
	@Autowired
	private CallMapper dao;

	public ResCommDTO getFriendInfo(HttpServletRequest request, HashMap params, String friendAngkorId) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseObject(null, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		if (StringUtils.isBlank((String)params.get("angkorId")) || StringUtils.isBlank(friendAngkorId)) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		try {
			// 1. 유효한 아이디인지 확인
			int isUser = dao.checkMyAngKorId(params);
			if(isUser>0) {
				// 2. 수신자의 정보 (개인설정 - Call / 친구여부 / 차단여부)
				params.put("friendAngkorId", friendAngkorId);
				HashMap map = dao.getFriendInfo(params);
				return CommonUtil.setResponseObject(map,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
			}else{
				return CommonUtil.setResponseObject(null,CommonErrorCode.INVALID_USER.getErrorcode()+"",CommonErrorCode.INVALID_USER.getGmessage()+" : "+CommonErrorCode.INVALID_USER.getDmessage());
			}
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}
	
	public ResCommDTO addBlockCallLog(HttpServletRequest request, HashMap params) {
		
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseObject(null, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}
		
		try {
			
			params.put("call_id", UUID.randomUUID().toString());
			params.put("callee_block_yn", "Y");
			
			int cnt = dao.insertCallLogExtra(params);
			return CommonUtil.setResponseObject(cnt,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());	
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
		
	}


	public ResCommDTO setBlockCallLog(HttpServletRequest request, HashMap params, String callId) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseObject(null, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		if (StringUtils.isBlank((String)params.get("angkorId")) || StringUtils.isBlank(callId)) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		try {
			// 1. 유효한 아이디인지 확인
			int isUser = dao.checkMyAngKorId(params);
			if(isUser>0) {
				// 2. 수신자의 정보 (개인설정 - Call / 친구여부 / 차단여부)
				params.put("callId", callId);
				dao.insertBlockCallLog(params);
				return CommonUtil.setResponseObject(null,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
			}else{
				return CommonUtil.setResponseObject(null,CommonErrorCode.INVALID_USER.getErrorcode()+"",CommonErrorCode.INVALID_USER.getGmessage()+" : "+CommonErrorCode.INVALID_USER.getDmessage());
			}
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResCommDTO getCallLog(HttpServletRequest request, HashMap params) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseObject(null, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		if (StringUtils.isBlank((String)params.get("angkorId"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}
		
		try {
			// 1. 유효한 아이디인지 확인
			int isUser = dao.checkMyAngKorId(params);
			if(isUser>0) {
				params.put("createdAt", request.getParameter("CreatedAt"));
				List<Map<String, Object>>  list = new ArrayList<>();
				for(HashMap<String, Object> map : dao.getCallLog(params)){
					String str = (String)map.get("call_log");
					JSONObject jsonObject = new JSONObject(StringUtil.convertToPascalCase(str));
					jsonObject.put("remoteUserDelYn", ((int)map.get("callee_del_yn")==0?false:true));
					list.add(jsonObject.toMap());
				}
				//역순 만들기 
//				Collections.reverse(list);
				
				return CommonUtil.setResponseObject(list,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
			}else{
				return CommonUtil.setResponseObject(null,CommonErrorCode.INVALID_USER.getErrorcode()+"",CommonErrorCode.INVALID_USER.getGmessage()+" : "+CommonErrorCode.INVALID_USER.getDmessage());
			}
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public void callHookProcess(String bodyString){

		// Call hook 받은 정보 취득
		JSONObject jsonObject = new JSONObject(bodyString);
		JSONObject direct_call = (JSONObject)jsonObject.getJSONObject("direct_call");
		
		// Sendbird에서 call 상세 정보 취득
		HashMap<String, Object> params = new  HashMap<String, Object>();		
		params.put("call_id", direct_call.getString("call_id"));
		HashMap dCallLog = SendbirdUtils.getDirectCallsLogByCallId(params);
		
		params.put("caller_id", direct_call.get("caller_id"));
		params.put("callee_id", direct_call.get("callee_id"));
		
		
		
		params.put("call_log", new JSONObject(dCallLog).toString());
		params.put("created_at", direct_call.getBigInteger("created_at"));
		params.put("started_at", dCallLog.get("started_at"));
		params.put("ended_at", dCallLog.get("ended_at"));
		
		try {
			
			String isBlock = (dao.checkCallBlock(params)>0?"Y":"N");
			params.put("callee_block_yn", isBlock);
			
			dao.insertCallLog(params);
		}catch(Exception e){
			log.error(e.getLocalizedMessage());
		}
	}
	
	public void makeCallLog(String next) {
		
//        param += "next="+StringUtil.nTs((String) params.get("next"));
//        param += "&limit="+StringUtil.nTs((String) params.get("limit"));
//        param += "&call_ts="+StringUtil.nTs((String) params.get("call_ts"));
//        param += "&search_scope_from_call_ts="+StringUtil.nTs((String) params.get("search_scope_from_call_ts"));
//        param += "&end_result="+StringUtil.nTs((String) params.get("end_result"));
//        param += "&duration_range_min="+StringUtil.nTs((String) params.get("duration_range_min"));
//        param += "&duration_range_max="+StringUtil.nTs((String) params.get("duration_range_max"));
        HashMap sendbirdParams = new HashMap<String, Object>();
        
        sendbirdParams.put("limit","100");
        sendbirdParams.put("next",next);
        sendbirdParams.put("end_result","completed,canceled");
        
		HashMap dCallLog = SendbirdUtils.getDirectCallsLogByParams(sendbirdParams);
		
		for(HashMap<String,Object> map:(ArrayList<HashMap>)dCallLog.get("calls")) {
			
			HashMap<String, Object> params = new  HashMap<String, Object>();	
			params.put("call_id", map.get("call_id"));
			params.put("call_log", new JSONObject(map).toString());
			params.put("created_at", map.get("started_at"));
			params.put("started_at", map.get("started_at"));
			params.put("ended_at", map.get("ended_at"));

			for(HashMap<String,Object> p :  (ArrayList<HashMap>)map.get("participants")) {
				if("dc_caller".equals((String)p.get("role"))) { // caller
					params.put("caller_id", p.get("user_id"));
				}else { // callee
					params.put("callee_id", p.get("user_id"));
				}
			}
			
			try {
				dao.insertCallLog(params);
			}catch(Exception e){
				log.error(e.getLocalizedMessage());
			}
		}
		
		if((boolean) dCallLog.get("has_next")) {
			makeCallLog((String)dCallLog.get("next"));
		}
		
	}

}
