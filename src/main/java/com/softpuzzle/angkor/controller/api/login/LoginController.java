
package com.softpuzzle.angkor.controller.api.login;

import com.softpuzzle.angkor.http.request.ORGReq010;
import com.softpuzzle.angkor.http.request.ORGReq011;
import com.softpuzzle.angkor.http.response.common.ResCommDTO;
import com.softpuzzle.angkor.service.api.login.LoginService;
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

@Api(tags = {"로그인"}, description = "Login")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/ankochat/api/login")
public class LoginController {

	@Autowired
	private LoginService service;

	@ApiOperation(value = "사용자 로그인", notes = "앙코르챗 사용자 로그인을 시도한다.\n" +
												"grant type을 password로 설정\n" +
												"[로그인 프로세스]\n" +
												"\n" +
												"전화번호, 아이디, 비밀번호 입력 => 200 리턴 => 로그인 완료\n" +
												"전화번호, 아이디, 비밀번호 입력 => 400417 오류 리턴 (등록된 기기가 아님) => SMS인증 요청 => SMS인증번호 확인 => 로그인 완료")
	@PostMapping(value = "/signin")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Language", required = true, dataType = "string", paramType = "header", value = "selected language")
	})
	@ResponseBody
	public ResCommDTO signIn(HttpServletRequest request, @RequestBody ORGReq010 rdto) throws Exception {

		log.info("request params : ________________ :" +rdto.toString());
		ResCommDTO res  = service.signIn(request, CommonUtil.Object2Hashmap(rdto));
		log.info("res data ________________ :" + res.getData());
		return res;
	}

	@ApiOperation(value = "사용자 로그아웃", notes = "앙코르챗 사용자 로그아웃한다.\n" +
											      "authKey는 소문자로 처리")
	@PostMapping(value = "/signout/{AngkorId}")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Language", required = true, dataType = "string", paramType = "header", value = "selected language"),
			@ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header", value = "authKey (lower case letters), bearer {authKey}"),
			@ApiImplicitParam(name = "AngkorId", required = true, dataType = "string", paramType = "path", value = "angkor id")
	})
	public ResCommDTO signout(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq011 rdto) {

		log.info("request params : ________________ :" +rdto.toString());
		ResCommDTO res  = service.signOut(request, CommonUtil.Object2Hashmap(rdto));
		log.info("res data ________________ :" + res.getData());
		return res;
	}

}