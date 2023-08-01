
package com.softpuzzle.angkor.controller.api.call;

import com.softpuzzle.angkor.http.request.*;
import com.softpuzzle.angkor.http.response.common.ResCommDTO;
import com.softpuzzle.angkor.service.api.call.CallService;
import com.softpuzzle.angkor.utility.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;

@Api(tags = {"call 관리"}, description = "Call")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/ankochat/api/call")
public class CallController {
	@Autowired
	private CallService service;
	@ApiOperation(value = "수신할 상대방의 정보 확인", notes = "발신 시 전화 수신할 상대방의 정보를 확인한다. \n" +
															"1. friend_calls : 1 => everybody\n" +
															"2. friend_calls : 2 => My friends\n" +
															"3. friend_calls : 3 => Nobody\n")
	@GetMapping(value = "/getFriendInfo/{AngkorId}")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "AngkorId", required = true, dataType = "string", paramType = "path", value = "angkor id"),
			@ApiImplicitParam(name = "Friend_AngkorId", dataType = "string", paramType = "query", value = "friend angkor id"),
			@ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
	})
	public ResCommDTO getFriendInfo(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq013 rdto, @RequestParam(value = "Friend_AngkorId") String friendAngkorId) {

		log.info("request params : ________________ :" +rdto.toString());
		ResCommDTO res  = service.getFriendInfo(request, CommonUtil.Object2Hashmap(rdto), friendAngkorId);
		log.info("res data ________________ :" + res.getData());

		HashMap mapRes = (HashMap) res.getData();
		if(mapRes!=null) res.setTotal(1);
		return res;
	}

	@ApiOperation(value = "Call 목록 삭제", notes = "Call 목록에서 삭제한 Call_id 를 등록합니다.")
	@GetMapping(value = "/setBlockCallLog/{AngkorId}")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "AngkorId", required = true, dataType = "string", paramType = "path", value = "angkor id"),
			@ApiImplicitParam(name = "CallId", dataType = "string", paramType = "query", value = "friend angkor id"),
			@ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
	})
	public ResCommDTO setBlockCallLog(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq013 rdto, @RequestParam(value = "CallId") String callId) {

		log.info("request params : ________________ :" +rdto.toString());
		ResCommDTO res  = service.setBlockCallLog(request, CommonUtil.Object2Hashmap(rdto), callId);
		log.info("res data ________________ :" + res.getData());

		return res;
	}
	
	@ApiOperation(value = "Call 목록 추가", notes = "Call 목록 추가 등록합니다.")
	@PostMapping(value = "/addBlockCallLog/{AngkorId}")
	@ResponseBody
	@ApiImplicitParams({
		@ApiImplicitParam(name = "AngkorId", required = true, dataType = "string", paramType = "path", value = "angkor id"),
		@ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
	})
	public ResCommDTO addBlockCallLog(HttpServletRequest request, @RequestBody ORGReq062 rdto, @PathVariable(name = "AngkorId") String AngkorId) {

		log.info("request params : ________________ :" +rdto.toString());
		rdto.setAngkorId(AngkorId);
		ResCommDTO res  = service.addBlockCallLog(request, CommonUtil.Object2Hashmap(rdto));
		log.info("res data ________________ :" + res.getData());

		return res;
	}

	@ApiOperation(value = "Call 목록 출력", notes = "Call 목록에서 삭제한 Call_id 를 등록합니다.")
	@GetMapping(value = "/getCallLog/{AngkorId}")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "AngkorId", required = true, dataType = "string", paramType = "path", value = "angkor id"),
			@ApiImplicitParam(name = "CreateAt", required = false, dataType = "string", paramType = "query", value = "call Create timestamp")
	})
	public ResCommDTO getCallLog(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq013 rdto) {

		log.info("request params : ________________ :" +rdto.toString());
		ResCommDTO res  = service.getCallLog(request, CommonUtil.Object2Hashmap(rdto));
		log.info("res data ________________ :" + res.getData());

		return res;
	}

	@ApiOperation(value = "Call Log Hook", notes = "1.Call Log Hook을 받는다. \n" 
												+  "2.sendbird api를 다시 요청하여 상세 정보는 받는다. \n"
												+  "3.받은 정보를 DB에 저장한다.")
	@PostMapping(value = "/callLog")
	@ResponseBody
	public ResCommDTO callLog(HttpServletRequest request, @RequestBody String str) throws Exception {
		HashMap tmp = new HashMap<String,Object>();
		
		service.callHookProcess(str);
		log.info("callLog:::::::::::::::res data ________________ :" + str);
		return new ResCommDTO();
	}
	
	
	@ApiOperation(value = "전체 Call log 목록 만들기", notes = "전체 Call log 목록 만들기")
	@GetMapping(value = "/makeCallLog")
	@ResponseBody
	@ApiImplicitParams({
			
	})
	public ResCommDTO makeCallLog(HttpServletRequest request) {

		service.makeCallLog(null);

		return new ResCommDTO();
	}
}