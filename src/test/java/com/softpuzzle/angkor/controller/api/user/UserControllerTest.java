package com.softpuzzle.angkor.controller.api.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softpuzzle.angkor.http.request.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:/application-test.properties")
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 회원_정보_확인() throws Exception {

        //given

        //when
        ResultActions result = mvc.perform(get("/ankochat/api/user/user/akICt71686138631")
                .header("Authorization", "bearer ")
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("{class-name}/{method-name}", preprocessResponse(prettyPrint()),
                                responseFields(
                                        List.of(
                                                fieldWithPath("data.profileUrl").type(JsonFieldType.STRING).description("사용자 프로필 이미지").optional(),
                                                fieldWithPath("data.authKey").type(JsonFieldType.STRING).description("사용자 authKey"),
                                                fieldWithPath("data.phonecode").type(JsonFieldType.STRING).description("국가코드번호"),
                                                fieldWithPath("data.profileMessage").type(JsonFieldType.STRING).description("사용자 프로필 메세지").optional(),
                                                fieldWithPath("data.phonenumber").type(JsonFieldType.STRING).description("사용자 국가코드 포함 휴대폰 번호"),
                                                fieldWithPath("data.gender").type(JsonFieldType.NUMBER).description("성별 [1:남자/2:여자/3:기타]").optional(),
                                                fieldWithPath("data.angkorId").type(JsonFieldType.STRING).description("앙코르챗 ID"),
                                                fieldWithPath("data.privacy_profile").type(JsonFieldType.BOOLEAN).description("프로필 공개/비공개"),
                                                fieldWithPath("data.birthday_dt").type(JsonFieldType.STRING).description("생일").optional(),
                                                fieldWithPath("data.language").type(JsonFieldType.STRING).description("설정 언어"),
                                                fieldWithPath("data.userName").type(JsonFieldType.STRING).description("사용자 이름"),
                                                fieldWithPath("data.userId").type(JsonFieldType.STRING).description("사용자 userId"),
                                                fieldWithPath("data.email").type(JsonFieldType.STRING).description("사용자 이메일").optional(),
                                                fieldWithPath("data.show_profile").type(JsonFieldType.BOOLEAN).description("프로필 검색 허용여부"),

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
    void 회원_전화번호_업데이트() throws Exception {

        //given
        ORGReq015 request = ORGReq015.builder().angkorId("akICt71686138631").phoneCode("82").phoneNumber("821056785678").certNumber("123456").grantType("update").authKey("bearer ").build();

        //when
        ResultActions result = mvc.perform(post("/ankochat/api/user/phone")
                .header("Authorization", "bearer ")
                .header("Uuid", "95aef0c9-d1f5-4800-b3f8-8d042a595ae6")
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
                                                fieldWithPath("data.authKey").type(JsonFieldType.STRING).description("사용자 authKey"),
                                                fieldWithPath("data.tokenType").type(JsonFieldType.STRING).description("tokenType"),
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
    void 회원_비밀번호_업데이트() throws Exception {

        //given
        ORGReq016 request = ORGReq016.builder().email("qwe123@qwe.com").certNumber("103658").build();

        //when
        ResultActions result = mvc.perform(post("/ankochat/api/user/pw")
                .header("AuthKey", "test1234!")
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
    void 회원_아이디_확인() throws Exception {

        //given
        ORGReq032 request = ORGReq032.builder().userId("test1234").grantType("signup").build();

        //when
        ResultActions result = mvc.perform(post("/ankochat/api/user/checkUserId")
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

    @Test
    void 회원_이메일_확인() throws Exception {

        //given
        ORGReq021 request = ORGReq021.builder().email("qwe5269@gmail.com").funcType("check").build();

        //when
        ResultActions result = mvc.perform(post("/ankochat/api/user/email")
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

    @Test
    void 회원_이메일_업데이트() throws Exception {

        //given
        ORGReq033 request = ORGReq033.builder().email("qwe123@qwe.com").angkorId("akICt71686138631").certNumber("301668").build();

        //when
        ResultActions result = mvc.perform(post("/ankochat/api/user/email/update")
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
                                                fieldWithPath("data.authKey").type(JsonFieldType.STRING).description("사용자 authKey"),
                                                fieldWithPath("data.tokenType").type(JsonFieldType.STRING).description("tokenType"),
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
    void 회원_아이디_찾기() throws Exception {

        //given
        ORGReq007 request = ORGReq007.builder().email("qwe1234@qwe.com").certNumber("768835").build();

        //when
        ResultActions result = mvc.perform(post("/ankochat/api/user/findUserId")
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
                                                fieldWithPath("data.userId").type(JsonFieldType.STRING).description("사용자 userId"),
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
    void 회원_위치정보_업데이트() throws Exception {

        //given
        ORGReq027 request = ORGReq027.builder().angkorId("akICt71686138631").chkCd(1).latitude("37.54889").longitude("126.9141561").build();

        //when
        ResultActions result = mvc.perform(post("/ankochat/api/user/position")
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
    void 회원_프로필정보_이미지상태_업데이트() throws Exception {

        //given
        ORGReq026 request = ORGReq026.builder().angkorId("akICt71686138631").profileSeq(139).status(1).build();

        //when
        ResultActions result = mvc.perform(post("/ankochat/api/user/profileImage")
                .header("Authorization", "bearer 6b87aa9d2bf0d79fe0d27bcb6efe864d30093e5769b5c7c7aa981a9ed91a45e5")
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
                                                fieldWithPath("data.recent_profile_url").type(JsonFieldType.STRING).description("최근 프로필 URL").optional(),
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

}