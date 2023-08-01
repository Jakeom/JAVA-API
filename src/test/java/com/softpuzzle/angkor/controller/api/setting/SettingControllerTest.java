package com.softpuzzle.angkor.controller.api.setting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softpuzzle.angkor.http.request.ORGReq041;
import com.softpuzzle.angkor.http.request.ORGReq043;
import com.softpuzzle.angkor.http.request.ORGReq058;
import com.softpuzzle.angkor.http.request.ORGReq059;
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

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:/application-test.properties")
public class SettingControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 버전_정보_확인() throws Exception {

        //given
        //ORGReq041 request = ORGReq041.builder().angkorId("akykre1680848624").build();

        //when
        ResultActions result = mvc.perform(get("/ankochat/api/sets/version/akOYHx1683105988")
                .header("Uuid", "f66c863a-b7b2-4cb8-9383-b33847c5f2f4")
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("{class-name}/{method-name}", preprocessResponse(prettyPrint()),
                                responseFields(
                                        List.of(
                                                fieldWithPath("data.osInfo").type(JsonFieldType.STRING).description("OS 정보"),
                                                fieldWithPath("data.angkorId").type(JsonFieldType.STRING).description("앙코르챗 ID"),
                                                fieldWithPath("data.updatedt").type(JsonFieldType.STRING).description("업데이트 최근 날짜").optional(),
                                                fieldWithPath("data.currentVersion").type(JsonFieldType.STRING).description("현재 설치되어 있는 버전"),
                                                fieldWithPath("data.newVersion").type(JsonFieldType.STRING).description("앙코르챗 최신 버전"),
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
    void 플렛폼별_버전_확인() throws Exception {

        //given
        //ORGReq055 request = ORGReq055.builder().osType("android").build();

        //when
        ResultActions result = mvc.perform(get("/ankochat/api/sets/version/os/android")
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("{class-name}/{method-name}", preprocessResponse(prettyPrint()),
                                responseFields(
                                        List.of(
                                                fieldWithPath("data.app_version").type(JsonFieldType.STRING).description("OS별 버전 정보"),
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
    void 강제_업데이트_확인() throws Exception {

        //given
        //ORGReq055 request = ORGReq055.builder().osType("android").build();

        //when
        ResultActions result = mvc.perform(get("/ankochat/api/sets/version/appUpdate/1.0.4")
                .header("OS", "android")
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("{class-name}/{method-name}", preprocessResponse(prettyPrint()),
                                responseFields(
                                        List.of(
                                                fieldWithPath("data.app_update").type(JsonFieldType.BOOLEAN).description("강제업데이트 대상 여부"),
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
    void 알림_설정_확인() throws Exception {

        //given
       // ORGReq043 request = ORGReq043.builder().angkorId("akykre1680848624").build();

        //when
        ResultActions result = mvc.perform(get("/ankochat/api/sets/noti/akOYHx1683105988")
                .header("Uuid", "f66c863a-b7b2-4cb8-9383-b33847c5f2f4")
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("{class-name}/{method-name}", preprocessResponse(prettyPrint()),
                                responseFields(
                                        List.of(
                                                fieldWithPath("data.addAuto").type(JsonFieldType.BOOLEAN).description("알림 온/오프"),
                                                fieldWithPath("data.inCountUnreadMessage").type(JsonFieldType.BOOLEAN).description("읽지 않은 메시지 개수 표시 온/오프"),
                                                fieldWithPath("data.inNameOnLockScreen").type(JsonFieldType.BOOLEAN).description("잠금 화면일 경우 사용자명 보기 온/오프"),
                                                fieldWithPath("data.angkorId").type(JsonFieldType.STRING).description("앙코르챗 ID"),
                                                fieldWithPath("data.inPreview").type(JsonFieldType.BOOLEAN).description("미리보기 온/오프"),
                                                fieldWithPath("data.inAppsounds").type(JsonFieldType.BOOLEAN).description("소리 온/오프"),
                                                fieldWithPath("data.newGroup").type(JsonFieldType.BOOLEAN).description("새로운 그룹쳇(에 초대) 알림 온/오프"),
                                                fieldWithPath("data.inVibrate").type(JsonFieldType.BOOLEAN).description("진동 온/오프"),
                                                fieldWithPath("data.inNewContacts").type(JsonFieldType.BOOLEAN).description("내 연락처에 저장된 사용자가 앙코르쳇 가입시 알림 온/오프"),
                                                fieldWithPath("data.updateDt").type(JsonFieldType.STRING).description("업데이트 최근 날짜").optional(),
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
    void 개인정보_설정_확인() throws Exception {

        //given
        // ORGReq043 request = ORGReq043.builder().angkorId("akykre1680848624").build();

        //when
        ResultActions result = mvc.perform(get("/ankochat/api/sets/privacy/akOYHx1683105988")
                .header("Uuid", "f66c863a-b7b2-4cb8-9383-b33847c5f2f4")
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("{class-name}/{method-name}", preprocessResponse(prettyPrint()),
                                responseFields(
                                        List.of(
                                                fieldWithPath("data.inPhotoRange").type(JsonFieldType.NUMBER).description("내 프로필 사진을 볼 수 있는 범위 설정 [1:everybody,2:My friends,3:Nobody]"),
                                                fieldWithPath("data.inTwostep").type(JsonFieldType.BOOLEAN).description("2단계 인증 온/오프"),
                                                fieldWithPath("data.inMessageRange").type(JsonFieldType.NUMBER).description("내 메시지를 포워딩 할 수 있는 범위 설정 [1:everybody,2:My friends,3:Nobody]"),
                                                fieldWithPath("data.inOnlineRange").type(JsonFieldType.NUMBER).description("내 접속 중 표시를 볼 수 있는 범위 설정 [1:everybody,2:My friends,3:Nobody]"),
                                                fieldWithPath("data.angkorId").type(JsonFieldType.STRING).description("앙코르챗 ID"),
                                                fieldWithPath("data.inGroupRange").type(JsonFieldType.NUMBER).description("나를 그룹채팅방에 초대 할 수 있는 범위 설정 [1:everybody,2:My friends,3:Nobody]"),
                                                fieldWithPath("data.inFaceid").type(JsonFieldType.BOOLEAN).description("생체인증 온/오프"),
                                                fieldWithPath("data.updateDt").type(JsonFieldType.STRING).description("업데이트 최근 날짜").optional(),
                                                fieldWithPath("data.inCallrange").type(JsonFieldType.NUMBER).description("나에게 전화를 걸 수 있는 범위 설정 [1:everybody,2:My friends,3:Nobody]"),
                                                fieldWithPath("data.inEmailOpen").type(JsonFieldType.NUMBER).description("내 이메일을 볼 수 있는 범위 설정 [1:everybody,2:My friends,3:Nobody]"),
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
    void 친구추가_설정_확인() throws Exception {

        //given
        // ORGReq043 request = ORGReq043.builder().angkorId("akykre1680848624").build();

        //when
        ResultActions result = mvc.perform(get("/ankochat/api/sets/friends/akOYHx1683105988")
                .header("Uuid", "f66c863a-b7b2-4cb8-9383-b33847c5f2f4")
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("{class-name}/{method-name}", preprocessResponse(prettyPrint()),
                                responseFields(
                                        List.of(
                                                fieldWithPath("data.addAuto").type(JsonFieldType.BOOLEAN).description("자동 친구 추가 ON/OFF 토글 스위치"),
                                                fieldWithPath("data.angkorId").type(JsonFieldType.STRING).description("앙코르챗 ID"),
                                                fieldWithPath("data.inRefresh").type(JsonFieldType.BOOLEAN).description("새로고침"),
                                                fieldWithPath("data.updateDt").type(JsonFieldType.STRING).description("업데이트 최근 날짜").optional(),
                                                fieldWithPath("data.inRecommendFriends").type(JsonFieldType.BOOLEAN).description("친구 추천 ON/OFF 토글 스위치"),
                                                fieldWithPath("data.inAllowOthers").type(JsonFieldType.BOOLEAN).description("allow others to add me 스위치"),
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
    void 채팅_설정_확인() throws Exception {

        //given
        // ORGReq043 request = ORGReq043.builder().angkorId("akykre1680848624").build();

        //when
        ResultActions result = mvc.perform(get("/ankochat/api/sets/chatting/akICt71686138631")
                .header("Uuid", "95aef0c9-d1f5-4800-b3f8-8d042a595ae6")
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("{class-name}/{method-name}", preprocessResponse(prettyPrint()),
                                responseFields(
                                        List.of(
                                                fieldWithPath("data.inVideosConvert").type(JsonFieldType.BOOLEAN).description("컨버팅 온/오프 토글 스위치"),
                                                fieldWithPath("data.inImageResolution").type(JsonFieldType.STRING).description("이미지 전송 시 화질(압축률) [D001:original,D002:standard,D003:low]"),
                                                fieldWithPath("data.inSortrooms").type(JsonFieldType.STRING).description("채팅방 정렬 [D001:By Time,D002:By Unread Chats]"),
                                                fieldWithPath("data.angkorId").type(JsonFieldType.STRING).description("앙코르챗 ID"),
                                                fieldWithPath("data.inChatbackop").type(JsonFieldType.BOOLEAN).description("채팅 백업"),
                                                fieldWithPath("data.inChatmbackop").type(JsonFieldType.BOOLEAN).description("채팅과 미디어 백업"),
                                                fieldWithPath("data.updateDt").type(JsonFieldType.STRING).description("업데이트 최근 날짜").optional(),
                                                fieldWithPath("data.inVideos").type(JsonFieldType.STRING).description("영상 전송 시 화질(압축률) [D001:high,D002:standard,D003:low]"),
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
    void 언어_설정_확인() throws Exception {

        //given
        // ORGReq043 request = ORGReq043.builder().angkorId("akykre1680848624").build();

        //when
        ResultActions result = mvc.perform(get("/ankochat/api/sets/language/akOYHx1683105988")
                .header("Uuid", "f66c863a-b7b2-4cb8-9383-b33847c5f2f4")
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("{class-name}/{method-name}", preprocessResponse(prettyPrint()),
                                responseFields(
                                        List.of(
                                                fieldWithPath("data.language").type(JsonFieldType.STRING).description("설정한 언어"),
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
    void 디스플레이_설정_확인() throws Exception {

        //given
        // ORGReq043 request = ORGReq043.builder().angkorId("akykre1680848624").build();

        //when
        ResultActions result = mvc.perform(get("/ankochat/api/sets/display/akJziI1687276312")
                .header("Uuid", "0cc3d393-7ad0-4784-a2ea-31e211c37e33")
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("{class-name}/{method-name}", preprocessResponse(prettyPrint()),
                                responseFields(
                                        List.of(
                                                fieldWithPath("data.inChatbackground").type(JsonFieldType.STRING).description("채팅창 백그라운드 컬러 코드"),
                                                fieldWithPath("data.inNightmode").type(JsonFieldType.BOOLEAN).description("나이트 모드 \n Code [0:밤,1:낮]"),
                                                fieldWithPath("data.inThemes").type(JsonFieldType.STRING).description("테마 컬러 코드 \n Code [D001:FFFAFF ...]"),
                                                fieldWithPath("data.inFontsize").type(JsonFieldType.STRING).description("폰트 사이즈"),
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
    void 디스플레이_채팅방_배경색상_리스트() throws Exception {

        //given
        // ORGReq043 request = ORGReq043.builder().angkorId("akykre1680848624").build();

        //when
        ResultActions result = mvc.perform(get("/ankochat/api/sets/display/background/akJziI1687276312")
                .header("Uuid", "0cc3d393-7ad0-4784-a2ea-31e211c37e33")
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("{class-name}/{method-name}", preprocessResponse(prettyPrint()),
                                responseFields(
                                        List.of(
                                                fieldWithPath("data[].seq").type(JsonFieldType.NUMBER).description("순번"),
                                                fieldWithPath("data[].inChatbackground").type(JsonFieldType.STRING).description("채팅창 백그라운드 컬러 코드"),
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
    void 디스플레이_채팅방_배경색상_업데이트() throws Exception {

        //given
        ORGReq058 request = ORGReq058.builder().angkorId("akJziI1687276312").backgroundSeq(19).build();

        //when
        ResultActions result = mvc.perform(post("/ankochat/api/sets/display/background/akJziI1687276312")
                .header("Uuid", "0cc3d393-7ad0-4784-a2ea-31e211c37e33")
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
                                                fieldWithPath("trcnt").type(JsonFieldType.NUMBER).description("데이터 개수"),
                                                fieldWithPath("message").type(JsonFieldType.STRING).description("수행결과 메시지"),
                                                fieldWithPath("trdate").type(JsonFieldType.STRING).description("수행 시간")
                                        ))
                        )
                )
                .andReturn()
                .getResponse();
    }

    @Test
    void 폰트_사이즈_업데이트() throws Exception {

        //given
        ORGReq059 request = ORGReq059.builder().angkorId("akJziI1687276312").inFontsize("16").build();

        //when
        ResultActions result = mvc.perform(post("/ankochat/api/sets/display/font/akJziI1687276312")
                .header("Uuid", "0cc3d393-7ad0-4784-a2ea-31e211c37e33")
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
                                                fieldWithPath("trcnt").type(JsonFieldType.NUMBER).description("데이터 개수"),
                                                fieldWithPath("message").type(JsonFieldType.STRING).description("수행결과 메시지"),
                                                fieldWithPath("trdate").type(JsonFieldType.STRING).description("수행 시간")
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