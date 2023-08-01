package com.softpuzzle.angkor.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softpuzzle.angkor.http.response.common.ResCommDTO;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Service("RestTemplateUtils")
@Slf4j
public class RestTemplateUtils {

    public static ResCommDTO getRequest(String url, HttpEntity<String> request) {

        JSONObject jsonObject;
        RestTemplate restTemplate = new RestTemplate();
        String language = String.valueOf(request.getHeaders().getContentLanguage());
        try {
            ResponseEntity<HashMap> response = restTemplate.exchange(url, HttpMethod.GET, request, HashMap.class);

            HashMap responseBody = response.getBody();

            if(responseBody.size() == 2 && (responseBody.containsKey("code") && responseBody.containsKey("message"))){
                return CommonUtil.setResponseObject(null, CommonErrorCode.SUCCESS.getErrorcode() + "", CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
            }else{
                return CommonUtil.setResponseObject(response.getBody(), CommonErrorCode.SUCCESS.getErrorcode() + "", CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
            }
        } catch (RestClientResponseException e) {
            String responseBody = e.getResponseBodyAsString();
            jsonObject = new JSONObject(responseBody.toString());

            String apiErrorCode = "API_" + jsonObject.get("code");
            if(language.equals("en")){
                if(!CommonErrorCode.valueOf(apiErrorCode).getDmessage().isEmpty()){
                    return CommonUtil.setResponseObject(null, CommonErrorCode.valueOf(apiErrorCode).getErrorcode() + "", CommonErrorCode.valueOf(apiErrorCode).getGmessage() + " : " + CommonErrorCode.valueOf(apiErrorCode).getDmessage());
                }else{
                    return CommonUtil.setResponseObject(null, CommonErrorCode.valueOf(apiErrorCode).getErrorcode() + "", CommonErrorCode.valueOf(apiErrorCode).getGmessage());
                }
            }else{
                if(!CommonErrorKhmerCode.valueOf(apiErrorCode).getDmessage().isEmpty()){
                    return CommonUtil.setResponseObject(null, CommonErrorKhmerCode.valueOf(apiErrorCode).getErrorcode() + "", CommonErrorKhmerCode.valueOf(apiErrorCode).getGmessage() + " : " + CommonErrorKhmerCode.valueOf(apiErrorCode).getDmessage());
                }else{
                    return CommonUtil.setResponseObject(null, CommonErrorKhmerCode.valueOf(apiErrorCode).getErrorcode() + "", CommonErrorKhmerCode.valueOf(apiErrorCode).getGmessage());
                }
            }
        }
    }


    public static ResCommDTO postRequest(String url, HttpEntity<HashMap> request) {

        JSONObject jsonObject;
        RestTemplate restTemplate = new RestTemplate();
        String language = String.valueOf(request.getHeaders().getContentLanguage());
        try {
            ResponseEntity<HashMap> response = restTemplate.exchange(url, HttpMethod.POST, request, HashMap.class);

            HashMap responseBody = response.getBody();

            if(responseBody.size() == 2 && (responseBody.containsKey("code") && responseBody.containsKey("message"))){
                return CommonUtil.setResponseObject(null, CommonErrorCode.SUCCESS.getErrorcode() + "", CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
            }else{
                return CommonUtil.setResponseObject(response.getBody(), CommonErrorCode.SUCCESS.getErrorcode() + "", CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
            }
        } catch (RestClientResponseException e) {
            String responseBody = e.getResponseBodyAsString();
            jsonObject = new JSONObject(responseBody.toString());

            String apiErrorCode = "API_" + jsonObject.get("code");
            if(language.equals("en")){
                if(!CommonErrorCode.valueOf(apiErrorCode).getDmessage().isEmpty()){
                    return CommonUtil.setResponseObject(null, CommonErrorCode.valueOf(apiErrorCode).getErrorcode() + "", CommonErrorCode.valueOf(apiErrorCode).getGmessage() + " : " + CommonErrorCode.valueOf(apiErrorCode).getDmessage());
                }else{
                    return CommonUtil.setResponseObject(null, CommonErrorCode.valueOf(apiErrorCode).getErrorcode() + "", CommonErrorCode.valueOf(apiErrorCode).getGmessage());
                }
            }else{
                if(!CommonErrorKhmerCode.valueOf(apiErrorCode).getDmessage().isEmpty()){
                    return CommonUtil.setResponseObject(null, CommonErrorKhmerCode.valueOf(apiErrorCode).getErrorcode() + "", CommonErrorKhmerCode.valueOf(apiErrorCode).getGmessage() + " : " + CommonErrorKhmerCode.valueOf(apiErrorCode).getDmessage());
                }else{
                    return CommonUtil.setResponseObject(null, CommonErrorKhmerCode.valueOf(apiErrorCode).getErrorcode() + "", CommonErrorKhmerCode.valueOf(apiErrorCode).getGmessage());
                }
            }
        }
    }

    public static ResCommDTO deleteRequest(String url, HttpEntity<HashMap> request) {

        JSONObject jsonObject;
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);
            String responseBody = response.getBody(); // 회원 삭제할 때는 getBody 에 아무것도 없음 - getBody가 필요하다면 분기 처리 필요
            return CommonUtil.setResponseObject(null, CommonErrorCode.SUCCESS.getErrorcode() + "", CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
        } catch (RestClientResponseException e) {
            String responseBody = e.getResponseBodyAsString();
            jsonObject = new JSONObject(responseBody.toString());

            String apiErrorCode = "API_" + jsonObject.get("code");
            if(!CommonErrorCode.valueOf(apiErrorCode).getDmessage().isEmpty()){
                return CommonUtil.setResponseObject(null, CommonErrorCode.valueOf(apiErrorCode).getErrorcode() + "", CommonErrorCode.valueOf(apiErrorCode).getGmessage() + " : " + CommonErrorCode.valueOf(apiErrorCode).getDmessage());
            }else{
                return CommonUtil.setResponseObject(null, CommonErrorCode.valueOf(apiErrorCode).getErrorcode() + "", CommonErrorCode.valueOf(apiErrorCode).getGmessage());
            }
        }
    }

    public static ResCommDTO putRequestMultiSendbird(String url, HttpEntity<MultiValueMap<String, Object>> request) {

        JSONObject jsonObject;
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<HashMap> response = restTemplate.exchange(url, HttpMethod.PUT, request, HashMap.class);
            HashMap responseBody = response.getBody();

            return CommonUtil.setResponseObject(responseBody, CommonErrorCode.SUCCESS.getErrorcode() + "", CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
        } catch (RestClientResponseException e) {
            return CommonUtil.setResponseObject(null, CommonErrorCode.SEND_API_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.SEND_API_SERVER_ERROR.getGmessage());
        }
    }

    public static ResCommDTO putRequestImageSendbird(String url, HttpEntity<HashMap> request) {

        JSONObject jsonObject;
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<HashMap> response = restTemplate.exchange(url, HttpMethod.PUT, request, HashMap.class);
            HashMap responseBody = response.getBody();

            return CommonUtil.setResponseObject(responseBody, CommonErrorCode.SUCCESS.getErrorcode() + "", CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
        } catch (RestClientResponseException e) {
            return CommonUtil.setResponseObject(null, CommonErrorCode.SEND_API_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.SEND_API_SERVER_ERROR.getGmessage());
        }
    }

    public static ResCommDTO getRequestSendbird(String url, HttpEntity<HashMap> request) {

        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<HashMap> response = restTemplate.exchange(url, HttpMethod.GET, request, HashMap.class);
            HashMap responseBody = response.getBody();

            return CommonUtil.setResponseObject(responseBody, CommonErrorCode.SUCCESS.getErrorcode() + "", CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
        } catch (RestClientResponseException e) {
            return CommonUtil.setResponseObject(null, CommonErrorCode.SEND_API_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.SEND_API_SERVER_ERROR.getGmessage());
        }
    }

    public static ResCommDTO postRequestSendbird(String url, HttpEntity<HashMap> request) {

        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<HashMap> response = restTemplate.exchange(url, HttpMethod.POST, request, HashMap.class);
            return CommonUtil.setResponseObject(response.getBody(), CommonErrorCode.SUCCESS.getErrorcode() + "", CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
        } catch (RestClientResponseException e) {
            return CommonUtil.setResponseObject(null, CommonErrorCode.SEND_API_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.SEND_API_SERVER_ERROR.getGmessage());
        }
    }

    public static ResCommDTO deleteRequestSendbird(String url, HttpEntity<HashMap> request) {

        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<HashMap> response = restTemplate.exchange(url, HttpMethod.DELETE, request, HashMap.class);
            return CommonUtil.setResponseObject(response.getBody(), CommonErrorCode.SUCCESS.getErrorcode() + "", CommonErrorCode.SUCCESS.getGmessage()+" : "+CommonErrorCode.SUCCESS.getDmessage());
        } catch (RestClientResponseException e) {
            return CommonUtil.setResponseObject(null, CommonErrorCode.SEND_API_SERVER_ERROR.getErrorcode() + "", CommonErrorCode.SEND_API_SERVER_ERROR.getGmessage());
        }
    }
}
