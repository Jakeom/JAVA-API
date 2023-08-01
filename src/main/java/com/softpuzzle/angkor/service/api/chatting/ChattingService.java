package com.softpuzzle.angkor.service.api.chatting;

import com.softpuzzle.angkor.database.mapper.api.chatting.ChattingMapper;
import com.softpuzzle.angkor.gcache.GlobalObjects;
import com.softpuzzle.angkor.http.response.common.ResCommDTO;
import com.softpuzzle.angkor.http.response.common.ResTrDTO;
import com.softpuzzle.angkor.service.api.cert.CertService;
import com.softpuzzle.angkor.utility.CommonErrorCode;
import com.softpuzzle.angkor.utility.CommonUtil;
import com.softpuzzle.angkor.utility.RSAEncryptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChattingService {
	@Autowired
	private CertService certService;
	private final ChattingMapper dao;

	public ResCommDTO getChatting(HttpServletRequest request, HashMap params) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseObject(null, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		HashMap map;
		ResCommDTO res;

		try {
			map = dao.getChatting(params);

			res = CommonUtil.setResponseObject(map,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
		} catch ( Exception e ) {
			res = CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode()+"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}

		return res;
	}

	public ResTrDTO setChatting(HttpServletRequest request, HashMap params) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		ResTrDTO res;
		int cnt = 0;

		try {

			cnt = dao.updateChatting(params);

			if ( cnt > 0 ) {
				res = CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
			} else {
				res = CommonUtil.setResponseTrObject(null, cnt, CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage() + " : " + CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
			}

		} catch ( Exception e ) {
			res = CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}

		return res;
	}

	public ResTrDTO setChattingToImageQuality(HttpServletRequest request, HashMap params) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		if (!(params.get("inImageResolution").equals("D001") || params.get("inImageResolution").equals("D002") || params.get("inImageResolution").equals("D003"))) {
			return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.INVALID_PARAMETER.getErrorcode() + "", CommonErrorCode.INVALID_PARAMETER.getGmessage() + " : " + CommonErrorCode.INVALID_PARAMETER.getDmessage());
		}

		ResTrDTO res;
		int cnt = 0;

		try {
			cnt = dao.updateChattingToImageQuality(params);
			if ( cnt > 0 ) {
				res = CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
			} else {
				res = CommonUtil.setResponseTrObject(null, cnt, CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage() + " : " + CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
			}

		} catch ( Exception e ) {
			res = CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}

		return res;
	}

	public ResTrDTO setChattingToVideoQuality(HttpServletRequest request, HashMap params) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		if (!(params.get("inVideos").equals("D001") || params.get("inVideos").equals("D002") || params.get("inVideos").equals("D003"))) {
			return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.INVALID_PARAMETER.getErrorcode() + "", CommonErrorCode.INVALID_PARAMETER.getGmessage() + " : " + CommonErrorCode.INVALID_PARAMETER.getDmessage());
		}

		ResTrDTO res;
		int cnt = 0;

		try {
			cnt = dao.updateChattingToVideoQuality(params);
			if ( cnt > 0 ) {
				res = CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
			} else {
				res = CommonUtil.setResponseTrObject(null, cnt, CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage() + " : " + CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
			}

		} catch ( Exception e ) {
			res = CommonUtil.setResponseTrObject(null, cnt,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}

		return res;
	}

	public ResCommDTO getParentEmoticonList(HttpServletRequest request, HashMap params) {

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
				// 2. 기본 master 이모티콘 리스트 추출
				List<HashMap> list = dao.getParentEmoticonList(params);
				return CommonUtil.setResponseObject(list,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
			}else{
				return CommonUtil.setResponseObject(null,CommonErrorCode.INVALID_USER.getErrorcode()+"",CommonErrorCode.INVALID_USER.getGmessage()+" : "+CommonErrorCode.INVALID_USER.getDmessage());
			}
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResCommDTO getChildEmoticonList(HttpServletRequest request, HashMap params, String seq) {

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
				// 2. 기본 master 하위 이모티콘 리스트 추출
				if (StringUtils.isBlank(seq)) {
					// emoticon seq가 없을 경우 첫번째 이모티콘 리스트 불러오기
					HashMap map = dao.getParentEmoticon(params);
					params.put("seq", map.get("emoti_seq"));
				}else{
					params.put("seq", seq);
				}
				List<HashMap> list = dao.getChildEmoticonList(params);
				return CommonUtil.setResponseObject(list,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
			}else{
				return CommonUtil.setResponseObject(null,CommonErrorCode.INVALID_USER.getErrorcode()+"",CommonErrorCode.INVALID_USER.getGmessage()+" : "+CommonErrorCode.INVALID_USER.getDmessage());
			}
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResCommDTO getPublicKeyList(HttpServletRequest request, HashMap params) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if(uuidRes!=null){
			return CommonUtil.setResponseObject(null, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		if (StringUtils.isBlank((String)params.get("angkorId"))) {
			return CommonUtil.setResponseObject(null, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		try {

			// 각 유저의 공용키 가져오기
			String[] uList = (String[]) params.get("userList");
			List<HashMap> resList = new ArrayList<>();
			if (uList.length == 0) {
				return CommonUtil.setResponseObject(null,CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
			} else {
				for (String angkorId : uList) {
					if (!angkorId.isEmpty()) {
						// angkorId로 db user 조회하여 유저의 공유키(공개키) 가져옴
						HashMap pMap = dao.getPublicKeyAndInfoOfUser(angkorId);
						if(pMap!=null) {
							String pKey = (String) pMap.get("public_key");
							if (StringUtils.isBlank(pKey)) {
								return CommonUtil.setResponseObject(null, CommonErrorCode.INVALID_PUBLIC_KEY.getErrorcode() + "", CommonErrorCode.INVALID_PUBLIC_KEY.getGmessage() + " : " + CommonErrorCode.INVALID_PUBLIC_KEY.getDmessage());
							}
							// 개행 문자 제거
							String publicKey = pKey.replaceAll("\\s", "");

							HashMap<String, Object> map = new HashMap<>();
							map.put("angkorId", angkorId);
							map.put("publicKey", publicKey);
							map.put("nickname", pMap.get("nickname"));
							map.put("profileUrl", pMap.get("profileUrl"));

							resList.add(map);
						}
					}
				}
				return CommonUtil.setResponseObject(resList,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
			}
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResTrDTO setSecertKey(HttpServletRequest request, HashMap params) {

		// uuid valid check
		String uuidRes = certService.validUuid(request.getHeader("Uuid"), params);
		if (uuidRes != null) {
			return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.valueOf(uuidRes).getErrorcode() + "", CommonErrorCode.valueOf(uuidRes).getGmessage() + " : " + CommonErrorCode.valueOf(uuidRes).getDmessage());
		}

		if (StringUtils.isBlank((String) params.get("angkorId"))) {
			return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
		}

		Map<String, Object> response = new HashMap<>();
		try {
			// 그룹 공개키 생성
			Pair<String, String> keyPair = RSAEncryptionUtil.generateRSAKeyPair();
			String privateKey = keyPair.getFirst();
			String PublicKey = keyPair.getSecond();

			response.put("groupPublicKey", PublicKey);

			// 그룹 개인키 생성
			String[] uList = (String[]) params.get("userList");
			List<HashMap> resList = new ArrayList<>();
			if (uList.length == 0) {
				return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.NULL_PARAMETER.getErrorcode() + "", CommonErrorCode.NULL_PARAMETER.getGmessage() + " : " + CommonErrorCode.NULL_PARAMETER.getDmessage());
			} else {
				for (String angkorId : uList) {
					// angkorId로 db user 조회하여 유저의 공유키(공개키) 가져옴
					String pKey = dao.getPublicKeyOfUser(angkorId);
					if(StringUtils.isBlank(pKey)){
						return CommonUtil.setResponseTrObject(null,0,CommonErrorCode.INVALID_PUBLIC_KEY.getErrorcode()+"",CommonErrorCode.INVALID_PUBLIC_KEY.getGmessage()+" : "+CommonErrorCode.INVALID_PUBLIC_KEY.getDmessage());
					}
					// 개행 문자 제거
					String publicKey = pKey.replaceAll("\\s", "");

					String encryptedText = RSAEncryptionUtil.encryptStringRSA(publicKey, privateKey);

					HashMap<String, Object> map = new HashMap<>();
					map.put("angkorId", angkorId);
					map.put("privateKey", encryptedText);

					resList.add(map);
				}

				response.put("groupPrivateKey", resList);
				return CommonUtil.setResponseTrObject(response,1,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
			}
		} catch (Exception e) {
			return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage() + " : " + CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}

	public ResCommDTO getGroupList(HttpServletRequest request, HashMap params) {

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
				String[] uList = (String[]) params.get("userList");
				List<HashMap> resList = new ArrayList<>();
				for (String friendAngkorId : uList) {
					if (!friendAngkorId.isEmpty()) {
						// angkorId로 해당 유저의 privacy 세팅 조회
						HashMap map = new HashMap();
						map.put("angkorId", params.get("angkorId"));
						map.put("friendAngkorId", friendAngkorId);

						HashMap pMap = dao.getPrivacySettingUser(map);

						if(pMap!=null) {
							HashMap<String, Object> resMap = new HashMap<>();
							resMap.put("friendAngkorId", friendAngkorId);
							resMap.put("friend_group", pMap.get("friend_group"));
							resMap.put("friend_yn", pMap.get("friend_yn"));
							resMap.put("friend_block_yn", pMap.get("friend_block_yn"));

							resList.add(resMap);
						}
					}
				}
				return CommonUtil.setResponseObject(resList,CommonErrorCode.SUCCESS.getErrorcode()+"",CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
			}else{
				return CommonUtil.setResponseObject(null,CommonErrorCode.INVALID_USER.getErrorcode()+"",CommonErrorCode.INVALID_USER.getGmessage()+" : "+CommonErrorCode.INVALID_USER.getDmessage());
			}
		} catch (Exception e) {
			return CommonUtil.setResponseObject(null,CommonErrorCode.INTERNAL_SERVER_ERROR.getErrorcode() +"",CommonErrorCode.INTERNAL_SERVER_ERROR.getGmessage()+" : "+CommonErrorCode.INTERNAL_SERVER_ERROR.getDmessage());
		}
	}
}
