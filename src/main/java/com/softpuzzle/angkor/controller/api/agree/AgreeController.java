
package com.softpuzzle.angkor.controller.api.agree;

import com.softpuzzle.angkor.http.request.ORGReq004;
import com.softpuzzle.angkor.http.request.ORGReq013;
import com.softpuzzle.angkor.http.response.common.ResCommDTO;
import com.softpuzzle.angkor.service.api.agree.AgreeService;
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

@Api(tags = {"약관동의"}, description = "Agree")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/ankochat/api/agree")
public class AgreeController {
	@Autowired
	private AgreeService service;

	@ApiOperation(value = "사용자 약관동의 - 언어 선택", notes = "초기 화면에서의 언어 선택 시 해당 언어에 맞는 약관 동의 리스트를 불러온다.\n" +
															"language를 en으로 설정하면 영어로, khr로 설정하면 크메르어로 번역된다.\n" +
															"ord = 화면상의 약관 순서, language_type = en(영어) / khr(크메로어), agree_term = 약관 내용, agree_type = 1(필수약관) / 2(선택약관)")
	@GetMapping(value = "/agree")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Language", required = true, dataType = "string", paramType = "header", value = "selected language")
	})
	@ResponseBody
	public ResCommDTO agree(HttpServletRequest request) {

		log.info("request params : ________________ :" +request.getHeader("Language").toString());
		ResCommDTO res  = service.getAgreeList(request);
		log.info("res data ________________ :" + res.getData());
		return res;
	}

}