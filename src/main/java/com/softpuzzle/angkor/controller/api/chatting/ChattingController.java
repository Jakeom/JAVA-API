
package com.softpuzzle.angkor.controller.api.chatting;

import com.softpuzzle.angkor.http.request.*;
import com.softpuzzle.angkor.http.response.common.ResCommDTO;
import com.softpuzzle.angkor.http.response.common.ResTrDTO;
import com.softpuzzle.angkor.service.api.chatting.ChattingService;
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

@Api(tags = {"채팅 관리"}, description = "Chatting")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/ankochat/api/chatting")
public class ChattingController {
	@Autowired
	private ChattingService service;

	@ApiOperation(value = "그룹 이모티콘 리스트", notes = "앙코르챗에서 제공하는 기본 이모티콘 그룹 전체 리스트를 가져온다.\n"+
													"(현재는 기본 이모티콘만 제공)")
	@GetMapping(value = "/getParentEmoticonList/{AngkorId}")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "AngkorId", required = true, dataType = "string", paramType = "path", value = "angkor id"),
			@ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
	})
	public ResCommDTO getParentEmoticonList(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq013 rdto) {

		log.info("request params : ________________ :" +rdto.toString());
		ResCommDTO res  = service.getParentEmoticonList(request, CommonUtil.Object2Hashmap(rdto));
		log.info("res data ________________ :" + res.getData());
		return res;
	}

	@ApiOperation(value = "선택한 그룹 이모티콘의 하위 리스트", notes = "선택한 그룹 이모티콘의 하위 24가지 이모티콘 리스트를 가져온다.\n"+
			"EmotiSeq는 생략 후 호출하면 제일 마지막에 생성된 이모티콘 하위 리스트를 불러온다.\n"+
			"(현재는 기본 이모티콘만 제공)")
	@GetMapping(value = "/getChildEmoticonList/{AngkorId}")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "AngkorId", required = true, dataType = "string", paramType = "path", value = "angkor id"),
			@ApiImplicitParam(name = "EmotiSeq", dataType = "string", paramType = "query", value = "selected emoticon seq"),
			@ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
	})
	public ResCommDTO getChildEmoticonList(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq013 rdto, @RequestParam(value = "EmotiSeq", required = false) String seq) {

		log.info("request params : ________________ :" +rdto.toString());
		ResCommDTO res  = service.getChildEmoticonList(request, CommonUtil.Object2Hashmap(rdto), seq);
		log.info("res data ________________ :" + res.getData());
		return res;
	}

	@ApiOperation(value = "유저의 공용키 조회 (리스트) ", notes = "유저의 각 공용키를 조회하여 리스트로 전달한다.")
	@GetMapping(value = "/getPublicKeyList/{AngkorId}")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "AngkorId", required = true, dataType = "string", paramType = "path", value = "angkor id"),
			@ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
	})
	public ResCommDTO getPublicKeyList(HttpServletRequest request, @PathVariable(name = "AngkorId") String angkorId, @RequestParam("userList") String[] userList) {

		ORGReq030 rdto = new ORGReq030();
		rdto.setAngkorId(angkorId);
		rdto.setUserList(userList);

		log.info("request params : ________________ :" +rdto);
		ResCommDTO res  = service.getPublicKeyList(request, CommonUtil.Object2Hashmap(rdto));
		log.info("res data ________________ :" + res.getData());
		return res;
	}

	@ApiOperation(value = "비밀 채팅 그룹 공용키 및 개인키 생성", notes = "비밀 그룹 채널 생성 시 그룹 공유키와 그룹 개인키를 생성하여 전달한다.")
	@PostMapping(value = "/setSecertKey")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
	})
	public ResTrDTO setSecertKey(HttpServletRequest request, @RequestBody ORGReq029 rdto) {

		log.info("request params : ________________ :" +rdto.toString());
		ResTrDTO res  = service.setSecertKey(request, CommonUtil.Object2Hashmap(rdto));
		log.info("res data ________________ :" + res.getData());
		return res;
	}

	@ApiOperation(value = "그룹 초대 가능 여부 확인", notes = "그룹에 초대하려는 사용자 중에서 그룹 초대가 가능한 사용자인지 체크한다.\n"+
															"1. friend_group : 1 => everybody\n" +
															"2. friend_group : 2 => My friends\n" +
															"3. friend_group : 3 => Nobody\n")
	@PostMapping(value = "/getGroupList/{AngkorId}")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
	})
	public ResCommDTO getGroupList(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq013 angkorId, @RequestBody ORGReq039 rdto) {

		rdto.setAngkorId(angkorId.getAngkorId());

		log.info("request params : ________________ :" +rdto.toString());
		ResCommDTO res  = service.getGroupList(request, CommonUtil.Object2Hashmap(rdto));
		log.info("res data ________________ :" + res.getData());
		return res;
	}


}