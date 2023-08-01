package com.softpuzzle.angkor.controller.api.address;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softpuzzle.angkor.http.request.*;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:/application-test.properties")
public class AddressControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 친구_추가() throws Exception {

        //given
        ORGReq024 request = ORGReq024.builder().angkorId("akOYHx1683105988").friend_angkorId("akU9CO1682660040").friend_name("Bob").friend_phonenumber("821000000000").build();

        //when
        ResultActions result = mvc.perform(post("/ankochat/api/address/setAddress")
                .header("Uuid", "f66c863a-b7b2-4cb8-9383-b33847c5f2f4")
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
    void 친구_내_주소록_일괄_추가() throws Exception {

        //given
        ORGReq025 request = ORGReq025.builder().angkorId("akOYHx1683105988").friendList(List.of(ORGReq023.builder().friend_name("Brown").friend_phonenumber("821000000001").build())).build();

        //when
        ResultActions result = mvc.perform(post("/ankochat/api/address/setAddress")
                .header("Uuid", "f66c863a-b7b2-4cb8-9383-b33847c5f2f4")
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
    void 친구_리스트_확인() throws Exception {

        //given
        //ORGReq041 request = ORGReq041.builder().angkorId("akykre1680848624").build();

        //when
        ResultActions result = mvc.perform(get("/ankochat/api/address/getAllAddressList/akNcB81683524397")
                .header("Uuid", "0f47fc38-5dbb-4351-8c6c-ad507405e573")
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("{class-name}/{method-name}", preprocessResponse(prettyPrint()),
                                responseFields(
                                        List.of(
                                                fieldWithPath("data[].friend_angkorId").optional().type(JsonFieldType.STRING).description("친구 AngkorId"),
                                                fieldWithPath("data[].friend_profileMessage").optional().type(JsonFieldType.STRING).description("친구 프로필 상태 메세지").optional(),
                                                fieldWithPath("data[].friend_phonenumber").optional().type(JsonFieldType.STRING).description("친구 휴대폰 번호").optional(),
                                                fieldWithPath("data[].friend_name").optional().type(JsonFieldType.STRING).description("친구 이름"),
                                                fieldWithPath("data[].friend_url").optional().type(JsonFieldType.STRING).description("친구 프로필 이미지").optional(),
                                                fieldWithPath("data[].friend_privacyProfile").optional().type(JsonFieldType.STRING).description("친구 프로필 공개 여부"),
                                                fieldWithPath("data[].friend_favorite_yn").optional().type(JsonFieldType.STRING).description("즐겨찾기 친구 여부"),
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
    void 친구_즐겨찾기_추가() throws Exception {

        //given
        ORGReq018 request = ORGReq018.builder().angkorId("akOYHx1683105988").friend_angkorId("akSY591683184131").build();

        //when
        ResultActions result = mvc.perform(post("/ankochat/api/address/setFavorite")
                .header("Uuid", "f66c863a-b7b2-4cb8-9383-b33847c5f2f4")
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
                                                fieldWithPath("data.status").optional().type(JsonFieldType.STRING).description("즐겨찾기 여부 결과"),
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
    void 친구_즐겨찾기_리스트_확인() throws Exception {

        //given
        //ORGReq041 request = ORGReq041.builder().angkorId("akykre1680848624").build();

        //when
        ResultActions result = mvc.perform(get("/ankochat/api/address/getFavoriteAddressList/akNcB81683524397")
                .header("Uuid", "0f47fc38-5dbb-4351-8c6c-ad507405e573")
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("{class-name}/{method-name}", preprocessResponse(prettyPrint()),
                                responseFields(
                                        List.of(
                                                fieldWithPath("data[].friend_angkorId").optional().type(JsonFieldType.STRING).description("즐겨찾기 친구 AngkorId"),
                                                fieldWithPath("data[].friend_profileMessage").optional().type(JsonFieldType.STRING).description("즐겨찾기 친구 프로필 상태 메세지").optional(),
                                                fieldWithPath("data[].friend_phonenumber").optional().type(JsonFieldType.STRING).description("즐겨찾기 친구 휴대폰 번호").optional(),
                                                fieldWithPath("data[].friend_name").optional().type(JsonFieldType.STRING).description("즐겨찾기 친구 이름"),
                                                fieldWithPath("data[].friend_url").optional().type(JsonFieldType.STRING).description("친구 프로필 이미지").optional(),
                                                fieldWithPath("data[].friend_privacyProfile").optional().type(JsonFieldType.STRING).description("친구 프로필 공개 여부"),
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
    void 프로필_상세정보_본인_확인() throws Exception {

        //given
        //ORGReq013 request = ORGReq013.builder().angkorId("akNsHv1681895437").build();

        //when
        ResultActions result = mvc.perform(get("/ankochat/api/address/getMyAddressInfo/akOYHx1683105988")
                .header("Uuid", "f66c863a-b7b2-4cb8-9383-b33847c5f2f4")
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("{class-name}/{method-name}", preprocessResponse(prettyPrint()),
                                responseFields(
                                        List.of(
                                                fieldWithPath("data.angkorId").optional().type(JsonFieldType.STRING).description("사용자 AngkorId"),
                                                fieldWithPath("data.profileMessage").optional().type(JsonFieldType.STRING).description("사용자 프로필 상태 메세지").optional(),
                                                fieldWithPath("data.phonecode").optional().type(JsonFieldType.STRING).description("국가전화 코드"),
                                                fieldWithPath("data.phonenumber").optional().type(JsonFieldType.STRING).description("국가코드 포함 휴대폰 번호"),
                                                fieldWithPath("data.name").optional().type(JsonFieldType.STRING).description("사용자 이름"),
                                                fieldWithPath("data.profileUrl").optional().type(JsonFieldType.STRING).description("사용자 프로필 이미지").optional(),
                                                fieldWithPath("data.privacy_profile").optional().type(JsonFieldType.BOOLEAN).description("사용자 프로필 공개 여부"),
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
    void 프로필_상세정보_친구_확인() throws Exception {

        //given
        //ORGReq013 request = ORGReq013.builder().angkorId("akNsHv1681895437").build();

        //when
        ResultActions result = mvc.perform(get("/ankochat/api/address/getFriendAddressInfo/akB2ho1689336875")
                .header("Uuid", "ca548187-145d-4a5b-9f52-216a53435029")
                .queryParam("Friend_angkorId","akmV1n1688703732")
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("{class-name}/{method-name}", preprocessResponse(prettyPrint()),
                                responseFields(
                                        List.of(
                                                fieldWithPath("data.friend_angkorId").optional().type(JsonFieldType.STRING).description("친구 AngkorId"),
                                                fieldWithPath("data.friend_profileMessage").optional().type(JsonFieldType.STRING).description("친구 프로필 상태 메세지").optional(),
                                                fieldWithPath("data.friend_phonecode").optional().type(JsonFieldType.STRING).description("친구 국가 전화코드"),
                                                fieldWithPath("data.friend_phonenumber").optional().type(JsonFieldType.STRING).description("친구 국가코드 포함 휴대폰 번호"),
                                                fieldWithPath("data.friend_name").optional().type(JsonFieldType.STRING).description("친구 이름"),
                                                fieldWithPath("data.friend_url").optional().type(JsonFieldType.STRING).description("친구 프로필 이미지").optional(),
                                                fieldWithPath("data.friend_privacyProfile").optional().type(JsonFieldType.BOOLEAN).description("친구 프로필 공개 여부"),
                                                fieldWithPath("data.friend_favorite_yn").optional().type(JsonFieldType.STRING).description("즐겨찾기 여부"),
                                                fieldWithPath("data.friend_block_yn").optional().type(JsonFieldType.STRING).description("차단 여부"),
                                                fieldWithPath("data.friend_yn").optional().type(JsonFieldType.STRING).description("나와의 친구 여부"),
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
    void 친구_차단_추가() throws Exception {

        //given
        ORGReq018 request = ORGReq018.builder().angkorId("akOYHx1683105988").friend_angkorId("akSY591683184131").build();

        //when
        ResultActions result = mvc.perform(post("/ankochat/api/address/setBlock")
                .header("Uuid", "f66c863a-b7b2-4cb8-9383-b33847c5f2f4")
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
                                                fieldWithPath("data.status").optional().type(JsonFieldType.STRING).description("차단 여부 결과"),
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
    void 내_프로필_검색_허용() throws Exception {

        //given
        ORGReq022 request = ORGReq022.builder().angkorId("akOYHx1683105988").showProfileYn(true).build();

        //when
        ResultActions result = mvc.perform(post("/ankochat/api/address/setShareProfile")
                .header("Uuid", "f66c863a-b7b2-4cb8-9383-b33847c5f2f4")
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
    void 근처_사용자_리스트_확인() throws Exception {

        //given
        //ORGReq041 request = ORGReq041.builder().angkorId("akykre1680848624").build();

        //when
        ResultActions result = mvc.perform(get("/ankochat/api/address/getAddressListNearBy/akOYHx1683105988")
                .header("Uuid", "f66c863a-b7b2-4cb8-9383-b33847c5f2f4")
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("{class-name}/{method-name}", preprocessResponse(prettyPrint()),
                                responseFields(
                                        List.of(
                                                fieldWithPath("data[].friend_angkorId").optional().type(JsonFieldType.STRING).description("상대방 AngkorId"),
                                                fieldWithPath("data[].friend_profileMessage").optional().type(JsonFieldType.STRING).description("상대방 프로필 상태 메세지").optional(),
                                                fieldWithPath("data[].friend_phonecode").optional().type(JsonFieldType.STRING).description("상대방 국가 번호코드"),
                                                fieldWithPath("data[].friend_phonenumber").optional().type(JsonFieldType.STRING).description("상대방 국가코드 포함 휴대폰 번호"),
                                                fieldWithPath("data[].friend_name").optional().type(JsonFieldType.STRING).description("상대방 이름"),
                                                fieldWithPath("data[].friend_url").optional().type(JsonFieldType.STRING).description("상대방 프로필 이미지").optional(),
                                                fieldWithPath("data[].friend_privacyProfile").optional().type(JsonFieldType.BOOLEAN).description("상대방 프로필 공개여부"),
                                                fieldWithPath("data[].friend_distance").optional().type(JsonFieldType.NUMBER).description("나와 상대방과의 거리"),
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
    void 전화번호_사용자_검색() throws Exception {

        //given
        //ORGReq041 request = ORGReq041.builder().angkorId("akykre1680848624").build();

        //when
        ResultActions result = mvc.perform(get("/ankochat/api/address/getAddressInfoByPhone/akOYHx1683105988")
                .header("Uuid", "f66c863a-b7b2-4cb8-9383-b33847c5f2f4")
                .queryParam("phoneNumber","821043536236")
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("{class-name}/{method-name}", preprocessResponse(prettyPrint()),
                                responseFields(
                                        List.of(
                                                fieldWithPath("data.friend_angkorId").optional().type(JsonFieldType.STRING).description("친구 AngkorId"),
                                                fieldWithPath("data.friend_profileMessage").optional().type(JsonFieldType.STRING).description("친구 프로필 상태 메세지").optional(),
                                                fieldWithPath("data.friend_phonecode").optional().type(JsonFieldType.STRING).description("친구 국가 전화코드"),
                                                fieldWithPath("data.friend_phonenumber").optional().type(JsonFieldType.STRING).description("친구 국가코드 포함 휴대폰 번호"),
                                                fieldWithPath("data.friend_name").optional().type(JsonFieldType.STRING).description("친구 이름"),
                                                fieldWithPath("data.friend_url").optional().type(JsonFieldType.STRING).description("친구 프로필 이미지").optional(),
                                                fieldWithPath("data.friend_privacyProfile").optional().type(JsonFieldType.BOOLEAN).description("친구 프로필 공개 여부"),
                                                fieldWithPath("data.friend_favorite_yn").optional().type(JsonFieldType.STRING).description("즐겨찾기 여부"),
                                                fieldWithPath("data.friend_block_yn").optional().type(JsonFieldType.STRING).description("차단 여부"),
                                                fieldWithPath("data.friend_yn").optional().type(JsonFieldType.STRING).description("나와의 친구 여부"),
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
    void 프로필_이미지_리스트_확인() throws Exception {

        //given
        //ORGReq041 request = ORGReq041.builder().angkorId("akykre1680848624").build();

        //when
        ResultActions result = mvc.perform(get("/ankochat/api/address/getProfilesImage/akB2ho1689336875")
                .header("Uuid", "ca548187-145d-4a5b-9f52-216a53435029")
                .queryParam("Friend_angkorId","akmV1n1688703732")
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("{class-name}/{method-name}", preprocessResponse(prettyPrint()),
                                responseFields(
                                        List.of(
                                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("결과 데이터").optional(),
                                                fieldWithPath("data[].friend_url").type(JsonFieldType.STRING).description("프로필 이미지").optional(),
                                                fieldWithPath("data[].hide_yn").type(JsonFieldType.STRING).description("프로필 이미지 숨김 여부").optional(),
                                                fieldWithPath("data[].seq").type(JsonFieldType.NUMBER).description("프로필 이미지 seq").optional(),
                                                fieldWithPath("data[].privacyProfile").type(JsonFieldType.BOOLEAN).description("상대방 Privacy Profile 여부 확인").optional(),
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
    void 친구_삭제() throws Exception {

        //given
        ORGReq018 request = ORGReq018.builder().angkorId("akOYHx1683105988").friend_angkorId("akd6Kw1682660046").build();

        //when
        ResultActions result = mvc.perform(delete("/ankochat/api/address/dtAddress/akOYHx1683105988")
                .header("Uuid", "f66c863a-b7b2-4cb8-9383-b33847c5f2f4")
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
    void 친구_추천_리스트() throws Exception {

        //given
        //ORGReq041 request = ORGReq041.builder().angkorId("akykre1680848624").build();

        //when
        ResultActions result = mvc.perform(get("/ankochat/api/address/getSuggestAddressList/akOYHx1683105988")
                .header("Uuid", "f66c863a-b7b2-4cb8-9383-b33847c5f2f4")
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("{class-name}/{method-name}", preprocessResponse(prettyPrint()),
                                responseFields(
                                        List.of(
                                                fieldWithPath("data[].friend_angkorId").optional().type(JsonFieldType.STRING).description("추천 친구 AngkorId"),
                                                fieldWithPath("data[].friend_profileMessage").optional().type(JsonFieldType.STRING).description("추천 친구 프로필 상태 메세지").optional(),
                                                fieldWithPath("data[].friend_phonenumber").optional().type(JsonFieldType.STRING).description("추천 친구 휴대폰 번호").optional(),
                                                fieldWithPath("data[].friend_name").optional().type(JsonFieldType.STRING).description("추천 친구 이름"),
                                                fieldWithPath("data[].friend_url").optional().type(JsonFieldType.STRING).description("추천 친구 프로필 이미지").optional(),
                                                fieldWithPath("data[].friend_privacyProfile").optional().type(JsonFieldType.STRING).description("추천 친구 프로필 공개 여부"),
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
    void 친구_차단_리스트() throws Exception {

        //given
        //ORGReq041 request = ORGReq041.builder().angkorId("akykre1680848624").build();

        //when
        ResultActions result = mvc.perform(get("/ankochat/api/address/getBlockAddressList/akNcB81683524397")
                .header("Uuid", "0f47fc38-5dbb-4351-8c6c-ad507405e573")
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("{class-name}/{method-name}", preprocessResponse(prettyPrint()),
                                responseFields(
                                        List.of(
                                                fieldWithPath("data[].friend_angkorId").optional().type(JsonFieldType.STRING).description("차단 친구 AngkorId"),
                                                fieldWithPath("data[].friend_profileMessage").optional().type(JsonFieldType.STRING).description("차단 친구 프로필 상태 메세지").optional(),
                                                fieldWithPath("data[].friend_phonenumber").optional().type(JsonFieldType.STRING).description("차단 친구 휴대폰 번호").optional(),
                                                fieldWithPath("data[].friend_name").optional().type(JsonFieldType.STRING).description("차단 친구 이름"),
                                                fieldWithPath("data[].friend_url").optional().type(JsonFieldType.STRING).description("차단 친구 프로필 이미지").optional(),
                                                fieldWithPath("data[].friend_privacyProfile").optional().type(JsonFieldType.STRING).description("차단 친구 프로필 공개 여부"),
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