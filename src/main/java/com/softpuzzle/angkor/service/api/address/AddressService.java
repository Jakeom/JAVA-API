package com.softpuzzle.angkor.service.api.address;

import com.softpuzzle.angkor.database.mapper.api.address.AddressMapper;
import com.softpuzzle.angkor.gcache.GlobalObjects;
import com.softpuzzle.angkor.http.request.ORGReq023;
import com.softpuzzle.angkor.http.response.common.ResCommDTO;
import com.softpuzzle.angkor.http.response.common.ResTrDTO;
import com.softpuzzle.angkor.service.api.cert.CertService;
import com.softpuzzle.angkor.service.api.s3.S3Service;
import com.softpuzzle.angkor.utility.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressService {
	@Autowired
	private CertService certService;
	@Autowired
	private S3Service s3Service;
	@Autowired
	private AddressMapper dao;

	public ResCommDTO getAllAddresList(HttpServletRequest request, HashMap params) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseObject(null, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		if (StringUtils.isBlank((String)params.get("angkorId"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		try {
			// 1. 유효한 아이디인지 확인
			int isUser = dao.checkMyAngKorId(params);
			if(isUser>0) {
				List<HashMap> list = dao.getAllAddresList(params);
				return CommonUtil.setResponseObject(list,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
			}else{
				return CommonUtil.setResponseObject(null,CommonErrorCode.INVALID_USER.getErrorcode()+"",CommonErrorCode.INVALID_USER.getGmessage()+" : "+CommonErrorCode.INVALID_USER.getDmessage());
			}
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResCommDTO getFavoriteAddresList(HttpServletRequest request, HashMap params) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseObject(null, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		if (StringUtils.isBlank((String)params.get("angkorId"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		try {
			// 1. 유효한 아이디인지 확인
			int isUser = dao.checkMyAngKorId(params);
			if(isUser>0) {
				// 2. 즐겨찾기 되어있는 친구 가져오기
				List<HashMap> list = dao.getFavoriteAddresList(params);
				return CommonUtil.setResponseObject(list,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
			}else{
				return CommonUtil.setResponseObject(null,CommonErrorCode.INVALID_USER.getErrorcode()+"",CommonErrorCode.INVALID_USER.getGmessage()+" : "+CommonErrorCode.INVALID_USER.getDmessage());
			}
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResCommDTO getMyAddressInfo(HttpServletRequest request, HashMap params) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseObject(null, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		if (StringUtils.isBlank((String)params.get("angkorId"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		try {
			// 1. 유효한 아이디인지 확인
			int isUser = dao.checkMyAngKorId(params);
			if(isUser>0) {
				HashMap data = dao.getMyProfileInfo(params);
				return CommonUtil.setResponseObject(data,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
			}else{
				return CommonUtil.setResponseObject(null,CommonErrorCode.INVALID_USER.getErrorcode()+"",CommonErrorCode.INVALID_USER.getGmessage()+" : "+CommonErrorCode.INVALID_USER.getDmessage());
			}
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResCommDTO getAddressInfo(HttpServletRequest request, HashMap params, String friendAngkorId) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseObject(null, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		if (StringUtils.isBlank((String)params.get("angkorId")) || StringUtils.isBlank(friendAngkorId)) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}
		HashMap data;

		try {
			// 1. 유효한 아이디인지 확인
			int isUser = 0;
			// 친구의 프로필을 조회하는 경우
			if(!StringUtils.isBlank(friendAngkorId)){
				isUser = dao.checkFriendAngKorId(friendAngkorId);
			}else{
				isUser = dao.checkMyAngKorId(params);
			}
			if(isUser>0) {
				// 상대방 앙코르챗 id와 내 id가 같을 경우 예외 처리
				if(params.get("angkorId").equals(friendAngkorId)){
					return CommonUtil.setResponseObject(null,CommonErrorCode.PROFILE_SAME_ID_ERROR.getErrorcode()+"",CommonErrorCode.PROFILE_SAME_ID_ERROR.getGmessage()+" : "+CommonErrorCode.PROFILE_SAME_ID_ERROR.getDmessage());
				}
				params.put("friendAngkorId", friendAngkorId);
				// 2. 상대방의 프로필일 경우
				data = dao.getAnotherUserProfileInfo(params);
				return CommonUtil.setResponseObject(data,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
			}else{
				return CommonUtil.setResponseObject(null,CommonErrorCode.INVALID_USER.getErrorcode()+"",CommonErrorCode.INVALID_USER.getGmessage()+" : "+CommonErrorCode.INVALID_USER.getDmessage());
			}
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResTrDTO blockMyFriend(HttpServletRequest request, HashMap params) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		if (StringUtils.isBlank((String)params.get("angkorId")) || StringUtils.isBlank((String)params.get("friend_angkorId"))) {
			return CommonUtil.setResponseTrObject(null, 0,CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		int cnt=0;
		// response에 차단 여부 결과값 status Y/N 전달
		HashMap response = new HashMap();
		try {
			HashMap map = dao.checkBlock(params);
			if(map==null || (Long) map.get("isUser")==0){
				return CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.INVALID_PARAMETER.getErrorcode()+"",CommonErrorCode.INVALID_PARAMETER.getGmessage()+" : "+CommonErrorCode.INVALID_PARAMETER.getDmessage());
			}
			if((Long)map.get("blockCnt")>0){
				// sendbird api - unblock user
				Boolean unBlockUserId = SendbirdUtils.updateUnBlockUser(params);
				if(unBlockUserId){
					cnt = dao.deleteBlock(params);
					response.put("status", "N");
				}
			}else{
				// sendbird api - block user
				String blockUserId = SendbirdUtils.updateBlockUser(params);
				if(!StringUtils.isBlank(blockUserId)){
					cnt = dao.insertBlock(params);
					dao.deleteFriend(params);
					dao.deleteFavorite(params);
					response.put("status", "Y");
				}
			}
			return CommonUtil.setResponseTrObject(response, cnt,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
		} catch (Exception e) {
			return CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResTrDTO favoriteMyFriend(HttpServletRequest request, HashMap params) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		if (StringUtils.isBlank((String)params.get("angkorId")) || StringUtils.isBlank((String)params.get("friend_angkorId"))) {
			return CommonUtil.setResponseTrObject(null, 0,CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		int cnt=0;
		// response에 즐겨찾기 여부 결과값 status Y/N 전달
		HashMap response = new HashMap();
		try {
			HashMap map = dao.checkFavorite(params);
			if(map!=null){
				cnt = dao.deleteFavorite(params);
				response.put("status", "N");
			}else{
				HashMap fMap = dao.getFriendInfo(params);
				if(fMap==null){
					return CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.INVALID_PARAMETER.getErrorcode()+"",CommonErrorCode.INVALID_PARAMETER.getGmessage()+" : "+CommonErrorCode.INVALID_PARAMETER.getDmessage());
				}
				if(!StringUtils.isBlank((String)fMap.get("friend_phonenumber"))){
					params.put("friend_phonenumber", fMap.get("friend_phonenumber"));
				}else{
					params.put("friend_phonenumber", null);
				}
				params.put("chatting_message", fMap.get("friend_profileMessage"));
				params.put("friend_name", fMap.get("friend_name"));
				params.put("friend_url", fMap.get("friend_url"));
				cnt = dao.insertFavorite(params);
				response.put("status", "Y");
			}
			return CommonUtil.setResponseTrObject(response, cnt,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
		} catch (Exception e) {
			return CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResTrDTO updateShareProfile(HttpServletRequest request, HashMap params) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		if (StringUtils.isBlank((String)params.get("angkorId")) || params.get("showProfileYn") == null) {
			return CommonUtil.setResponseTrObject(null, 0,CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		int cnt=0;

		try {
			cnt = dao.updateShareProfile(params);
			if(cnt>0){
				return CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
			}else{
				return CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.INVALID_USER.getErrorcode()+"",CommonErrorCode.INVALID_USER.getGmessage()+" : "+CommonErrorCode.INVALID_USER.getDmessage());
			}
		} catch (Exception e) {
			return CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResTrDTO setAddress(HttpServletRequest request, HashMap params) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		if (StringUtils.isBlank((String)params.get("angkorId"))) {
			return CommonUtil.setResponseTrObject(null, 0,CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		if (StringUtils.isBlank((String)params.get("friend_angkorId")) && StringUtils.isBlank((String)params.get("friend_phonenumber"))) {
			return CommonUtil.setResponseTrObject(null, 0,CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		if(StringUtils.isBlank((String)params.get("friend_phonenumber"))){
			params.put("friend_phonenumber", null);
		}

		int cnt=0;

		try {
			// 1. 유효한 아이디인지 확인
			HashMap map = new HashMap<>();
			map.put("angkorId", params.get("angkorId"));
			map.put("friend_phonenumber", params.get("friend_phonenumber"));

			int isUser = dao.checkMyAngKorId(map);
			if(isUser>0) {
				// 2. 친구 추가
				if(params.get("friend_phonenumber")!=null) {
					// 번호 통일 - ex) 821012341234, 01012341234, 8201012341234 모두 같은 번호로 인식해야 함
					// case1 : 01012341234 -> 그대로 sql문
					// case2 : 821012341234-> 82를 제외하고 0을 붙여줌
					// case3 : 8201012341234 일경우 - 82를 제외함
					// case4 : 1043536236 일경우 - 0 붙여줌
					String phone = ((String) params.get("friend_phonenumber"));
					if (phone.startsWith("82")) {
						String prefixToRemove = "82";
						phone = phone.replace(prefixToRemove, "");
						params.put("friend_phonenumber", phone);
					}

					if (phone.startsWith("10")) {
						phone = "0" + phone;
						params.put("friend_phonenumber", phone);
					}

				}
				cnt = dao.insertFriend(params);

				if(StringUtils.isBlank((String)params.get("friend_angkorId")) && !StringUtils.isBlank((String)params.get("friend_phonenumber"))){
					String angkorId = dao.getAngkorIdByPhoneAndPhoneCode(map);
					map.put("friendAngkorId", angkorId);
				}else{
					map.put("friendAngkorId", params.get("friend_angkorId"));
				}

				HashMap response = dao.getAnotherUserProfileInfo(map);
				return CommonUtil.setResponseTrObject(response, cnt,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
			}else{
				return CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.INVALID_USER.getErrorcode()+"",CommonErrorCode.INVALID_USER.getGmessage()+" : "+CommonErrorCode.INVALID_USER.getDmessage());
			}
		} catch (Exception e) {
			return CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResTrDTO setBulkAddress(HttpServletRequest request, HashMap params) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		if (StringUtils.isBlank((String)params.get("angkorId"))) {
			return CommonUtil.setResponseTrObject(null, 0,CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		List<ORGReq023> list = (List<ORGReq023>) params.get("friendList");

		if(list==null){
			return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.SUCCESS.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}else{
			for (ORGReq023 data : list) {
				if(StringUtils.isBlank(data.getFriend_phonenumber())){
					return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
				}
			}
		}

		int cnt=0;

		try {
			// 1. 유효한 아이디인지 확인
			HashMap map = new HashMap<>();
			map.put("angkorId", params.get("angkorId"));

			int isUser = dao.checkMyAngKorId(map);
			if(isUser>0) {
				// 2. 친구 목록 일괄 추가
				Set<String> uniqueNumbers = new HashSet<>();
				for (ORGReq023 data : list) {
					if(!StringUtils.isBlank(data.getFriend_phonenumber())) {

						HashMap fData = new HashMap();
						fData.put("friend_phonenumber", data.getFriend_phonenumber());
						if (data.getFriend_name() == null || data.getFriend_name().equals("")) {
							fData.put("friend_name", null);
						} else {
							fData.put("friend_name", data.getFriend_name());
						}
						fData.put("angkorId", params.get("angkorId"));

						// 번호 통일 - ex) 821012341234, 01012341234, 8201012341234 모두 같은 번호로 인식해야 함
						// case1 : 01012341234 -> 그대로 sql문
						// case2 : 821012341234-> 82를 제외하고 0을 붙여줌
						// case3 : 8201012341234 일경우 - 82를 제외함
						// case4 : 1043536236 일경우 - 0 붙여줌
						String phone = data.getFriend_phonenumber().replace("-", "");
						if (phone.startsWith("82")) {
							String prefixToRemove = "82";
							phone = phone.replace(prefixToRemove, "");
							params.put("friend_phonenumber", phone);
						}

						if (phone.startsWith("10")) {
							phone = "0" + phone;
						}
						fData.put("friend_phonenumber", phone);

						if (phone != null) {
							uniqueNumbers.add(phone);
						}

						int check = dao.checkBlockByFriendPhoneNumber(fData);
						if (check == 0) {
							int count;
							count = dao.insertAllMyPhoneAddrFriends(fData);
							cnt += count;
						}
					}
				}

				// 3. 나의 친구 목록 불러오기
				List<String> myFriendList = dao.getMyFriendPhoneNumberList(params);
				List<String> uniqueStrings = new ArrayList<>();

				// myFriendList에는 있지만 uniqueNumbers에 없는 문자열을 찾음
				for (String str : myFriendList) {
					if (!uniqueNumbers.contains(str)) {
						uniqueStrings.add(str);
					}
				}

				// 나의 휴대폰 주소록에 없는 친구는 친구가 설정한 이름으로 변경 처리
				for (String friend_phonenumber : uniqueStrings) {
					String friend_angkorId;
					String[] fList = dao.getAngkorIdByPhone(friend_phonenumber);
					if(fList.length>0){
						if(fList.length>1){
							HashMap fMap = new HashMap();
							fMap.put("angkorId", params.get("angkorId"));
							fMap.put("friend_phonenumber", friend_phonenumber);
							friend_angkorId = dao.selectOneAngkorIdByPhone(fMap);
						}else{
							friend_angkorId = fList[0];
						}

						if(!StringUtils.isBlank(friend_angkorId)){
							dao.updateFriendNameToBasic(friend_angkorId, (String) params.get("angkorId"));
						}
					}
				}

				return CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
			}else{
				return CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.INVALID_USER.getErrorcode()+"",CommonErrorCode.INVALID_USER.getGmessage()+" : "+CommonErrorCode.INVALID_USER.getDmessage());
			}
		} catch (Exception e) {
			return CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResTrDTO dtAddress(HttpServletRequest request, HashMap params, String friendAngkorId) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		if (StringUtils.isBlank((String)params.get("angkorId")) || StringUtils.isBlank(friendAngkorId)) {
			return CommonUtil.setResponseTrObject(null, 0,CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		int cnt=0;

		try {
			// 1. 유효한 아이디인지 확인
			int isUser = dao.checkMyAngKorId(params);
			if(isUser>0) {
				// 2. 친구 삭제
				params.put("friend_angkorId", friendAngkorId);
				cnt = dao.deleteFriend(params);
				dao.deleteFavorite(params);
				return CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
			}else{
				return CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.INVALID_USER.getErrorcode()+"",CommonErrorCode.INVALID_USER.getGmessage()+" : "+CommonErrorCode.INVALID_USER.getDmessage());
			}
		} catch (Exception e) {
			return CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResTrDTO getProfilesImage(HttpServletRequest request, HashMap params, String friendAngkorId) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		if (StringUtils.isBlank((String)params.get("angkorId"))) {
			return CommonUtil.setResponseTrObject(null, 0,CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		try {
			// 1. 유효한 아이디인지 확인
			int isUser = dao.checkMyAngKorId(params);
			if(isUser>0) {
				List<HashMap> list = dao.getProfilesImage(params);
				// 친구의 프로필을 조회하는 경우
				if(!StringUtils.isBlank(friendAngkorId)){
					params.put("friendAngkorId", friendAngkorId);
					int result = dao.getFriendPrivacySetting(params);

					HashMap privacyPhotoOption = new HashMap<>();
					privacyPhotoOption.put("privacyProfile", false);

					// result 1 : Everybody => 프로필 모두 공개
					// result 2 : My Friends && 상대방이 나를 친구로 등록했을 경우 => 프로필 친구에게만 공개
					// result 3 : Nobody => 프로필 비공개
					if(result>1){
						if(result>2){
							list = new ArrayList<>();
							privacyPhotoOption.put("privacyProfile", true);
						}else{
							list = dao.getFriendProfilesImage(params);
						}
					}else{
						list = dao.getFriendProfilesImage(params);
					}
					list.add(privacyPhotoOption);
				}
				return CommonUtil.setResponseTrObject(list, list.size(), CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
			}else{
				return CommonUtil.setResponseTrObject(null,0,CommonErrorCode.INVALID_USER.getErrorcode()+"",CommonErrorCode.INVALID_USER.getGmessage()+" : "+CommonErrorCode.INVALID_USER.getDmessage());
			}
		} catch (Exception e) {
			return CommonUtil.setResponseTrObject(null,0,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResCommDTO getAddressListNearBy(HttpServletRequest request, HashMap params) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseObject(null, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		if (StringUtils.isBlank((String)params.get("angkorId"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		try {
			// 1. 유효한 아이디인지 확인
			int isUser = dao.checkMyAngKorId(params);
			if(isUser>0) {
				// 2. 주변 1km 앙코르챗 사용자 list 검색(본인 제외)
				HashMap myPosition = dao.getMyPosition(params);
				if (myPosition == null){
					return CommonUtil.setResponseObject(null,CommonErrorCode.LOCATION_NOT_FOUND.getErrorcode()+"",CommonErrorCode.LOCATION_NOT_FOUND.getGmessage()+" : "+CommonErrorCode.LOCATION_NOT_FOUND.getDmessage());
				}else {
					params.put("latitude", myPosition.get("latitude"));
					params.put("longitude", myPosition.get("longitude"));

					List<HashMap> list = dao.getAddressListNearBy(params);
					List<HashMap<String, Object>> removeList = new ArrayList<>();

					for(HashMap map : list) {

						HashMap lMap = new HashMap();
						lMap.put("angkorId", params.get("angkorId"));
						lMap.put("friend_angkorId", map.get("friend_angkorId"));

						// 내가 차단한 친구 or 나와 이미 친구인 경우 list 제외
						HashMap deleteCase1 = dao.getBlockFriend(lMap);
						HashMap deleteCase2 = dao.getMyFriend(lMap);

						if(deleteCase1!=null || deleteCase2!=null){
							removeList.add(map);
						}
					}
					list.removeIf(map -> removeList.stream().anyMatch(map::equals));
					return CommonUtil.setResponseObject(list,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
				}
			}else{
				return CommonUtil.setResponseObject(null,CommonErrorCode.INVALID_USER.getErrorcode()+"",CommonErrorCode.INVALID_USER.getGmessage()+" : "+CommonErrorCode.INVALID_USER.getDmessage());
			}
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResCommDTO getAddressInfoByPhone(HttpServletRequest request, HashMap params, String phoneNumber) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseObject(null, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		if (StringUtils.isBlank((String)params.get("angkorId")) || StringUtils.isBlank(phoneNumber)) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		try {
			// 1. 유효한 전화번호인지 확인
			params.put("phoneNumber", phoneNumber);
			int isUser = dao.searchUserByPhoneNumber(params);
			if(isUser>0) {
				int allowOthersToN = dao.checkAllowOthers(params);
				if(allowOthersToN==0){
					if(request.getHeader("Language").equals("en")){
						return CommonUtil.setResponseObject(null,CommonErrorCode.SEARCH_UNAVAILABLE.getErrorcode()+"",CommonErrorCode.SEARCH_UNAVAILABLE.getGmessage());
					}else{
						return CommonUtil.setResponseObject(null,CommonErrorKhmerCode.SEARCH_UNAVAILABLE.getErrorcode()+"",CommonErrorKhmerCode.SEARCH_UNAVAILABLE.getGmessage());
					}
				}
				HashMap data = dao.getAddressInfoByPhone(params);
				return CommonUtil.setResponseObject(data,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
			}else{
				HashMap data = new HashMap();
				// invite link
				String inviteUrl = GlobalObjects.getAapiUrl() + "invite";
				data.put("inviteUrl", inviteUrl);
				return CommonUtil.setResponseObject(data,CommonErrorCode.INVALID_USER.getErrorcode()+"",CommonErrorCode.INVALID_USER.getGmessage()+" : "+CommonErrorCode.INVALID_USER.getDmessage());
			}
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResCommDTO getSuggestAddressList(HttpServletRequest request, HashMap params) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseObject(null, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		if (StringUtils.isBlank((String)params.get("angkorId"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		try {
			// 1. 유효한 아이디인지 확인
			int isUser = dao.checkMyAngKorId(params);
			if(isUser>0) {
				int recommendYn = dao.checkRecommendYn(params);
				if(recommendYn>0){
					// 2. 내가 친구로 등록한 사용자 리스트
					List<HashMap> myFriendList = dao.getMyFriendList(params);
					// 3. 상대방이 나를 친구로 등록한 사용자 리스트
					List<HashMap> addedMeFriendList = dao.getAddedMeFriendList(params);
					// 4. 내가 차단한 친구 리스트
					List<HashMap> blockFriendList = dao.getBlockFriendList(params);
					// 5. 나는 친구 추가하지 않았지만 상대방은 나를 친구로 추가한 사용자 리스트 추출 (단, 내가 차단한 친구와 친구추천 토글 off한 유저는 제외)
					List<HashMap<String, Object>> filteredList = addedMeFriendList.stream()
							.filter(map -> map.containsKey("friend_angkorId")
									&& myFriendList.stream().noneMatch(friendMap -> friendMap.containsKey("friend_angkorId") && friendMap.get("friend_angkorId").equals(map.get("friend_angkorId")))
									&& blockFriendList.stream().noneMatch(blockMap -> blockMap.containsKey("friend_angkorId") && blockMap.get("friend_angkorId").equals(map.get("friend_angkorId"))))
							.map(map -> new HashMap<String, Object>(map))
							.collect(Collectors.toList());
					return CommonUtil.setResponseObject(filteredList,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
				}else{
					return CommonUtil.setResponseObject(new ArrayList<>(),CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
				}
			}else{
				return CommonUtil.setResponseObject(null,CommonErrorCode.INVALID_USER.getErrorcode()+"",CommonErrorCode.INVALID_USER.getGmessage()+" : "+CommonErrorCode.INVALID_USER.getDmessage());
			}
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResCommDTO getBlockAddressList(HttpServletRequest request, HashMap params) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseObject(null, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		if (StringUtils.isBlank((String)params.get("angkorId"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		try {
			// 1. 유효한 아이디인지 확인
			int isUser = dao.checkMyAngKorId(params);
			if(isUser>0) {
				List<HashMap> list = dao.getBlockAddressList(params);
				return CommonUtil.setResponseObject(list,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
			}else{
				return CommonUtil.setResponseObject(null,CommonErrorCode.INVALID_USER.getErrorcode()+"",CommonErrorCode.INVALID_USER.getGmessage()+" : "+CommonErrorCode.INVALID_USER.getDmessage());
			}
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResCommDTO checkIsNotUserAddressList(HttpServletRequest request, HashMap params) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseObject(null, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		if (StringUtils.isBlank((String)params.get("angkorId"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		try {
			String[] aList = (String[]) params.get("friendList");
			if(aList.length==0){
				HashMap resMap = new HashMap();
				resMap.put("isNotUserList", aList);
				// invite link
				String inviteUrl = GlobalObjects.getAapiUrl() + "invite";
				resMap.put("inviteUrl", inviteUrl);
				return CommonUtil.setResponseObject(resMap, CommonErrorCode.SUCCESS.getErrorcode() + "", CommonErrorCode.SUCCESS.getGmessage() + " : " + CommonErrorCode.SUCCESS.getDmessage());
			}else {
				// 1. 유효한 아이디인지 확인
				HashMap map = new HashMap<>();
				map.put("angkorId", params.get("angkorId"));

				int isUser = dao.checkMyAngKorId(map);
				if (isUser > 0) {
					// aList 중복 제거
					Set<String> set = new HashSet<>();
                    for (String element : aList) { set.add(element); }

					// Set을 배열로 변환
					String[] result = new String[set.size()];
					set.toArray(result);
					aList = result;

					// 중복제거된 aList 다시 friendList에 put
					params.put("friendList", aList);

					// 2. 친구 목록 중 앙코르챗 회원인 친구 목록 리스트 확인
					String[] resList = dao.checkUserAddressList(params);

					// 중복되지 않은 값을 저장할 리스트
					List<String> uniqueValues = new ArrayList<>();

					// resList 값들을 중복 체크를 위한 Set에 저장
					Set<String> setB = new HashSet<>();
					for (String value : resList) {
						setB.add(value);
					}

					// aList 값들 중에서 resList에 없는 값을 uniqueValues 리스트에 추가
					for (String value : aList) {
						if (!setB.contains(value)) {
							uniqueValues.add(value);
						}
					}

					// uniqueValues에서 빈 문자열 제외 시킴
					List<String> nonEmptyValues = new ArrayList<>();
					for (String value : uniqueValues) {
						if (!value.isEmpty()) {
							nonEmptyValues.add(value);
						}
					}

					HashMap resMap = new HashMap();
					resMap.put("isNotUserList", nonEmptyValues);
					// invite link
					String inviteUrl = GlobalObjects.getInviteurl();
					resMap.put("inviteUrl", inviteUrl);

					return CommonUtil.setResponseObject(resMap, CommonErrorCode.SUCCESS.getErrorcode() + "", CommonErrorCode.SUCCESS.getGmessage() + " : " + CommonErrorCode.SUCCESS.getDmessage());
				} else {
					return CommonUtil.setResponseObject(null, CommonErrorCode.INVALID_USER.getErrorcode() + "", CommonErrorCode.INVALID_USER.getGmessage() + " : " + CommonErrorCode.INVALID_USER.getDmessage());
				}
			}
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResCommDTO getMyQrCode(HttpServletRequest request, HashMap params) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseObject(null, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		if (StringUtils.isBlank((String)params.get("angkorId"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		try {
			HashMap response = new HashMap();

			// 1. 유효한 아이디인지 확인
			int isUser = dao.checkMyAngKorId(params);
			if(isUser>0) {
				// 2. QR 코드 생성
				// 내 아이디 가져오기
				HashMap map = dao.getMyId(params);

				/*String qrCodeImage;
				if(map.get("qrcodeUrl")==null){
					qrCodeImage = QRCodeGenerator.generateQRCode((String) map.get("userId"));
					if(qrCodeImage!=null){
						// insert my qr code
						HashMap iMap = new HashMap();
						iMap.put("userId", map.get("userId"));
						iMap.put("qrcodeUrl", map.get("userId")+".png");
						dao.insertMyQrCode(iMap);
					}
				}else{
					qrCodeImage = (String) map.get("qrcodeUrl");
				}

				log.info(">>>>>>>>>>>" + qrCodeImage);

				String qrUrl = GlobalObjects.getAapiUrl() + "upload/qrcode/" + map.get("userId") + ".png";
				response.put("QRcodeUrl", qrUrl);*/

				MultipartFile qrCodeImageFile;
				String url = null;
				if(map.get("qrcodeUrl")==null){
					qrCodeImageFile = QRCodeGenerator.generateQRCodeToS3((String) map.get("userId"));
					if(qrCodeImageFile!=null){

						url = s3Service.save(qrCodeImageFile, "upload/qrcode"); // 디렉터리명
						log.info("s3 url : " + url);

						// insert my qr code
						HashMap iMap = new HashMap();
						iMap.put("userId", map.get("userId"));
						iMap.put("qrcodeUrl", url);
						dao.insertMyQrCode(iMap);
					}
					response.put("QRcodeUrl", url);
				}else{
					response.put("QRcodeUrl", map.get("qrcodeUrl"));
				}
				return CommonUtil.setResponseObject(response,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
			}else{
				return CommonUtil.setResponseObject(null,CommonErrorCode.INVALID_USER.getErrorcode()+"",CommonErrorCode.INVALID_USER.getGmessage()+" : "+CommonErrorCode.INVALID_USER.getDmessage());
			}
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResCommDTO getFriendAddrByQrScan(HttpServletRequest request, HashMap params, String friendUserId) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseObject(null, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		if (StringUtils.isBlank((String)params.get("angkorId")) || StringUtils.isBlank(friendUserId)) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		try {
			// 1. 유효한 아이디인지 확인
			HashMap map = dao.getFriendYnByUserId(params, friendUserId);
			if(map==null){
				if(request.getHeader("Language").equals("en")){
					return CommonUtil.setResponseObject(null,CommonErrorCode.QR_SCAN_NO_FOUND.getErrorcode() +"",CommonErrorCode.QR_SCAN_NO_FOUND.getGmessage());
				}else{
					return CommonUtil.setResponseObject(null,CommonErrorKhmerCode.QR_SCAN_NO_FOUND.getErrorcode() +"",CommonErrorKhmerCode.QR_SCAN_NO_FOUND.getGmessage());
				}
			}

			if(map!=null && map.get("friend_angkorId").equals(params.get("angkorId"))){
				// 상대방 앙코르챗 id와 내 id가 같을 경우 예외 처리
				map.put("friend_yn", "Y");
			}
			return CommonUtil.setResponseObject(map,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}
}
