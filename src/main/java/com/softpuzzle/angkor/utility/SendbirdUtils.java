package com.softpuzzle.angkor.utility;

import com.softpuzzle.angkor.gcache.GlobalObjects;
import com.softpuzzle.angkor.http.response.common.ResCommDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("SendbirdUtils")
@Slf4j
public class SendbirdUtils {

    public static String updateUserBySendbird(HashMap params, MultipartFile profileUrl) throws IOException {
        // case : sendbird에 file이 포함된 경우
        String url = "https://api-" + GlobalObjects.getAppKey() +".sendbird.com/v3/users/" + params.get("angkorId");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Api-Token", GlobalObjects.getAppToken());
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        if(profileUrl!=null) {
            body.add("profile_file", new ByteArrayResource(profileUrl.getBytes()) {
                @Override
                public String getFilename() {
                    return profileUrl.getOriginalFilename();
                }
            });
        }else{
            body.add("profile_file", "");
        }
        body.add("nickname", params.get("userName"));

        HttpEntity<MultiValueMap<String, Object>> req = new HttpEntity<>(body, headers);

        ResCommDTO response = RestTemplateUtils.putRequestMultiSendbird(url, req);

        HashMap profile = (HashMap) response.getData();

        String profile_url = (String) profile.get("profile_url");

        return profile_url;

    }

    public static void updateUserMetaDataBySendbird(HashMap params) throws IOException {

        String url = "https://api-" + GlobalObjects.getAppKey() +".sendbird.com/v3/users/" + params.get("angkorId") + "/metadata";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Api-Token", GlobalObjects.getAppToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        // metadate upsert - phoneCode, phoneNumber
        HashMap sMap = new HashMap<>();
        if(!StringUtils.isBlank((String) params.get("phoneCode"))){
            sMap.put("phoneCode", params.get("phoneCode"));
        }
        if(!StringUtils.isBlank((String) params.get("phoneNumber"))){
            sMap.put("phoneNumber", params.get("phoneNumber"));
        }
        sMap.put("inOnlineRange", params.get("inOnlineRange"));
        sMap.put("inPhotoRange", params.get("inPhotoRange"));

        HashMap reqMap = new HashMap();

        reqMap.put("metadata", sMap);
        reqMap.put("upsert", true);

        HttpEntity<HashMap> req = new HttpEntity<>(reqMap, headers);
        RestTemplateUtils.putRequestImageSendbird(url, req);

    }

    public static void deleteUserBySendbird(HashMap params) {
        // case : sendbird에 file이 포함된 경우
        String url = "https://api-" + GlobalObjects.getAppKey() +".sendbird.com/v3/users/" + params.get("angkorId");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Api-Token", GlobalObjects.getAppToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        params.put("user_id", params.get("angkorId"));
        HttpEntity<HashMap> req = new HttpEntity<>(params, headers);
        RestTemplateUtils.deleteRequestSendbird(url, req);
    }

    public static String updateImageBySendbird(HashMap params) {
        // case : sendbird에 file이 포함되지 않는 경우
        String url = "https://api-" + GlobalObjects.getAppKey() +".sendbird.com/v3/users/" + params.get("angkorId");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Api-Token", GlobalObjects.getAppToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 닉네임 update 처리
        params.put("nickname", params.get("userName"));
        HttpEntity<HashMap> req = new HttpEntity<>(params, headers);
        ResCommDTO response = RestTemplateUtils.putRequestImageSendbird(url, req);

        HashMap profile = (HashMap) response.getData();

        String profile_url = (String) profile.get("profile_url");

        return profile_url;

    }

    public static HashMap getDailyUsers(HashMap params) {

        String url = "https://api-" + GlobalObjects.getAppKey() +".sendbird.com/v3/applications/dau?date=" + params.get("date");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Api-Token", GlobalObjects.getAppToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<HashMap> req = new HttpEntity<>(null, headers);
        ResCommDTO response = RestTemplateUtils.getRequestSendbird(url, req);

        HashMap resMap = (HashMap) response.getData();

        return resMap;

    }
    
    public static HashMap getMonthUsers(HashMap params) {

        String url = "https://api-" + GlobalObjects.getAppKey() +".sendbird.com/v3/applications/mau?date=" + params.get("date");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Api-Token", GlobalObjects.getAppToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<HashMap> req = new HttpEntity<>(null, headers);
        ResCommDTO response = RestTemplateUtils.getRequestSendbird(url, req);

        HashMap resMap = (HashMap) response.getData();

        return resMap;

    }

    public static String updateBlockUser(HashMap params) {

        String url = "https://api-" + GlobalObjects.getAppKey() +".sendbird.com/v3/users/" + params.get("angkorId") + "/block";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Api-Token", GlobalObjects.getAppToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HashMap map = new HashMap();
        map.put("target_id", params.get("friend_angkorId"));
        HttpEntity<HashMap> req = new HttpEntity<>(map, headers);
        ResCommDTO response = RestTemplateUtils.postRequestSendbird(url, req);

        HashMap resMap = (HashMap) response.getData();

        String blockUserId = (String) resMap.get("user_id");

        return blockUserId;

    }

    public static Boolean updateUnBlockUser(HashMap params) {

        String url = "https://api-" + GlobalObjects.getAppKey() +".sendbird.com/v3/users/" + params.get("angkorId") + "/block/" + params.get("friend_angkorId");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Api-Token", GlobalObjects.getAppToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HashMap map = new HashMap();
        map.put("target_id", params.get("friend_angkorId"));
        HttpEntity<HashMap> req = new HttpEntity<>(map, headers);
        ResCommDTO response = RestTemplateUtils.deleteRequestSendbird(url, req);

        if (response.getCode().equals("0")) return true;
        else return false;
    }

    public static HashMap getDirectCallsLogByCallId(HashMap params) {

        String url = "https://api-" + GlobalObjects.getAppKey() +".calls.sendbird.com/v1/direct_calls/" + params.get("call_id");
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Api-Token", GlobalObjects.getAppToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<HashMap> req = new HttpEntity<>(null, headers);
        ResCommDTO response = RestTemplateUtils.getRequestSendbird(url, req);

        HashMap resMap = (HashMap) response.getData();

        return resMap;

    }
    

    
    public static HashMap getDirectCallsLogByParams(HashMap params) {
    	
    	String param = "";
        param += "next="+StringUtil.nTs((String) params.get("next"));
        param += "&limit="+StringUtil.nTs((String) params.get("limit"));
        param += "&call_ts="+StringUtil.nTs((String) params.get("call_ts"));
        param += "&search_scope_from_call_ts="+StringUtil.nTs((String) params.get("search_scope_from_call_ts"));
        param += "&end_result="+StringUtil.nTs((String) params.get("end_result"));
        param += "&duration_range_min="+StringUtil.nTs((String) params.get("duration_range_min"));
        param += "&duration_range_max="+StringUtil.nTs((String) params.get("duration_range_max"));
        

        String url = "https://api-" + GlobalObjects.getAppKey() +".calls.sendbird.com/v1/direct_calls?"+param;
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Api-Token", GlobalObjects.getAppToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<HashMap> req = new HttpEntity<>(null, headers);
        ResCommDTO response = RestTemplateUtils.getRequestSendbird(url, req);

        HashMap resMap = (HashMap) response.getData();

        return resMap;

    }
    
    public static HashMap getUserPushInfo(JSONObject params) {

    	// token_type : gcm, huawei, or apns
        String url = "https://api-" + GlobalObjects.getAppKey() +".sendbird.com/v3/users/" + params.get("user_id")+"/push/"+params.get("token_type");
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Api-Token", GlobalObjects.getAppToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<HashMap> req = new HttpEntity<>(null, headers);
        ResCommDTO response = RestTemplateUtils.getRequestSendbird(url, req);

        HashMap resMap = (HashMap) response.getData();

        return resMap;

    }
}
