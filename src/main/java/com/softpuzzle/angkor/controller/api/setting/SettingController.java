package com.softpuzzle.angkor.controller.api.setting;

import com.softpuzzle.angkor.http.request.*;
import com.softpuzzle.angkor.http.response.common.ResCommDTO;
import com.softpuzzle.angkor.http.response.common.ResTrDTO;
import com.softpuzzle.angkor.service.api.chatting.ChattingService;
import com.softpuzzle.angkor.service.api.display.DisplayService;
import com.softpuzzle.angkor.service.api.friends.FriendsService;
import com.softpuzzle.angkor.service.api.language.LanguageService;
import com.softpuzzle.angkor.service.api.noti.NotiService;
import com.softpuzzle.angkor.service.api.privacy.PrivacyService;
import com.softpuzzle.angkor.service.api.version.VersionService;
import com.softpuzzle.angkor.utility.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;

@Api(tags = {"시스템 설정"}, description = "Setting")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/ankochat/api/sets")
public class SettingController {

    private final VersionService versionService;
    private final NotiService notiService;
    private final PrivacyService privacyService;
    private final FriendsService friendsService;
    private final DisplayService displayService;
    private final ChattingService chattingService;
    private final LanguageService languageService;

    @ApiOperation(value = "사용자별 버전정보", notes = "앙코르챗 사용자의 버전정보를 체크한다.")
    @GetMapping(value = "/version/{AngkorId}")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
    })
    public ResCommDTO getVersion(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq013 rdto) {

        log.info("request params : ________________ :" +rdto.toString());
        ResCommDTO res  = versionService.getVersion(request, CommonUtil.Object2Hashmap(rdto));
        log.info("res data ________________ :" + res.getData());
        return res;
    }

    @ApiOperation(value = "버전정보 Update", notes = "앙코르챗 사용자의 버전정보를 갱신한다.")
    @PostMapping(value = "/version/{AngkorId}")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
    })
    public ResTrDTO setVersion(HttpServletRequest request, @RequestBody ORGReq041 rdto) {

        log.info("request params : ________________ :" +rdto.toString());
        ResTrDTO res  = versionService.setVersion(request, CommonUtil.Object2Hashmap(rdto));
        log.info("res data ________________ :" + res.getData());
        return res;
    }

    @ApiOperation(value = "플렛폼별 버전정보", notes = "Android/IOS 각 플렛폼별 앱 최신 버전정보를 체크한다.\n" +
                                                    "안드로이드의 버전 정보를 얻고 싶다면 ANDROID를, IOS의 버전 정보는 IOS를 OsType에 입력해준다.")
    @GetMapping(value = "/version/os/{OsType}")
    @ResponseBody
    public ResCommDTO getOsVersion(HttpServletRequest request, @PathVariable(name = "OsType") ORGReq055 rdto) {

        log.info("request params : ________________ :" +rdto.toString());
        ResCommDTO res  = versionService.getOsVersion(request, CommonUtil.Object2Hashmap(rdto));
        log.info("res data ________________ :" + res.getData());
        return res;
    }

    @ApiOperation(value = "강제업데이트 대상 확인", notes = "앙코르챗 설치되어 있는 기기의 현재 버전 정보를 가져와 강제업데이트 대상인지 확인한다.")
    @GetMapping(value = "/version/appUpdate/{Version}")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "OS", required = true, dataType = "string", paramType = "header", value = "android")
    })
    public ResCommDTO checkAppUpdate(HttpServletRequest request, @PathVariable(value = "Version") String version) {

        log.info("request params : ________________ :" +version);
        ResCommDTO res  = versionService.checkAppUpdate(request, version);
        log.info("res data ________________ :" + res.getData());

        HashMap mapRes = (HashMap) res.getData();
        if(mapRes!=null) res.setTotal(1);
        return res;
    }

    @ApiOperation(value = "사용자별 Noti 정보", notes = "앙코르챗 사용자의 Notification 정보를 체크한다.")
    @GetMapping(value = "/noti/{AngkorId}")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
    })
    public ResCommDTO getNoti(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq043 rdto) {

        log.info("request params : ________________ :" +rdto.toString());
        ResCommDTO res  = notiService.getNoti(request, CommonUtil.Object2Hashmap(rdto));
        log.info("res data ________________ :" + res.getData());
        return res;
    }

    @ApiOperation(value = "사용자별 Noti 정보 Update", notes = "앙코르챗 사용자의 Notification 정보를 갱신한다.")
    @PostMapping(value = "/noti/{AngkorId}")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
    })
    public ResTrDTO setNoti(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq043 angkorId,
                               @Valid @RequestBody ORGReq044 rdto) {

        rdto.setAngkorId(angkorId.getAngkorId());

        log.info("request params : ________________ :" +rdto);
        ResTrDTO res  = notiService.setNoti(request, CommonUtil.Object2Hashmap(rdto));
        log.info("res data ________________ :" + res.getData());
        return res;
    }

    @ApiOperation(value = "개인정보 보안 가져오기", notes = "앙코르챗 사용자의 개인정보 보안을 체크한다.")
    @GetMapping(value = "/privacy/{AngkorId}")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
    })
    public ResCommDTO getPrivacy(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq045 rdto) {

        log.info("request params : ________________ :" +rdto.toString());
        ResCommDTO res  = privacyService.getPrivacy(request, CommonUtil.Object2Hashmap(rdto));
        log.info("res data ________________ :" + res.getData());
        return res;
    }

    @ApiOperation(value = "사용자별 개인정보 보안 정보 Update", notes = "앙코르챗 사용자의 개인정보 보안을 갱신한다.")
    @PostMapping(value = "/privacy/{AngkorId}")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
    })
    public ResTrDTO setPrivacy(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq045 angkorId,
                               @Valid @RequestBody ORGReq046 rdto) {

        rdto.setAngkorId(angkorId.getAngkorId());

        log.info("request params : ________________ :" +rdto);
        ResTrDTO res  = privacyService.setPrivacy(request, CommonUtil.Object2Hashmap(rdto));
        log.info("res data ________________ :" + res.getData());
        return res;
    }

    @ApiOperation(value = "친구추가 설정 가져오기", notes = "앙코르챗 사용자의 친구추가 설정을 체크한다.")
    @GetMapping(value = "/friends/{AngkorId}")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
    })
    public ResCommDTO getFriends(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq047 rdto) {

        log.info("request params : ________________ :" +rdto.toString());
        ResCommDTO res  = friendsService.getFriends(request, CommonUtil.Object2Hashmap(rdto));
        log.info("res data ________________ :" + res.getData());
        return res;
    }

    @ApiOperation(value = "사용자별 친구추가 설정 Update", notes = "앙코르챗 사용자의 친구추가 설정을 갱신한다.")
    @PostMapping(value = "/friends/{AngkorId}")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
    })
    public ResTrDTO setFriends(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq047 angkorId,
                               @Valid @RequestBody ORGReq048 rdto) {

        rdto.setAngkorId(angkorId.getAngkorId());

        log.info("request params : ________________ :" +rdto);
        ResTrDTO res  = friendsService.setFriends(request, CommonUtil.Object2Hashmap(rdto));
        log.info("res data ________________ :" + res.getData());
        return res;
    }

    @ApiOperation(value = "디스플레이 설정 가져오기", notes = "앙코르챗 사용자의 디스플레이 설정을 체크한다.")
    @GetMapping(value = "/display/{AngkorId}")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
    })
    public ResCommDTO getDisplay(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq051 rdto) {

        log.info("request params : ________________ :" +rdto.toString());
        ResCommDTO res  = displayService.getDisplay(request, CommonUtil.Object2Hashmap(rdto));
        log.info("res data ________________ :" + res.getData());
        return res;
    }

    @ApiOperation(value = "사용자별 디스플레이 설정 Update", notes = "앙코르챗 사용자의 디스플레이 설정을 갱신한다.")
    @PostMapping(value = "/display/{AngkorId}")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
    })
    public ResTrDTO setDisplay(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq051 angkorId,
                               @Valid @RequestBody ORGReq052 rdto) {

        rdto.setAngkorId(angkorId.getAngkorId());

        log.info("request params : ________________ :" +rdto);
        ResTrDTO res  = displayService.setDisplay(request, CommonUtil.Object2Hashmap(rdto));
        log.info("res data ________________ :" + res.getData());
        return res;
    }

    @ApiOperation(value = "채팅방 배경 컬러 색상 가져오기", notes = "채팅방 기본 배경색상 컬러 코드들을 가져온다.")
    @GetMapping(value = "/display/background/{AngkorId}")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
    })
    public ResCommDTO getBackgroundColor(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq049 rdto) {

        log.info("request params : ________________ :" +rdto.toString());
        ResCommDTO res  = displayService.getBackgroundColor(request, CommonUtil.Object2Hashmap(rdto));
        log.info("res data ________________ :" + res.getData());
        return res;
    }

    @ApiOperation(value = "채팅방 배경컬러 Update", notes = "앙코르챗 사용자의 채팅방 배경색을 선택한 색에 맞게 설정한다.")
    @PostMapping(value = "/display/background/{AngkorId}")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
    })
    public ResTrDTO setBackgroundColor(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq049 angkorId, @RequestBody ORGReq058 rdto) {

        rdto.setAngkorId(angkorId.getAngkorId());

        log.info("request params : ________________ :" +rdto);
        ResTrDTO res  = displayService.setBackgroundColor(request, CommonUtil.Object2Hashmap(rdto));
        log.info("res data ________________ :" + res.getData());
        return res;
    }

    @ApiOperation(value = "폰트 사이즈 Update", notes = "앙코르챗 사용자가 설정한 폰트 사이즈로 재설정한다.")
    @PostMapping(value = "/display/font/{AngkorId}")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
    })
    public ResTrDTO setFontSize(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq049 angkorId, @RequestBody ORGReq059 rdto) {

        rdto.setAngkorId(angkorId.getAngkorId());

        log.info("request params : ________________ :" +rdto);
        ResTrDTO res  = displayService.setFontSize(request, CommonUtil.Object2Hashmap(rdto));
        log.info("res data ________________ :" + res.getData());
        return res;
    }

    @ApiOperation(value = "채팅 설정 가져오기", notes = "앙코르챗 사용자의 채팅 설정을 체크한다.")
    @GetMapping(value = "/chatting/{AngkorId}")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
    })
    public ResCommDTO getChatting(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq049 rdto) {

        log.info("request params : ________________ :" +rdto.toString());
        ResCommDTO res  = chattingService.getChatting(request, CommonUtil.Object2Hashmap(rdto));
        log.info("res data ________________ :" + res.getData());
        return res;
    }

    @ApiOperation(value = "사용자별 채팅 설정 Update", notes = "앙코르챗 사용자의 채팅 설정을 갱신한다.")
    @PostMapping(value = "/chatting/{AngkorId}")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
    })
    public ResTrDTO setChatting(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq049 angkorId,
                                @Valid @RequestBody ORGReq050 rdto) {

        rdto.setAngkorId(angkorId.getAngkorId());

        log.info("request params : ________________ :" +rdto);
        ResTrDTO res  = chattingService.setChatting(request, CommonUtil.Object2Hashmap(rdto));
        log.info("res data ________________ :" + res.getData());
        return res;
    }

    @ApiOperation(value = "사용자별 채팅 설정 - 이미지 품질 Update", notes = "앙코르챗 사용자의 이미지 전송 품질 설정을 갱신한다.\n"+
                                                                        "[D001:original, D002:standard,D003:low]")
    @PostMapping(value = "/chatting/image/{AngkorId}")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
    })
    public ResTrDTO setChattingToImageQuality(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq049 angkorId,
                                @Valid @RequestBody ORGReq056 rdto) {

        rdto.setAngkorId(angkorId.getAngkorId());

        log.info("request params : ________________ :" +rdto);
        ResTrDTO res  = chattingService.setChattingToImageQuality(request, CommonUtil.Object2Hashmap(rdto));
        log.info("res data ________________ :" + res.getData());
        return res;
    }

    @ApiOperation(value = "사용자별 채팅 설정 - 비디오 품질 Update", notes = "앙코르챗 사용자의 비디오 전송 품질 설정을 갱신한다.\n"+
                                                                        "[D001:original, D002:standard]")
    @PostMapping(value = "/chatting/video/{AngkorId}")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
    })
    public ResTrDTO setChattingToVideoQuality(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq049 angkorId,
                                              @Valid @RequestBody ORGReq057 rdto) {

        rdto.setAngkorId(angkorId.getAngkorId());

        log.info("request params : ________________ :" +rdto);
        ResTrDTO res  = chattingService.setChattingToVideoQuality(request, CommonUtil.Object2Hashmap(rdto));
        log.info("res data ________________ :" + res.getData());
        return res;
    }

    @ApiOperation(value = "언어 설정 가져오기", notes = "앙코르챗 사용자의 언어 설정을 체크한다.")
    @GetMapping(value = "/language/{AngkorId}")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
    })
    public ResCommDTO getLanguage(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq053 rdto) {

        log.info("request params : ________________ :" +rdto.toString());
        ResCommDTO res  = languageService.getLanguage(request, CommonUtil.Object2Hashmap(rdto));
        log.info("res data ________________ :" + res.getData());
        return res;
    }

    @ApiOperation(value = "사용자별 언어 설정 Update", notes = "앙코르챗 사용자의 언어 설정을 갱신한다.")
    @PostMapping(value = "/language/{AngkorId}")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Uuid", required = true, dataType = "string", paramType = "header", value = "angkor uuid")
    })
    public ResTrDTO setLanguage(HttpServletRequest request, @PathVariable(name = "AngkorId") ORGReq053 angkorId,
                                @Valid @RequestBody ORGReq054 rdto) {

        rdto.setAngkorId(angkorId.getAngkorId());

        log.info("request params : ________________ :" +rdto);
        ResTrDTO res  = languageService.setLanguage(request, CommonUtil.Object2Hashmap(rdto));
        log.info("res data ________________ :" + res.getData());
        return res;
    }


}
