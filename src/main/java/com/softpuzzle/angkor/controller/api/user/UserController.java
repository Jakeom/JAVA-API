
package com.softpuzzle.angkor.controller.api.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softpuzzle.angkor.http.request.*;
import com.softpuzzle.angkor.http.response.common.ResCommDTO;
import com.softpuzzle.angkor.http.response.common.ResTrDTO;
import com.softpuzzle.angkor.service.api.user.UserService;
import com.softpuzzle.angkor.utility.CommonErrorCode;
import com.softpuzzle.angkor.utility.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Api(tags = {"사용자관리"}, description = "User")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/ankochat/api/user")
public class UserController {
	@Autowired
	private UserService service;

	@ApiOperation(value = "회원 아이디 확인", notes = "회원 아이디를 확인한다.\n" +
													"[grantType이 signup일 경우 회원가입, signin일 경우 로그인]\n"+
													"가입일 경우 회원 아이디가 이미 존재하는 아이디인지 확인한다.\n" +
													"로그인일 경우 회원 아이디가 유효한 아이디인지 확인한다.")
	@PostMapping(value = "/checkUserId")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Language", required = true, dataType = "string", paramType = "header", value = "selected language")
	})
	@ResponseBody
	public ResCommDTO checkUserId(HttpServletRequest request, @RequestBody ORGReq032 rdto) {

		log.info("request params : ________________ :" +rdto.toString());
		ResCommDTO res  = service.checkUserId(request, CommonUtil.Object2Hashmap(rdto));
		log.info("res data ________________ :" + res.getData());
		return res;
	}

	@ApiOperation(value = "회원가입", notes = "앙코르챗 신규 회원으로 가입한다. Key로 사용되는 문자는 소문자로 처리\n" +
											"회원가입 프로세스 : SMS인증 요청 => SMS인증번호 확인 => 회원가입\n\n" +
											"회원가입 시 초기에 선택한 약관동의 선택 항목에 대해 agreeList에 담아 같이 보낸다.\n" +
											"agreenemt는 선택 true/false, termOrd는 약관 항목의 순서를 뜻한다.\n\n" +
											"gender - 남자 1, 여자 2, 기타 3\n"+
											"language - 영어 en, 크메르어 khr\n"+
											"osType - ANDROID, IOS\n"+
											"version - 앱 버전(ex - 1.0)\n"+
											"\n"+
											"rdto(formData) 에 json string 형식의 value값을 넣어준다.\n" +
											"{\"agreeList\": [{\"agreement\": true, \"termOrd\": 1}], \"birth\": \"2023-03-03\", \"email\": \"\", \"gender\": 1, \"language\": \"en\", \"osType\": \"ANDROID\", \"password\": \"adkfajk23435jksjd\", \"phoneCode\": 82, \"phoneNumber\": 821022322232, \"profileMessage\": \"hello!\", \"profileUrl\": \"http://img.angkorChat.com/image/alice.png\", \"userName\": \"angkor\", \"uuid\": \"ABCD-1234...\", \"publicKey\": \"MFwwDQYJKoZIhvcNAQEBBQA...\", \"version\": 1.1}"
											)
	@PostMapping(value = "/signup", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Language", required = true, dataType = "string", paramType = "header", value = "selected language")
	})
	@ResponseBody
		public ResTrDTO signUp(HttpServletRequest request, @RequestPart ( value = "profileUrl", required = false) MultipartFile profileUrl, @RequestPart("rdto") String rdto) {

		ORGReq012 rdtoJson = new ORGReq012();
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			if(request.getHeader("OS")==null){
				rdtoJson = objectMapper.readValue(rdto, ORGReq012.class);
			}else{
				log.info(">>>>>ostype : " + request.getHeader("OS").toLowerCase().toString());
				if(request.getHeader("OS").toLowerCase().equals("ios")){
					byte[] ptext = rdto.getBytes(StandardCharsets.ISO_8859_1);
					String value = new String(ptext, StandardCharsets.UTF_8);
					rdtoJson = objectMapper.readValue(value, ORGReq012.class);
				}else{
					rdtoJson = objectMapper.readValue(rdto, ORGReq012.class);
				}
			}
		} catch (JsonProcessingException e) {
			return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.INVALID_PARAMETER.getErrorcode() + "", CommonErrorCode.INVALID_PARAMETER.getGmessage() + " : " + CommonErrorCode.INVALID_PARAMETER.getDmessage());
		}

		log.info("request params : ________________ :" +rdto.toString());
		ResTrDTO res  = service.signUp(request, profileUrl, CommonUtil.Object2Hashmap(rdtoJson));
		log.info("res count ________________ :" + res.getTrcnt());
		return res;
	}

	@ApiOperation(value = "회원 이메일 확인", notes = "회원가입 또는 비밀번호 분실시 사용가능한 이메일인지 확인한다.\n" +
													"system에 이미 등록된 이메일으로는 회원가입이 불가능하다.\n" +
													"회원가입-비밀번호설정-2차인증 Two-Setp Verification화면에서 이메일 입력 후 Next 버튼을 클릭할때 호출해서 사용이 가능한 이메일인지 확인한다.\n" +
													"\n" +
													"비밀번호를 분실하여 이메일 인증을 받기 위해 입력한 이메일이 system에 등록된 이메일인지 확인한다.\n" +
													"system에 등록되지 않은 이메일은 비밀번호 확인을 진행할 수 없다.\n" +
													"Ener your Email 화면에서 이메일 입력 후 Next 버튼을 클릭할때 호출해서 사용이 가능한 이메일인지 확인한다.")
	@PostMapping(value = "/email")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Language", required = true, dataType = "string", paramType = "header", value = "selected language")
	})
	@ResponseBody
	public ResCommDTO getEmail(HttpServletRequest request, @RequestBody ORGReq021 rdto) {

		log.info("request params : ________________ :" +rdto.toString());
		ResCommDTO res  = service.getEmail(request, CommonUtil.Object2Hashmap(rdto));
		log.info("res data ________________ :" + res.getData());
		return res;
	}

	@ApiOperation(value = "회원 정보 확인", notes = "앙코르챗 ID로 등록된 회원인지 조회한다.\n"+
												   "Key로 사용되는 문자는 소문자로 처리\n" +
			                                       "gender = 1(남자) / 2(여자) / 3(기타), privacy_profile = 0(프로필 공개) / 1(프로필 비공개)")
	@GetMapping(value = "/user/{AngkorId}")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Language", required = true, dataType = "string", paramType = "header", value = "selected language"),
			@ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header", value = "authKey (lower case letters), bearer {authKey}")
	})
	public ResCommDTO getUser(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq013 rdto) {

		log.info("request params : ________________ :" +rdto.toString());
		ResCommDTO res  = service.getUser(request, CommonUtil.Object2Hashmap(rdto));
		log.info("res data ________________ :" + res.getData());
		return res;
	}

	@ApiOperation(value = "회원 아이디 찾기", notes = "회원 아이디를 분실한 경우 이메일 인증 코드를(2차 인증)을 받아 나의 아이디를 확인한다.")
	@PostMapping(value = "/findUserId")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Language", required = true, dataType = "string", paramType = "header", value = "selected language")
	})
	@ResponseBody
	public ResCommDTO findUserId(HttpServletRequest request, @RequestBody ORGReq007 rdto) {

		log.info("request params : ________________ :" +rdto.toString());
		ResCommDTO res  = service.findUserId(request, CommonUtil.Object2Hashmap(rdto));
		log.info("res data ________________ :" + res.getData());

		HashMap mapRes = (HashMap) res.getData();
		if(mapRes!=null){
			if(mapRes.size()==0) res.setTotal(0);
			else res.setTotal(mapRes.size());
		}

		return res;
	}

	@ApiOperation(value = "회원 아이디 추가", notes = "아이디가 없는 사용자인 경우 회원 아이디를 추가한다.")
	@PostMapping(value = "/setUserId")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Language", required = true, dataType = "string", paramType = "header", value = "selected language"),
			@ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header", value = "authKey (lower case letters), bearer {authKey}")
	})
	public ResTrDTO setUserId(HttpServletRequest request, @RequestBody ORGReq034 rdto) {

		log.info("request params : ________________ :" +rdto.toString());
		ResTrDTO res  = service.setUserId(request, CommonUtil.Object2Hashmap(rdto));
		log.info("res count ________________ :" + res.getTrcnt());
		return res;
	}

	@ApiOperation(value = "회원 탈퇴", notes = "앙코르챗 회원을 탈퇴한다.\n" +
											  "Key로 사용되는 문자는 소문자로 처리")
	@DeleteMapping(value = "/user/{AngkorId}")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header", value = "authKey (lower case letters), bearer {authKey}"),
			@ApiImplicitParam(name = "AngkorId", required = true, dataType = "string", paramType = "path", value = "angkor id")
	})
	public ResTrDTO deleteUser(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq014 rdto) {

		log.info("request params : ________________ :" +rdto.toString());
		ResTrDTO res  = service.deleteUser(request, CommonUtil.Object2Hashmap(rdto));
		log.info("res count ________________ :" + res.getTrcnt());
		return res;
	}

	@ApiOperation(value = "회원 이메일 업데이트", notes = "2차 인증을 아직 하지 않거나 다른 메일로 등록하고 싶은 경우 설정 탭에서 이메일 인증 코드를 받아 확인 후 이메일 등록 및 수정 처리한다.")
	@PostMapping(value = "/email/update")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Language", required = true, dataType = "string", paramType = "header", value = "selected language"),
			@ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header", value = "authKey (lower case letters), bearer {authKey}")
	})
	public ResTrDTO updateEmail(HttpServletRequest request, @RequestBody ORGReq033 rdto) {

		log.info("request params : ________________ :" +rdto.toString());
		ResTrDTO res  = service.updateEmail(request, CommonUtil.Object2Hashmap(rdto));
		log.info("res data ________________ :" + res.getData());
		return res;
	}

	@ApiOperation(value = "회원 전화번호 업데이트", notes = "동일한 기기에서 유심변경으로 전화번호가 변경된 경우 전화번호를 업데이트 한다.\n" +
														"기존 전화번호로 로그인을 한 후 새로운 전화번호로 SMS 인증을 받은 후 호출한다.\n"+
														"grantType에는 update를, authKey에는 SMS 인증코드 요청 시 리턴된 authKey를 넣어준다.\n"+
														"header의 Authorization은 해당 사용자의 로그인 시 발급된 authKey를 사용한다. header에 Uuid 필요")
	@PostMapping(value = "/phone")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Language", required = true, dataType = "string", paramType = "header", value = "selected language"),
			@ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header", value = "authKey (lower case letters), bearer {authKey}"),
			@ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
	})
	public ResTrDTO updatePhone(HttpServletRequest request, @RequestBody ORGReq015 rdto) {

		log.info("request params : ________________ :" +rdto.toString());
		ResTrDTO res  = service.updatePhone(request, CommonUtil.Object2Hashmap(rdto));
		log.info("res data ________________ :" + res.getData());
		return res;
	}

	@ApiOperation(value = "회원 비밀번호 업데이트 (분실 시)", notes = "회원이 비밀번호를 분실하여 비밀번호를 신규 등록할때 사용한다.\n" +
																"이메일 인증 번호를 발급받은 이후 변경이 가능하다.")
	@PostMapping(value = "/pw")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Language", required = true, dataType = "string", paramType = "header", value = "selected language"),
			@ApiImplicitParam(name = "AuthKey", required = true, dataType = "string", paramType = "header", value = "new password")
	})
	public ResTrDTO updatePw(HttpServletRequest request, @RequestBody ORGReq016 rdto) {

		log.info("request params : ________________ :" +rdto.toString());
		ResTrDTO res  = service.updatePw(request, CommonUtil.Object2Hashmap(rdto));
		log.info("res data ________________ :" + res.getData());
		return res;
	}

	@ApiOperation(value = "회원 비밀번호 업데이트 (알고 있을 경우)", notes = "기존 비밀번호를 기억하는 경우 새로운 비밀번호로 업데이트한다.")
	@PostMapping(value = "/pwchange")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Language", required = true, dataType = "string", paramType = "header", value = "selected language"),
			@ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header", value = "authKey (lower case letters), bearer {authKey}")
	})
	public ResTrDTO updatePwChange(HttpServletRequest request, @RequestBody ORGReq017 rdto) {

		log.info("request params : ________________ :" +rdto.toString());
		ResTrDTO res  = service.updatePwChange(request, CommonUtil.Object2Hashmap(rdto));
		log.info("res data ________________ :" + res.getData());
		return res;
	}

	@ApiOperation(value = "회원 정보 수정", notes = "회원 정보를 수정한다.\n"+
			"Key로 사용되는 문자는 소문자로 처리\n"+
			"\n"+
			"rdto(formData) 에 json string 형식의 value값을 넣어준다.\n" +
			"{\"angkorId\": \"akadsf1231344334\", \"birth\": \"2023-03-03\", \"gender\": 1, \"privacyProfile\": true, \"profileStatus\": 1, \"profileMessage\": \"hello!\", \"userName\": \"angkor\"} \n" +
			"1. profileStatus = 1 -> 첨부 파일 프로필 이미지 변경\n"+
			"2. profileStatus = 2 ->  프로필 이미지 제외하고, 나머지 사용자 변경 항목 update\n"+
			"3. profileStatus = 3 -> 프로필 이미지 기본 이미지로 변경"
	)
	@PostMapping(value = "/user/{AngkorId}", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Language", required = true, dataType = "string", paramType = "header", value = "selected language"),
			@ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header", value = "authKey (lower case letters), bearer {authKey}"),
			@ApiImplicitParam(name = "AngkorId", required = true, dataType = "string", paramType = "path", value = "angkor id")
	})
	public ResTrDTO updateInfo(HttpServletRequest request, @RequestPart (value = "profileUrl", required = false) MultipartFile profileUrl, @RequestPart("rdto") String rdto) {

		ORGReq019 rdtoJson = new ORGReq019();
		ObjectMapper objectMapper = new ObjectMapper();

		String value;
		try {
			if(request.getHeader("OS")==null){
				rdtoJson = objectMapper.readValue(rdto, ORGReq019.class);
			}else{
				log.info(">>>>>ostype : " + request.getHeader("OS").toLowerCase().toString());
				if(request.getHeader("OS").toLowerCase().equals("ios")){
					byte[] ptext = rdto.getBytes(StandardCharsets.ISO_8859_1);
					value = new String(ptext, StandardCharsets.UTF_8);
					rdtoJson = objectMapper.readValue(value, ORGReq019.class);
				}else{
					rdtoJson = objectMapper.readValue(rdto, ORGReq019.class);
				}
			}
		} catch (JsonProcessingException e) {
			return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.INVALID_PARAMETER.getErrorcode() + "", CommonErrorCode.INVALID_PARAMETER.getGmessage() + " : " + CommonErrorCode.INVALID_PARAMETER.getDmessage());
		}

		log.info("request params : ________________ :" +rdto.toString());
		log.info(">>>>>rdtoJson : " + rdtoJson.getProfileMessage());
		ResTrDTO res  = service.updateInfo(request, profileUrl, CommonUtil.Object2Hashmap(rdtoJson));
		return res;
	}

	@ApiOperation(value = "회원 위치정보 업데이트", notes = "나의 위치정보를 등록하거나 업데이트한다.")
	@PostMapping(value = "/position")
	@ResponseBody
	public ResTrDTO updatePosition(@RequestBody ORGReq027 rdto) {

		log.info("request params : ________________ :" +rdto.toString());
		ResTrDTO res  = service.updatePosition(CommonUtil.Object2Hashmap(rdto));
		log.info("res data ________________ :" + res.getData());
		return res;
	}

	@ApiOperation(value = "회원 프로필 이미지 상태 업데이트", notes = "내가 선택한 프로필 이미지를 프로필 이미지로 등록 또는 숨김 혹은 삭제시킨다.\n"+
																	"변경하려는 상태 [1:Set as Profile Photo, 2:Hide,3:Delete]")
	@PostMapping(value = "/profileImage")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", required = true, dataType = "string", paramType = "header", value = "authKey (lower case letters), bearer {authKey}")
	})
	public ResTrDTO profileImage(HttpServletRequest request, @RequestBody ORGReq026 rdto) {

		log.info("request params : ________________ :" +rdto.toString());
		ResTrDTO res  = service.profileImage(request, CommonUtil.Object2Hashmap(rdto));
		log.info("res data ________________ :" + res.getData());
		return res;
	}

	@ApiOperation(value = "sendbird angkor DAU 수집", notes = "30분에 한번씩 DAU 수집하여 앙코르챗 서버에 관리한다.", hidden = true)
	@PostMapping(value = "/getDau")
	@ResponseBody
	@Scheduled(cron = "0 */30 * * * ?", zone="Asia/Seoul")
	public ResTrDTO getDau() {

		ResTrDTO res  = service.getDau();
		log.info("res data ________________ :" + res.getData());
		return res;
	}

	@ApiOperation(value = "회원 Chat 사운드 설정", notes = "회원의 Chat Channel_url 별로 사운드명 등록.")
	@PostMapping(value = "/setChannelSound/{AngkorId}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "AngkorId", required = true, dataType = "string", paramType = "path", value = "angkor id")
	})
	@ResponseBody
	public ResCommDTO setChannelSound(HttpServletRequest request, @PathVariable(name = "AngkorId") String angkorId, @RequestBody ORGReq060 rdto) {

		log.info("request params : ________________ :" +rdto.toString());
		rdto.setAngkorId(angkorId);
		ResCommDTO res  = service.setChannelSound(request, CommonUtil.Object2Hashmap(rdto));
		log.info("resData list ________________ :" + res.getData());
		return res;
	}
	
	@ApiOperation(value = "회원 Chat 사운드 취득", notes = "회원의 Chat Channel_url 사운드 취득.")
	@PostMapping(value = "/getChannelSound/{AngkorId}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "AngkorId", required = true, dataType = "string", paramType = "path", value = "angkor id")
	})
	@ResponseBody
	public ResCommDTO getChannelSound(HttpServletRequest request, @PathVariable(name = "AngkorId") String angkorId, @RequestBody ORGReq060 rdto) {

		log.info("request params : ________________ :" +rdto.toString());
		rdto.setAngkorId(angkorId);
		ResCommDTO res  = service.getChannelSound(request, CommonUtil.Object2Hashmap(rdto));
		log.info("resData list ________________ :" + res.getData());
		return res;
	}
	
	@ApiOperation(value = "회원 푸시 토큰 등록", notes = "회원 푸시 토큰 등록.")
	@PostMapping(value = "/setPushToken/{AngkorId}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "AngkorId", required = true, dataType = "string", paramType = "path", value = "angkor id")
	})
	@ResponseBody
	public ResCommDTO setPushToken(HttpServletRequest request, @PathVariable(name = "AngkorId") String angkorId, @RequestBody ORGReq061 rdto) {

		log.info("request params : ________________ :" +rdto.toString());
		rdto.setAngkorId(angkorId);
		ResCommDTO res  = service.setPushToken(request, CommonUtil.Object2Hashmap(rdto));
		log.info("resData list ________________ :" + res.getData());
		return res;
	}

	@ApiOperation(value = "회원 그룹 메세지 데이터 Hook", notes = "Sendbird에서 보내주는 회원 콜 그룹 메시지를 받는다")
	@PostMapping(value = "/callLog")
	@ResponseBody
	public ResCommDTO callLog(HttpServletRequest request, @RequestBody String str) throws Exception {
		HashMap tmp = new HashMap<String,Object>();
		tmp.put("str", str);
		
		log.info("-----0---------------------------");
		service.callLog(tmp);
		log.info("callLog:::::::::::::::res data ________________ :" + str);
		return new ResCommDTO();
	}
	
	@ApiOperation(value = "회원 메세지 목록 출력", notes = "Call 목록에서 삭제한 Call_id 를 등록합니다.")
	@GetMapping(value = "/getMessageLog/{AngkorId}")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "AngkorId", required = true, dataType = "string", paramType = "path", value = "angkor id"),
			@ApiImplicitParam(name = "CreateAt", required = false, dataType = "string", paramType = "query", value = "call Create timestamp"),
			@ApiImplicitParam(name = "CustomType", required = false, dataType = "string", paramType = "query", value = "call Create timestamp")
	})
	public ResCommDTO getMessageLog(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq013 rdto) {

		log.info("request params : ________________ :" +rdto.toString());
		ResCommDTO res  = service.getMessageLog(request, CommonUtil.Object2Hashmap(rdto));
		log.info("res data ________________ :" + res.getData());

		return res;
	}
	
	@ApiOperation(value = "회원 푸시 토큰 등록", notes = "회원 푸시 토큰 등록.")
	@GetMapping(value = "/setOnOff/{AngkorId}")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "AngkorId", required = true, dataType = "string", paramType = "path", value = "angkor id")
	})
	@ResponseBody
	public ResCommDTO setOnOff(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq013 rdto) {
		
		ResCommDTO res  = service.setOnOff(request, CommonUtil.Object2Hashmap(rdto));
		log.info("resData list ________________ :" + res.getData());
		return res;
	}

}