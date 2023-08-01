package com.softpuzzle.angkor.controller.api.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softpuzzle.angkor.http.request.ORGReq008;
import com.softpuzzle.angkor.http.request.ORGReq010;
import com.softpuzzle.angkor.http.request.ORGReq011;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:/application-test.properties")
public class LoginControllerTest {

    private static String APP_KEY = "ea798027-6556-4319-b4ed-52dae46158cd";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void 사용자_로그인() throws Exception {

        //given
        ORGReq010 request = ORGReq010.builder().password("wnsgml4344!").uuid("0cc3d393-7ad0-4784-a2ea-31e211c37e33").userId("test9003").publicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAy8Xf4R1S6UI5P2YCWqqoTMHAIZ07Bw9m4zFwa2c6StwgrmQy0vT4HddkTWUPeGH6kzuvyQqaGWu1UQRfC9DcuuMepnuQUn5ko1bOEf8uqFOlNP1B/0GmhU8hg282tBxi6TXU1DN6mpJhZ0/TS25qfRyUzd4Mh/+aBSZzNOiC8N+aaYOIG6sHmYzVZddU1pHlV602a2ASEZdA95Vr8blp8guoZV5K+ANIX5Qkba5yHIPNmQwgEGSUiZSg94TQBPXuossG2uOlRYvkAvRPbhIA4VryI+2C4U7L2vp3oTZopkwKSL1h8rDOwPSHV0A4u1tLazSRnp40wNtc8SoSnwH+vQIDAQAB").build();

        //when
        ResultActions result = mvc.perform(post("/ankochat/api/login/signin")
                .header("Language", "en")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("{class-name}/{method-name}", preprocessResponse(prettyPrint()),
                                responseFields(
                                        List.of(
                                                fieldWithPath("data.profileUrl").type(JsonFieldType.STRING).description("사용자 프로필 이미지").optional(),
                                                fieldWithPath("data.profileMessage").type(JsonFieldType.STRING).description("사용자 프로필 메세지").optional(),
                                                fieldWithPath("data.authKey").type(JsonFieldType.STRING).description("사용자 authKey"),
                                                fieldWithPath("data.phonecode").type(JsonFieldType.STRING).description("국가코드번호"),
                                                fieldWithPath("data.phonenumber").type(JsonFieldType.STRING).description("사용자 국가코드 포함 휴대폰 번호"),
                                                fieldWithPath("data.gender").type(JsonFieldType.NUMBER).description("성별 [1:남자/2:여자/3:기타]").optional(),
                                                fieldWithPath("data.angkorId").type(JsonFieldType.STRING).description("앙코르챗 ID"),
                                                fieldWithPath("data.privacy_profile").type(JsonFieldType.BOOLEAN).description("프로필 공개/비공개"),
                                                fieldWithPath("data.birthday_dt").type(JsonFieldType.STRING).description("생일").optional(),
                                                fieldWithPath("data.userName").type(JsonFieldType.STRING).description("사용자 이름"),
                                                fieldWithPath("data.userId").type(JsonFieldType.STRING).description("사용자 userId"),
                                                fieldWithPath("data.email").type(JsonFieldType.STRING).description("사용자 이메일").optional(),
                                                fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("accessToken"),
                                                fieldWithPath("data.show_profile").type(JsonFieldType.BOOLEAN).description("프로필 검색 허용여부"),

                                                fieldWithPath("data.settingData.noti.addAuto").type(JsonFieldType.BOOLEAN).description("알림 온/오프"),
                                                fieldWithPath("data.settingData.noti.inCountUnreadMessage").type(JsonFieldType.BOOLEAN).description("읽지 않은 메시지 개수 표시 온/오프"),
                                                fieldWithPath("data.settingData.noti.inNameOnLockScreen").type(JsonFieldType.BOOLEAN).description("잠금 화면일 경우 사용자명 보기 온/오프"),
                                                fieldWithPath("data.settingData.noti.inPreview").type(JsonFieldType.BOOLEAN).description("미리보기 온/오프"),
                                                fieldWithPath("data.settingData.noti.inAppsounds").type(JsonFieldType.BOOLEAN).description("소리 온/오프"),
                                                fieldWithPath("data.settingData.noti.newGroup").type(JsonFieldType.BOOLEAN).description("새로운 그룹쳇(에 초대) 알림 온/오프"),
                                                fieldWithPath("data.settingData.noti.inVibrate").type(JsonFieldType.BOOLEAN).description("진동 온/오프"),
                                                fieldWithPath("data.settingData.noti.inNewContacts").type(JsonFieldType.BOOLEAN).description("내 연락처에 저장된 사용자가 앙코르쳇 가입시 알림 온/오프"),

                                                fieldWithPath("data.settingData.security.inPhotoRange").type(JsonFieldType.NUMBER).description("내 프로필 사진을 볼 수 있는 범위 설정 [1:everybody,2:My friends,3:Nobody]"),
                                                fieldWithPath("data.settingData.security.inTwostep").type(JsonFieldType.BOOLEAN).description("2단계 인증 온/오프"),
                                                fieldWithPath("data.settingData.security.inMessageRange").type(JsonFieldType.NUMBER).description("내 메시지를 포워딩 할 수 있는 범위 설정 [1:everybody,2:My friends,3:Nobody]"),
                                                fieldWithPath("data.settingData.security.inOnlineRange").type(JsonFieldType.NUMBER).description("내 접속 중 표시를 볼 수 있는 범위 설정 [1:everybody,2:My friends,3:Nobody]"),
                                                fieldWithPath("data.settingData.security.inGroupRange").type(JsonFieldType.NUMBER).description("나를 그룹채팅방에 초대 할 수 있는 범위 설정 [1:everybody,2:My friends,3:Nobody]"),
                                                fieldWithPath("data.settingData.security.inFaceid").type(JsonFieldType.BOOLEAN).description("생체인증 온/오프"),
                                                fieldWithPath("data.settingData.security.inCallrange").type(JsonFieldType.NUMBER).description("나에게 전화를 걸 수 있는 범위 설정 [1:everybody,2:My friends,3:Nobody]"),
                                                fieldWithPath("data.settingData.security.inEmailOpen").type(JsonFieldType.NUMBER).description("내 이메일을 볼 수 있는 범위 설정 [1:everybody,2:My friends,3:Nobody]"),

                                                fieldWithPath("data.settingData.friend.addAuto").type(JsonFieldType.BOOLEAN).description("자동 친구 추가 ON/OFF 토글 스위치"),
                                                fieldWithPath("data.settingData.friend.inRecommendFriends").type(JsonFieldType.BOOLEAN).description("친구 추천 ON/OFF 토글 스위치"),
                                                fieldWithPath("data.settingData.friend.inAllowOthers").type(JsonFieldType.BOOLEAN).description("allow others to add me 스위치"),

                                                fieldWithPath("data.settingData.chatting.inVideosConvert").type(JsonFieldType.BOOLEAN).description("컨버팅 온/오프 토글 스위치"),
                                                fieldWithPath("data.settingData.chatting.inImageResolution").type(JsonFieldType.STRING).description("이미지 전송 시 화질(압축률) [D001:original,D002:standard,D003:low]"),
                                                fieldWithPath("data.settingData.chatting.inSortrooms").type(JsonFieldType.STRING).description("채팅방 정렬 [D001:By Time,D002:By Unread Chats]"),
                                                fieldWithPath("data.settingData.chatting.inChatbackop").type(JsonFieldType.BOOLEAN).description("채팅 백업"),
                                                fieldWithPath("data.settingData.chatting.inChatmbackop").type(JsonFieldType.BOOLEAN).description("채팅과 미디어 백업"),
                                                fieldWithPath("data.settingData.chatting.inVideos").type(JsonFieldType.STRING).description("영상 전송 시 화질(압축률) [D001:high,D002:standard]"),

                                                fieldWithPath("data.settingData.display.inChatbackground").type(JsonFieldType.STRING).description("채팅창 배경화면 컬러"),
                                                fieldWithPath("data.settingData.display.inFontsize").type(JsonFieldType.STRING).description("폰트 사이즈"),
                                                fieldWithPath("data.settingData.display.inNightmode").type(JsonFieldType.BOOLEAN).description("나이트 모드"),
                                                fieldWithPath("data.settingData.display.inThemes").type(JsonFieldType.STRING).description("테마 컬러"),

                                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과 코드"),
                                                fieldWithPath("current").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                                fieldWithPath("pageSize").type(JsonFieldType.NUMBER).description("요청 페이지 사이즈"),
                                                fieldWithPath("total").type(JsonFieldType.NUMBER).description("수행 결과 코드 0(성공) 나머지는 에러"),
                                                fieldWithPath("message").type(JsonFieldType.STRING).description("수행결과 메시지"),
                                                fieldWithPath("resTime").type(JsonFieldType.STRING).description("수행 시간")
                                        ))
                        )
                )
                .andReturn()
                .getResponse();
    }

    @Test
    public void 사용자_로그아웃() throws Exception {

        //given
        ORGReq011 request = ORGReq011.builder().angkorId("akICt71686138631").build();

        //when
        ResultActions result = mvc.perform(post("/ankochat/api/login/signout/akFBJ31681979983")
                .header("Authorization", "bearer ")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("{class-name}/{method-name}", preprocessResponse(prettyPrint()),
                                responseFields(
                                        List.of(
                                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터").optional(),
                                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과 코드"),
                                                fieldWithPath("current").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                                fieldWithPath("pageSize").type(JsonFieldType.NUMBER).description("요청 페이지 사이즈"),
                                                fieldWithPath("total").type(JsonFieldType.NUMBER).description("수행 결과 코드 0(성공) 나머지는 에러"),
                                                fieldWithPath("message").type(JsonFieldType.STRING).description("수행결과 메시지"),
                                                fieldWithPath("resTime").type(JsonFieldType.STRING).description("수행 시간")
                                        ))
                        )
                )
                .andReturn()
                .getResponse();
    }

    private Attributes.Attribute getFormatAttribute(String value){
        return new Attributes.Attribute("format", value);
    }
}