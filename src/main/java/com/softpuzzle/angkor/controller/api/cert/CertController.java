
package com.softpuzzle.angkor.controller.api.cert;

import com.softpuzzle.angkor.http.request.ORGReq013;
import com.softpuzzle.angkor.http.response.common.ResCommDTO;
import com.softpuzzle.angkor.http.response.common.ResTrDTO;
import com.softpuzzle.angkor.service.api.agree.AgreeService;
import com.softpuzzle.angkor.service.api.cert.CertService;
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

@Api(tags = {"기본인증"}, description = "Cert")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/ankochat/api/cert")
public class CertController {
	@Autowired
	private CertService service;

	@ApiOperation(value = "UUID 요청", notes = "통합 인증 서버로 UUID 를 요청한다. <p style='color:red; font-weight:bold;'>  UUID는 핸드폰당 하나의 ID만 발급을 원칙으로 한다.</p>")
	@PostMapping(value = "/uuid")
	@ResponseBody
	public ResCommDTO getUuid(HttpServletRequest request) {

		ResCommDTO res = service.getUuid(request);
		log.info("resData list ________________ :" + res.getData());
		return res;
	}

	@ApiOperation(value = "UUID 변경", notes = "타기기에서 로그인 시 사용중인 angkorId의 UUID를 변경한다. (서버)\n"+
												"header에 Uuid로 uuid를 추가한다.")
	@PostMapping(value = "/uuid/{AngkorId}")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid"),
			@ApiImplicitParam(name = "AngkorId", required = true, dataType = "string", paramType = "path", value = "angkor id")
	})
	public ResTrDTO updateUuid(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq013 rdto) {

		log.info("request params : ________________ :" +rdto.toString());
		ResTrDTO res = service.updateUuid(request, CommonUtil.Object2Hashmap(rdto));
		log.info("res count ________________ :" + res.getTrcnt());
		return res;
	}

	@ApiOperation(value = "기본 사용자 인증", notes = "앱사용중 사용자 인증이 필요할 때 사용\n" +
													"앱사용자의 마지막 접속 시간 관리\n" +
													"ex: 앱 상태가 background에서 foreground로 전환될 때, foreground상태인 경우 1분마다 호출")
	@PostMapping(value = "/cert/{AngkorId}")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header", value = "authKey (lower case letters), bearer {authKey}"),
			@ApiImplicitParam(name = "Language", required = true, dataType = "string", paramType = "header", value = "selected language"),
			@ApiImplicitParam(name = "AngkorId", required = true, dataType = "string", paramType = "path", value = "angkor id")
	})
	public ResCommDTO cert(HttpServletRequest request, @RequestBody ORGReq013 rdto) {

		log.info("request params : ________________ :" +rdto.toString());
		ResCommDTO res  = service.cert(request, CommonUtil.Object2Hashmap(rdto));
		log.info("res data ________________ :" + res.getData());
		return res;
	}

}