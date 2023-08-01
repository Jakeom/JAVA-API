
package com.softpuzzle.angkor.controller.api.smscert;

import com.softpuzzle.angkor.http.request.ORGReq005;
import com.softpuzzle.angkor.http.request.ORGReq008;
import com.softpuzzle.angkor.http.request.ORGReq009;
import com.softpuzzle.angkor.http.request.ORGReq047;
import com.softpuzzle.angkor.http.response.common.ResCommDTO;
import com.softpuzzle.angkor.service.api.smscert.CertSmsService;
import com.softpuzzle.angkor.utility.CommonUtil;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.Authorization;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.header.Header;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = {"SMS 인증"}, description = "SMS")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/ankochat/api/smscert")
public class CertSmsController {

	@Autowired
	private CertSmsService service;

	@ApiOperation(value = "SMS 인증코드 요청", notes = "입력한 전화번호로 6자리의 인증번호를 전달한다. 하루 호출 5번으로 제한하고, 00시에 호출횟수가 초기화된다.")
	@PostMapping(value = "/req")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Language", required = true, dataType = "string", paramType = "header", value = "selected language")
	})
	@ResponseBody
	public ResCommDTO getAuthCode(HttpServletRequest request, @RequestBody ORGReq008 rdto) {

		log.info("request params : ________________ :" +rdto.toString());
		ResCommDTO res = service.getAuthCode(request, CommonUtil.Object2Hashmap(rdto));
		log.info("resData list ________________ :" + res.getData());
		return res;
	}

	@ApiOperation(value = "SMS 인증코드 확인", notes = "인증번호를 확인한다.\n" +
													"\n" +
													"인증번호 입력은 인증번호 발급 후 3분 이내에 완료해야한다.\n" +
													"code값이 400603인 경우 error_count는 인증번호를 잘못입력한 횟수이다.\n" +
													"인증번호 10회 잘못입력시 인증번호를 다시 발급 받아야한다.\n" +
													"\n" +
													"SMS인증번호 확인에서 GrantType은 signup을 사용한다.\n" +
													"예외로 전화번호 변경 시 추가로 GrantType은 update로 설정한다.\n"+
													"\n" +
													"회원가입에서의 SMS인증은 회원가입 API를 호출하기전 수행완료 해야한다.\n" +
													"로그인을 하지않고 SMS인증만 받을 때 사용한다.")
	@PostMapping(value = "/cert")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Language", required = true, dataType = "string", paramType = "header", value = "selected language"),
			@ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header", value = "authKey (lower case letters), bearer {authKey}")
	})
	public ResCommDTO isAuthCode(HttpServletRequest request, @RequestBody ORGReq009 rdto) throws Exception {

		log.info("request params : ________________ :" +rdto.toString());
		ResCommDTO res = service.isAuthCode(request, CommonUtil.Object2Hashmap(rdto));
		log.info("resData list ________________ :" + res.getData());
		return res;
	}

	@ApiOperation(value = "SMS 인증코드 확인 및 로그인", notes = "인증번호를 확인 및 로그인한다.\n" +
			"\n" +
			"새로 추가된 API에서는 전화번호가 변경된 상태에서 SMS인증을 받는 경우 UserId에 해당하는 사용자의 전화번호도 자동으로 변경해 줍니다.\n"+
			"\n" +
			"인증번호 입력은 인증번호 발급 후 3분 이내에 완료해야한다.\n" +
			"code값이 400603인 경우 error_count는 인증번호를 잘못입력한 횟수이다.\n" +
			"인증번호 10회 잘못입력시 인증번호를 다시 발급 받아야한다.\n" +
			"\n" +
			"SMS인증번호 확인에서 GrantType은 로그인에서 signin, 회원가입 signup을 사용한다.\n" +
			"예외로 전화번호 변경 시 추가로 GrantType은 update로 설정한다.\n"+
			"\n" +
			"회원가입에서의 SMS인증은 회원가입 API를 호출하기전 수행완료 해야한다.\n" +
			"로그인에서의 SMS 인증은 로그인 API를 호출 후 400417오류를 리턴받으면 SMS인증을 진행한다.")
	@PostMapping(value = "/certLogin/{UserId}")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Language", required = true, dataType = "string", paramType = "header", value = "selected language"),
			@ApiImplicitParam(name = "UserId", required = true, dataType = "string", paramType = "path", value = "user id"),
			@ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header", value = "authKey (lower case letters), bearer {authKey}")
	})
	public ResCommDTO isAuthCodeToLogin(HttpServletRequest request, @PathVariable(name = "UserId") String userId,  @RequestBody ORGReq005 rdto) throws Exception {

		rdto.setUserId(userId);

		log.info("request params : ________________ :" +rdto.toString());
		ResCommDTO res = service.isAuthCodeToLogin(request, CommonUtil.Object2Hashmap(rdto));
		log.info("resData list ________________ :" + res.getData());
		return res;
	}
}