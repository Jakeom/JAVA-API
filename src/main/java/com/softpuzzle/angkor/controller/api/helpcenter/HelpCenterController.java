package com.softpuzzle.angkor.controller.api.helpcenter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softpuzzle.angkor.http.request.*;
import com.softpuzzle.angkor.http.response.common.ResCommDTO;
import com.softpuzzle.angkor.http.response.common.ResTrDTO;
import com.softpuzzle.angkor.service.api.helpcenter.HelpCenterService;
import com.softpuzzle.angkor.utility.CommonErrorCode;
import com.softpuzzle.angkor.utility.CommonUtil;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Api(tags = {"고객센터"}, description = "Help Center")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/ankochat/api/helpcenter")
public class HelpCenterController {
    @Autowired
    private HelpCenterService service;

    @ApiOperation(value = "공지사항 예정날짜 체크하여 게시 처리", notes = "cms에서 등록한 공지사항에 공지 예정날짜를 등록한 경우 체크하여 설정한 날짜에 게시처리 진행한다.", hidden = true)
    @PostMapping(value = "/notice/autoPublished")
    @Scheduled(cron = "0 * * * * ?", zone="Asia/Seoul")
    public ResTrDTO checkNoticeExpectDt() {

        ResTrDTO res  = service.checkNoticeExpectDt();
        log.info("res data ________________ :" + res.getData());
        return res;
    }

    @ApiOperation(value = "공지사항", notes = "등록되어 있는 공지사항 정보를 가져온다.")
    @GetMapping(value = "/notice/{AngkorId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Language", required = true, dataType = "string", paramType = "header", value = "selected language"),
            @ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
    })
    public ResCommDTO getNoticeList(HttpServletRequest request, @PathVariable(name = "AngkorId") String angkorId, @ApiParam(value = "request DTO", required = true) @ModelAttribute ORGReq037 rdto) {

        rdto.setAngkorId(angkorId);
        log.info("request params : ________________ :" +rdto.toString());
        ResCommDTO res  = service.getNoticeList(request, CommonUtil.Object2Hashmap(rdto));
        log.info("res data ________________ :" + res.getData());
        return res;
    }

    @ApiOperation(value = "FAQ 예정날짜 체크하여 게시 처리", notes = "cms에서 등록한 FAQ에 공지 예정날짜를 등록한 경우 체크하여 설정한 날짜에 게시처리 진행한다.", hidden = true)
    @PostMapping(value = "/faq/autoPublished")
    @Scheduled(cron = "0 * * * * ?", zone="Asia/Seoul")
    public ResTrDTO checkFaqExpectDt() {

        ResTrDTO res  = service.checkFaqExpectDt();
        log.info("res data ________________ :" + res.getData());
        return res;
    }

    @ApiOperation(value = "FAQ 카테고리 목록", notes = "등록되어 있는 faq 목록을 가져온다.")
    @GetMapping(value = "/faq/category/{AngkorId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Language", required = true, dataType = "string", paramType = "header", value = "selected language"),
            @ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
    })
    public ResCommDTO getFaqCategoryList(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq013 rdto) {

        log.info("request params : ________________ :" +rdto.toString());
        ResCommDTO res  = service.getFaqCategoryList(request, CommonUtil.Object2Hashmap(rdto));
        log.info("res data ________________ :" + res.getData());

        return res;
    }

    @ApiOperation(value = "FAQ 목록", notes = "등록되어 있는 faq 목록을 가져온다.\n"+
                                                "1. [fCategory 첫번째 카테고리]\n"+
                                                "FCategory = 1 -> Common\n"+
                                                "FCategory = 2 -> Android\n"+
                                                "FCategory = 3 -> iOS\n"+
                                                "2. [sCategory 두번째 카테고리]\n"+
                                                "sCategory = 1 -> General\n"+
                                                "sCategory = 2 -> Profile\n"+
                                                "sCategory = 3 -> New Device/Phone Number\n"+
                                                "sCategory = 4 -> Secret Chat\n"+
                                                "sCategory = 5 -> Voice Call/Face Call\n"+
                                                "sCategory = 6 -> Back up & Restore Chats\n"+
                                                "sCategory = 7 -> Report/Restrictions\n"+
                                                "sCategory = 8 -> Angkor Account")
    @GetMapping(value = "/faq/{AngkorId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Language", required = true, dataType = "string", paramType = "header", value = "selected language"),
            @ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
    })
    public ResCommDTO getFaqList(HttpServletRequest request, @PathVariable(name = "AngkorId") String angkorId, @ApiParam(value = "request DTO", required = true) @ModelAttribute ORGReq035 rdto) {

        rdto.setAngkorId(angkorId);
        log.info("request params : ________________ :" +rdto.toString());
        ResCommDTO res  = service.getFaqList(request, CommonUtil.Object2Hashmap(rdto));
        log.info("res data ________________ :" + res.getData());
        return res;
    }

    @ApiOperation(value = "FAQ 상세", notes = "선택한 faq의 상세 정보를 가져온다.")
    @GetMapping(value = "/faq/detail/{AngkorId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Language", required = true, dataType = "string", paramType = "header", value = "selected language"),
            @ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
    })
    public ResCommDTO getFaqDetail(HttpServletRequest request, @PathVariable(name = "AngkorId") String angkorId, @ApiParam(value = "request DTO", required = true) @ModelAttribute ORGReq036 rdto) {

        rdto.setAngkorId(angkorId);
        log.info("request params : ________________ :" +rdto.toString());
        ResCommDTO res  = service.getFaqDetail(request, CommonUtil.Object2Hashmap(rdto));
        log.info("res data ________________ :" + res.getData());

        HashMap mapRes = (HashMap) res.getData();
        if(mapRes!=null){
            res.setTotal(1);
        }
        return res;
    }

    @ApiOperation(value = "문의하기 등록", notes = "앙코르챗 사용자가 1:1 문의 신규 등록한다. \n" +
                                                  "files 첨부할 경우 아래와 같이 header 추가가 필요하다. header value filename에 파일명, 확장자명 입력\n" +
                                                  "파일 두개 이상 첨부할 경우 header Content-Disposition도 파일 개수만큼 설정해야 한다. \n" +
                                                  "header key: Content-Disposition, header value: form-data; name=\"files\"; filename=\"example1.jpg\"\n" +
                                                  "header key: Content-Disposition, header value: form-data; name=\"files\"; filename=\"example2.jpg\"\n" +
                                                  "rdto(formData) 에 json string 형식의 value값을 넣어준다. \n" +
                                                  "이메일은 선택사항으로 입력할 경우 rdto에 \"email\":\"test@test.com\" 을 추가로 넣어준다.\n" +
                                                  "{\"angkorId\": \"akadsf1231344334\", \"category\": 1, \"title\": \"title test\", \"content\": \"content test\"} \n" +
                                                  "1. category = 1 -> General\n"+
                                                  "2. category = 2 -> Angkor Account\n"+
                                                  "3. category = 3 -> Chat\n"+
                                                  "4. category = 4 -> Voice Call/Video Call")
    @PostMapping(value = "/question/setQuestion", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Language", required = true, dataType = "string", paramType = "header", value = "selected language"),
            @ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
    })
    public ResTrDTO setQuestion(HttpServletRequest request, @RequestPart(value = "files", required = false) MultipartFile[] files, @RequestPart("rdto") String rdto) {

        ORGReq038 rdtoJson = new ORGReq038();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            if(request.getHeader("OS")==null){
                rdtoJson = objectMapper.readValue(rdto, ORGReq038.class);
            }else{
                if(request.getHeader("OS").toLowerCase().equals("ios")){
                    byte[] ptext = rdto.getBytes(StandardCharsets.ISO_8859_1);
                    String value = new String(ptext, StandardCharsets.UTF_8);
                    rdtoJson = objectMapper.readValue(value, ORGReq038.class);
                }else{
                    rdtoJson = objectMapper.readValue(rdto, ORGReq038.class);
                }
            }
        } catch (JsonProcessingException e) {
            return CommonUtil.setResponseTrObject(null, 0, CommonErrorCode.INVALID_PARAMETER.getErrorcode() + "", CommonErrorCode.INVALID_PARAMETER.getGmessage() + " : " + CommonErrorCode.INVALID_PARAMETER.getDmessage());
        }

        log.info("request params : ________________ :" +rdto.toString());
        ResTrDTO res  = service.setQuestion(request, files, CommonUtil.Object2Hashmap(rdtoJson));
        log.info("res count ________________ :" + res.getTrcnt());
        return res;
    }

    @ApiOperation(value = "문의하기 목록", notes = "사용자가 등록한 문의 내역 목록을 불러온다.")
    @GetMapping(value = "/question/{AngkorId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Language", required = true, dataType = "string", paramType = "header", value = "selected language"),
            @ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
    })
    public ResCommDTO getQuestionList(HttpServletRequest request, @PathVariable(name = "AngkorId") String angkorId, @ApiParam(value = "request DTO", required = true) @ModelAttribute ORGReq037 rdto) {

        rdto.setAngkorId(angkorId);
        log.info("request params : ________________ :" +rdto.toString());
        ResCommDTO res  = service.getQuestionList(request, CommonUtil.Object2Hashmap(rdto));
        log.info("res data ________________ :" + res.getData());
        return res;
    }

    @ApiOperation(value = "문의하기 상세", notes = "선택한 문의 내역의 상세 정보를 가져온다.")
    @GetMapping(value = "/question/detail/{AngkorId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Language", required = true, dataType = "string", paramType = "header", value = "selected language"),
            @ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
    })
    public ResCommDTO getQuestionDetail(HttpServletRequest request, @PathVariable(name = "AngkorId") String angkorId, @ApiParam(value = "request DTO", required = true) @ModelAttribute ORGReq036 rdto) {

        rdto.setAngkorId(angkorId);
        log.info("request params : ________________ :" +rdto.toString());
        ResCommDTO res  = service.getQuestionDetail(request, CommonUtil.Object2Hashmap(rdto));
        log.info("res data ________________ :" + res.getData());

        HashMap mapRes = (HashMap) res.getData();
        if(mapRes!=null && mapRes.get("question")!=null){
            res.setTotal(1);
        }
        return res;
    }
}
