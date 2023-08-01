
package com.softpuzzle.angkor.controller.api.emailcert;

import com.softpuzzle.angkor.http.request.ORGReq006;
import com.softpuzzle.angkor.http.request.ORGReq007;
import com.softpuzzle.angkor.http.response.common.ResCommDTO;
import com.softpuzzle.angkor.service.api.emailcert.CertEmailService;
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

@Api(tags = {"이메일 인증"}, description = "Email")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/ankochat/api/emailcert")
public class CertEmailController {

	@Autowired
	private CertEmailService service;

	@ApiOperation(value = "메일 인증코드 요청", notes = "메일로 인증 코드 6자리를 요청하고, 해당 메일로 인증코드가 발송된다.\n" +
													 "이전 메일 인증코드 입력 10회 오류 시 24시간동안 발송이 제한된다. 관련 errorCode는 1010")
	@PostMapping(value = "/req")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Language", required = true, dataType = "string", paramType = "header", value = "selected language")
	})
	@ResponseBody
	public ResCommDTO getAuthCode(HttpServletRequest request, @RequestBody ORGReq006 rdto) {

		log.info("request params : ________________ :" +rdto.toString());
		ResCommDTO res = service.getAuthCode(request, CommonUtil.Object2Hashmap(rdto));
		log.info("resData list ________________ :" + res.getData());
		return res;
	}

	@ApiOperation(value = "메일 인증코드 확인", notes = "메일로 발송된 인증코드 6자리와 입력한 코드가 일치하는지 확인한다. 최대 10회 입력가능하며, 코드 입력이 틀렸을 경우 tryCnt로 실패 횟수를 확인할 수 있다.\n"+
														"인증코드 발송 후 3분 유효시간이 지날 경우의 errorCode는 1009")
	@PostMapping(value = "/cert")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Language", required = true, dataType = "string", paramType = "header", value = "selected language")
	})
	@ResponseBody
	public ResCommDTO isAuthCode(HttpServletRequest request, @RequestBody ORGReq007 rdto) throws Exception {

		log.info("request params : ________________ :" +rdto.toString());
		ResCommDTO res = service.isAuthCode(request, CommonUtil.Object2Hashmap(rdto));
		log.info("resData list ________________ :" + res.getData());
		return res;
	}

}