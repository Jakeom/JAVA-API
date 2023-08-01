
package com.softpuzzle.angkor.controller.api.test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softpuzzle.angkor.database.mapper.api.test.TestMapper;
import com.softpuzzle.angkor.database.mapper.api.user.UserMapper;
import com.softpuzzle.angkor.http.request.ORGReq010;
import com.softpuzzle.angkor.http.request.ORGReq012Test;
import com.softpuzzle.angkor.http.response.common.ResCommDTO;
import com.softpuzzle.angkor.http.response.common.ResTrDTO;
import com.softpuzzle.angkor.service.api.s3.S3Service;
import com.softpuzzle.angkor.service.api.test.TestService;
import com.softpuzzle.angkor.utility.CommonErrorCode;
import com.softpuzzle.angkor.utility.CommonUtil;
import com.softpuzzle.angkor.utility.PushUtil;
import com.softpuzzle.angkor.utility.SendbirdUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Api(tags = {"테스트"}, description = "Test")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/ankochat/api/test")
public class TestController {
	@Autowired
	private S3Service s3Service;
	@Autowired
	private TestService service;
	@Autowired
	private TestMapper dao;
	@Autowired
	private UserMapper userDao;

	@ApiOperation(value = "S3", notes = "s3 test")
	@PostMapping(value = "/s3Upload", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE }, produces = MediaType.APPLICATION_JSON_VALUE)
	public void s3Upload(HttpServletRequest request, @RequestPart ( value = "testUrl", required = false) MultipartFile testUrl){

		String url = s3Service.save(testUrl, "test"); // sns : 디렉터리명
		log.info("url : " + url); // https://angkorchat-bucket.s3.ap-southeast-1.amazonaws.com/test/00bfd8f1205040d6863dabb5ccdbe739.png
	}

	/* userId 추가된 소스 - notes 설명 추가, ORGReq012 추가*/
	@ApiOperation(value = "회원가입", notes = "앙코르챗 신규 회원으로 가입한다. Key로 사용되는 문자는 소문자로 처리\n" +
			"회원가입 프로세스 : SMS인증 요청 => SMS인증번호 확인 => 회원 아이디 중복 확인 및 입력 => 비밀번호 입력 => 내 정보 입력 => 회원가입\n\n" +
			"회원가입 시 초기에 선택한 약관동의 선택 항목에 대해 agreeList에 담아 같이 보낸다.\n" +
			"agreenemt는 선택 true/false, termOrd는 약관 항목의 순서를 뜻한다.\n\n" +
			"gender - 남자 1, 여자 2, 기타 3\n"+
			"language - 영어 en, 크메르어 khr\n"+
			"osType - ANDROID, IOS\n"+
			"version - 앱 버전(ex - 1.0)\n"+
			"\n"+
			"rdto(formData) 에 json string 형식의 value값을 넣어준다.\n" +
			"{\"agreeList\": [{\"agreement\": true, \"termOrd\": 1}], \"birth\": \"2023-03-03\", \"email\": \"\", \"gender\": 1, \"language\": \"en\", \"osType\": \"ANDROID\", \"userId\": \"testId1234\", \"password\": \"adkfajk23435jksjd\", \"phoneCode\": 82, \"phoneNumber\": 821022322232, \"profileMessage\": \"hello!\", \"profileUrl\": \"http://img.angkorChat.com/image/alice.png\", \"userName\": \"angkor\", \"uuid\": \"ABCD-1234...\", \"publicKey\": \"MFwwDQYJKoZIhvcNAQEBBQA...\", \"version\": 1.1}"
	, hidden = true)
	@PostMapping(value = "/signup", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResTrDTO signUp(HttpServletRequest request, @RequestPart ( value = "profileUrl", required = false) MultipartFile profileUrl, @RequestPart("rdto") String rdto) {

		ORGReq012Test rdtoJson = new ORGReq012Test();
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			if(request.getHeader("OS")==null){
				rdtoJson = objectMapper.readValue(rdto, ORGReq012Test.class);
			}else{
				log.info(">>>>>ostype : " + request.getHeader("OS").toLowerCase().toString());
				if(request.getHeader("OS").toLowerCase().equals("ios")){
					byte[] ptext = rdto.getBytes(StandardCharsets.ISO_8859_1);
					String value = new String(ptext, StandardCharsets.UTF_8);
					rdtoJson = objectMapper.readValue(value, ORGReq012Test.class);
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

	@ApiOperation(value = "사용자 로그인", notes = "앙코르챗 사용자 로그인을 시도한다.\n" +
			"grant type을 password로 설정\n" +
			"[로그인 프로세스]\n" +
			"\n" +
			"전화번호, 아이디, 비밀번호 입력 => 200 리턴 => 로그인 완료\n" +
			"전화번호, 아이디, 비밀번호 입력 => 400417 오류 리턴 (등록된 기기가 아님) => SMS인증 요청 => SMS인증번호 확인 => 로그인 완료"
	, hidden = true)
	@PostMapping(value = "/signin")
	@ResponseBody
	public ResCommDTO signIn(HttpServletRequest request, @RequestBody ORGReq010 rdto) {

		log.info("request params : ________________ :" +rdto.toString());
		ResCommDTO res  = service.signIn(request, CommonUtil.Object2Hashmap(rdto));
		log.info("res data ________________ :" + res.getData());
		return res;
	}

	@ApiOperation(value = "샌드버드 회원 삭제", notes = "기존 삭제 회원 샌드버드 유저 삭제 처리 진행", hidden = true)
	@PostMapping(value = "/quitSendbird")
	@ResponseBody
	public int quitSendbird(HttpServletRequest request) throws Exception {

		List<HashMap> quitList = dao.getQuitUserList();
		int cnt = 0;
		for(HashMap user : quitList){
			HashMap params = new HashMap();
			params.put("angkorId", user.get("angkorId"));
			SendbirdUtils.deleteUserBySendbird(params);
			cnt++;
		}
		return cnt;
	}

	@ApiOperation(value = "샌드버드 유저 메타데이터 업데이트", notes = "기존 회원 샌드버드 유저 메타 데이터 일괄 업데이트")
	@PostMapping(value = "/update/sendbirdUserMetaData")
	@ResponseBody
	public int sendbirdUserMetaData(HttpServletRequest request) throws Exception {

		List<HashMap> userList = dao.getUserList();
		int cnt = 0;
		for(HashMap user : userList){
			HashMap params = new HashMap();
			params.put("angkorId", user.get("angkorId"));
			params.put("inOnlineRange", user.get("inOnlineRange").toString());
			params.put("inPhotoRange", user.get("inPhotoRange").toString());
			SendbirdUtils.updateUserMetaDataBySendbird(params);
			cnt++;
		}
		return cnt;
	}
	
	@ApiOperation(value = "FCM테스트 APNS테스트 ", notes = "FCM테스트 APNS테스트")
	@GetMapping(value = "/makeToken")
	@ResponseBody
	public ResCommDTO makeToken(HttpServletRequest request) throws Exception {
		
//		List<String> users = userDao.getNoTokenUser(null);
//		for(String userId : users) {
//			JSONObject tmp = new  JSONObject();
//			tmp.put("user_id", userId);
//			tmp.put("token_type", "apns");
//			HashMap map = SendbirdUtils.getUserPushInfo(tmp);
//			if(map == null) {continue;}
//			List<String> tokens = (List<String>) map.get("tokens");
//			for(String token : tokens) {
//				HashMap<String, Object> params = new HashMap<String, Object>();
//				params.put("angkorId", userId);
//				params.put("os", "i");
//				params.put("token", token);
//				userDao.insertUserPushToken(params);
//				break;
//			}
//			
//			
//		
//		}
		
//		users = userDao.getNoTokenUser(null);
//		for(String userId : users) {
//			JSONObject tmp = new  JSONObject();
//			tmp.put("user_id", userId);
//			tmp.put("token_type", "apns");
//			HashMap map = SendbirdUtils.getUserPushInfo(tmp);
//			
//			List<String> tokens = (List<String>) map.get("tokens");
//			for(String token : tokens) {
//				
//			}
//			break;
//		}
		
		return new ResCommDTO();
	}
	
	@ApiOperation(value = "FCM테스트 APNS테스트 ", notes = "FCM테스트 APNS테스트")
	@GetMapping(value = "/message")
	@ResponseBody
	public ResCommDTO sendApns(HttpServletRequest request, @RequestBody String str) throws Exception {
		
		String token = null;
		String os = null;
		String onoff = "N";
		if(request.getParameter("token") != null) {
			token = request.getParameter("token");
		}
		if(request.getParameter("os") != null) {
			os = request.getParameter("os");
		}
		
		if(request.getParameter("onoff") != null) {
			onoff = request.getParameter("onoff");
		}
		
		String isDev = request.getParameter("isDev");
		
		if(token != null && os != null) {
			PushUtil pu = new PushUtil();
			JSONObject tmp = new JSONObject(str);
			if("i".equals(os)) {
				pu.sendIos(token, tmp,onoff,isDev);	
			}else {
				pu.sendAndroid(token, tmp,onoff,isDev);
			}
		}
				
		return new ResCommDTO();
	}
	
}