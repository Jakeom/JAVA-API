
package com.softpuzzle.angkor.controller.api.address;

import com.softpuzzle.angkor.http.request.*;
import com.softpuzzle.angkor.http.response.common.ResCommDTO;
import com.softpuzzle.angkor.http.response.common.ResTrDTO;
import com.softpuzzle.angkor.service.api.address.AddressService;
import com.softpuzzle.angkor.utility.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;

@Api(tags = {"주소록(친구) 관리"}, description = "Address")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/ankochat/api/address")
public class AddressController {
	@Autowired
	private AddressService service;

	@ApiOperation(value = "나의 친구 목록 리스트", notes = "나의 친구 목록 전체 리스트를 불러온다.")
	@GetMapping(value = "/getAllAddressList/{AngkorId}")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "AngkorId", required = true, dataType = "string", paramType = "path", value = "angkor id"),
			@ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
	})
	public ResCommDTO getAllAddressList(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq013 rdto) {

		log.info("request params : ________________ :" +rdto.toString());
		ResCommDTO res  = service.getAllAddresList(request, CommonUtil.Object2Hashmap(rdto));
		log.info("res data ________________ :" + res.getData());
		return res;
	}

	@ApiOperation(value = "나의 즐겨찾기 친구 목록 리스트", notes = "나의 즐겨찾기 친구 목록 전체 리스트를 불러온다.")
	@GetMapping(value = "/getFavoriteAddressList/{AngkorId}")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "AngkorId", required = true, dataType = "string", paramType = "path", value = "angkor id"),
			@ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
	})
	public ResCommDTO getFavoriteAddressList(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq013 rdto) {

		log.info("request params : ________________ :" +rdto.toString());
		ResCommDTO res  = service.getFavoriteAddresList(request, CommonUtil.Object2Hashmap(rdto));
		log.info("res data ________________ :" + res.getData());
		return res;
	}

	@ApiOperation(value = "프로필(본인) 상세정보", notes = "프로필 조회를 위해 프로필 상세정보를 불러온다.\n"+
														 "나의 angkorId를 입력해준다.")
	@GetMapping(value = "/getMyAddressInfo/{AngkorId}")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "AngkorId", required = true, dataType = "string", paramType = "path", value = "angkor id"),
			@ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
	})
	public ResCommDTO getMyAddressInfo(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq013 rdto) {

		log.info("request params : ________________ :" +rdto.toString());
		ResCommDTO res  = service.getMyAddressInfo(request, CommonUtil.Object2Hashmap(rdto));
		log.info("res data ________________ :" + res.getData());
		return res;
	}

	@ApiOperation(value = "프로필(상대방) 상세정보", notes = "프로필 조회를 위해 프로필 상세정보를 불러온다.\n"+
															"나의 angkorId와 상대방 FriendAngkorId도 같이 입력해준다.\n"+
															"상대방의 프로필일 경우 friend_yn로 친구여부를 구분할 수 있다.")
	@GetMapping(value = "/getFriendAddressInfo/{AngkorId}")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "AngkorId", required = true, dataType = "string", paramType = "path", value = "angkor id"),
			@ApiImplicitParam(name = "Friend_angkorId", dataType = "string", paramType = "query", value = "friend angkor id"),
			@ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
	})
	public ResCommDTO getAddressInfo(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq013 rdto, @RequestParam(value = "Friend_angkorId", required = false) String friendAngkorId) {

		log.info("request params : ________________ :" +rdto.toString());
		ResCommDTO res  = service.getAddressInfo(request, CommonUtil.Object2Hashmap(rdto), friendAngkorId);
		log.info("res data ________________ :" + res.getData());
		return res;
	}

	@ApiOperation(value = "친구 차단 추가/해제", notes = "나의 친구 목록 중에서 차단하고 싶은 친구가 있다면 차단을 추가한다. 혹은 이전에 차단한 친구를 다시 차단 해제시킨다.")
	@PostMapping(value = "/setBlock")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
	})
	public ResTrDTO setBlock(HttpServletRequest request, @RequestBody ORGReq018 rdto) {

		log.info("request params : ________________ :" +rdto.toString());
		ResTrDTO res  = service.blockMyFriend(request, CommonUtil.Object2Hashmap(rdto));
		log.info("res data ________________ :" + res.getData());
		return res;
	}

	@ApiOperation(value = "친구 즐겨찾기 추가/해제", notes = "친구를 선택하여 즐겨찾기 목록에 추가한다. 혹은 즐겨찾기에 추가되어 있는 친구를 다시 해제시킨다.")
	@PostMapping(value = "/setFavorite")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
	})
	public ResTrDTO setFavorite(HttpServletRequest request, @RequestBody ORGReq018 rdto) {

		log.info("request params : ________________ :" +rdto.toString());
		ResTrDTO res  = service.favoriteMyFriend(request, CommonUtil.Object2Hashmap(rdto));
		log.info("res data ________________ :" + res.getData());
		return res;
	}

	@ApiOperation(value = "내 프로필 검색 허용", notes = "나와 친구가 아닌 유저에게도 내 프로필 검색이 가능하도록 허용시키거나 금지한다.")
	@PostMapping(value = "/setShareProfile")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
	})
	public ResTrDTO setShareProfile(HttpServletRequest request, @RequestBody ORGReq022 rdto) {

		log.info("request params : ________________ :" +rdto.toString());
		ResTrDTO res  = service.updateShareProfile(request, CommonUtil.Object2Hashmap(rdto));
		log.info("res data ________________ :" + res.getData());
		return res;
	}

	@ApiOperation(value = "앙코르챗 친구 추가", notes = "선택한 유저를 나의 친구 목록에 추가한다.\n" +
													 "나의 angkorId는 필수값, friend_angkorId 나 friend_phoneNumber 둘 중 하나는 필수로 입력되어야 한다.\n" +
													 "1. friend_angkorId 로만 친구 추가하는 경우\n" +
			 										 "2. friend_name 입력 없이 진행할 경우 해당 사용자의 앙코르챗 이름을 그대로 가져온다.\n" +
													 "3. friend_phonenumber로만 친구 추가하는 경우")
	@PostMapping(value = "/setAddress")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
	})
	public ResTrDTO setAddress(HttpServletRequest request, @RequestBody ORGReq024 rdto) {

		log.info("request params : ________________ :" +rdto.toString());
		ResTrDTO res  = service.setAddress(request, CommonUtil.Object2Hashmap(rdto));
		log.info("res data ________________ :" + res.getData());
		return res;
	}

	@ApiOperation(value = "나의 휴대폰 주소록의 앙코르챗 가입 친구 일괄 추가", notes = "내 휴대폰 주소록에 앙코르챗 가입이 되어있는 친구를 나의 친구 목록으로 일괄 설정한다.")
	@PostMapping(value = "/setBulkAddress")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
	})
	public ResTrDTO setBulkAddress(HttpServletRequest request, @RequestBody ORGReq025 rdto) {

		log.info("request params : ________________ :" +rdto.toString());
		ResTrDTO res  = service.setBulkAddress(request, CommonUtil.Object2Hashmap(rdto));
		log.info("res data ________________ :" + res.getData());
		return res;
	}

	@ApiOperation(value = "나의 친구목록에서 친구 삭제", notes = "앙코르챗 나의 친구목록 중 선택한 친구를 목록에서 삭제한다.")
	@DeleteMapping(value = "/dtAddress/{AngkorId}")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "AngkorId", required = true, dataType = "string", paramType = "path", value = "angkor id"),
			@ApiImplicitParam(name = "Friend_angkorId", dataType = "string", paramType = "query", value = "friend angkor id"),
			@ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
	})
	public ResTrDTO dtAddress(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq013 rdto, @RequestParam(value = "Friend_angkorId", required = false) String friendAngkorId) {

		log.info("request params : ________________ :" +rdto.toString());
		ResTrDTO res  = service.dtAddress(request, CommonUtil.Object2Hashmap(rdto), friendAngkorId);
		log.info("res count ________________ :" + res.getTrcnt());
		return res;
	}

	@ApiOperation(value = "나 또는 친구의 프로필 이미지 리스트 확인", notes = "나 혹은 친구가 올린 Photos를 모두 확인한다.\n"+
																		"나의 프로필 이미지를 확인하고 싶다면 나의 angkorId를, 상대의 프로필 이미지를 확인하고 싶다면 상대방의 friendAngkorId를 입력해준다.")
	@GetMapping(value = "/getProfilesImage/{AngkorId}")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "AngkorId", required = true, dataType = "string", paramType = "path", value = "angkor id"),
			@ApiImplicitParam(name = "Friend_angkorId", dataType = "string", paramType = "query", value = "friend angkor id"),
			@ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
	})
	public ResTrDTO getProfilesImage(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq013 rdto,  @RequestParam(value = "Friend_angkorId", required = false) String friendAngkorId) {

		log.info("request params : ________________ :" +rdto.toString());
		ResTrDTO res  = service.getProfilesImage(request, CommonUtil.Object2Hashmap(rdto), friendAngkorId);
		log.info("res count ________________ :" + res.getTrcnt());
		return res;
	}

	@ApiOperation(value = "사용자 근처 주변 가입자 리스트 조회", notes = "나의 근처(1km)에 있는 다른 앙코르챗 사용자 리스트를 검색한다.\n"+
			              											  "프로필 검색(공개여부)이 허용된 사용자 리스트만 불러온다.")
	@GetMapping(value = "/getAddressListNearBy/{AngkorId}")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "AngkorId", required = true, dataType = "string", paramType = "path", value = "angkor id"),
			@ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
	})
	public ResCommDTO getAddressListNearBy(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq013 rdto) {

		log.info("request params : ________________ :" +rdto.toString());
		ResCommDTO res  = service.getAddressListNearBy(request, CommonUtil.Object2Hashmap(rdto));
		log.info("res data ________________ :" + res.getData());
		return res;
	}

	@ApiOperation(value = "전화번호 입력하여 사용자 검색", notes = "전화번호(phoneNumber)를 입력하여 앙코르챗 사용자를 검색한다.")
	@GetMapping(value = "/getAddressInfoByPhone/{AngkorId}")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Language", required = true, dataType = "string", paramType = "header", value = "selected language"),
			@ApiImplicitParam(name = "AngkorId", required = true, dataType = "string", paramType = "path", value = "angkor id"),
			@ApiImplicitParam(name = "phoneNumber", required = true, dataType = "string", paramType = "query", value = "phoneNumber"),
			@ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
	})
	@ResponseBody
	public ResCommDTO getAddressInfoByPhone(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq013 rdto, @RequestParam(value = "phoneNumber", required = false) String phoneNumber) {

		log.info("request params : ________________ :" +rdto.toString());
		ResCommDTO res  = service.getAddressInfoByPhone(request, CommonUtil.Object2Hashmap(rdto), phoneNumber);
		log.info("res data ________________ :" + res.getData());
		return res;
	}

	@ApiOperation(value = "친구 추천", notes = "내 친구목록에는 없지만 나를 친구 추가한 상대방 사용자의 리스트를 보여준다.")
	@GetMapping(value = "/getSuggestAddressList/{AngkorId}")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "AngkorId", required = true, dataType = "string", paramType = "path", value = "angkor id"),
			@ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
	})
	public ResCommDTO getSuggestAddressList(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq013 rdto) {

		log.info("request params : ________________ :" +rdto.toString());
		ResCommDTO res  = service.getSuggestAddressList(request, CommonUtil.Object2Hashmap(rdto));
		log.info("res data ________________ :" + res.getData());
		return res;
	}

	@ApiOperation(value = "내가 차단한 친구 목록 리스트", notes = "내가 차단한 친구 목록 전체 리스트를 불러온다.")
	@GetMapping(value = "/getBlockAddressList/{AngkorId}")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "AngkorId", required = true, dataType = "string", paramType = "path", value = "angkor id"),
			@ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
	})
	public ResCommDTO getBlockAddressList(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq013 rdto) {

		log.info("request params : ________________ :" +rdto.toString());
		ResCommDTO res  = service.getBlockAddressList(request, CommonUtil.Object2Hashmap(rdto));
		log.info("res data ________________ :" + res.getData());
		return res;
	}

	@ApiOperation(value = "나의 휴대폰 주소록에 앙코르챗 가입이 되어 있지 않은 친구 리스트 확인", notes = "내 휴대폰 주소록에 앙코르챗 가입이 되어 있지 않은 친구의 목록을 조회한다.")
	@PostMapping(value = "/checkIsNotUserAddressList")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
	})
	public ResCommDTO checkIsNotUserAddressList(HttpServletRequest request, @RequestBody ORGReq031 rdto) {

		log.info("request params : ________________ :" +rdto);
		ResCommDTO res  = service.checkIsNotUserAddressList(request, CommonUtil.Object2Hashmap(rdto));
		log.info("res data ________________ :" + res.getData());

		HashMap mapRes = (HashMap) res.getData();
		if(mapRes!=null){
			Object obj = mapRes.get("isNotUserList");
			ArrayList<String> isNotUserList = new ArrayList<>();

			if (obj instanceof ArrayList) {
				isNotUserList = (ArrayList<String>) obj;
			}
			if(isNotUserList.size()==0) res.setTotal(0);
			else res.setTotal(isNotUserList.size());
		}
		return res;
	}

	@ApiOperation(value = "나의 QR코드 확인하기", notes = "나의 QR코드를 확인한다.")
	@GetMapping(value = "/getMyQrCode/{AngkorId}")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "AngkorId", required = true, dataType = "string", paramType = "path", value = "angkor id"),
			@ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
	})
	public ResCommDTO getMyQrCode(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq013 rdto) {

		log.info("request params : ________________ :" +rdto.toString());
		ResCommDTO res  = service.getMyQrCode(request, CommonUtil.Object2Hashmap(rdto));
		log.info("res data ________________ :" + res.getData());
		return res;
	}

	@ApiOperation(value = "QR코드 스캔하여 유저아이디로 상대방 프로필 조회", notes = "QR코드 스캔하여 스캔한 상대방의 프로필 화면으로 이동한다.\n"+
			"나의 angkorId와 스캔한 상대방 userId도 같이 입력해준다.\n"+
			"상대방의 프로필일 경우 friend_yn로 친구여부를 구분할 수 있다.\n"+
			"나의 프로필일 경우 마이 프로필 화면으로 이동한다.")
	@GetMapping(value = "/getFriendAddrByQrScan/{AngkorId}")
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Language", required = true, dataType = "string", paramType = "header", value = "selected language"),
			@ApiImplicitParam(name = "AngkorId", required = true, dataType = "string", paramType = "path", value = "angkor id"),
			@ApiImplicitParam(name = "Friend_userId", dataType = "string", paramType = "query", value = "friend user id"),
			@ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
	})
	public ResCommDTO getFriendAddrByQrScan(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq013 rdto, @RequestParam(value = "Friend_userId") String friendUserId) {

		log.info("request params : ________________ :" +rdto.toString());
		ResCommDTO res  = service.getFriendAddrByQrScan(request, CommonUtil.Object2Hashmap(rdto), friendUserId);
		log.info("res data ________________ :" + res.getData());

		HashMap mapRes = (HashMap) res.getData();
		if(mapRes!=null){
			res.setTotal(1);
		}
		return res;

	}
}