package com.softpuzzle.angkor.controller.api.chatting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softpuzzle.angkor.http.request.ORGReq029;
import com.softpuzzle.angkor.http.request.ORGReq039;
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
public class ChattingControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 그룹_이모티콘_리스트() throws Exception {

        //given
        //ORGReq013 request = ORGReq013.builder().angkorId("akykre1680848624").build();

        //when
        ResultActions result = mvc.perform(get("/ankochat/api/chatting/getParentEmoticonList/ak8rhg1689335044")
                .header("Uuid", "ca548187-145d-4a5b-9f52-216a53435029")
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("{class-name}/{method-name}", preprocessResponse(prettyPrint()),
                                responseFields(
                                        List.of(
                                                fieldWithPath("data[].emoti_seq").type(JsonFieldType.NUMBER).description("emoticon seq").optional(),
                                                fieldWithPath("data[].emoti_name").type(JsonFieldType.STRING).description("emoticon 이름").optional(),
                                                fieldWithPath("data[].emoti_url").type(JsonFieldType.STRING).description("emoticon 대표 이미지").optional(),
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
    void 선택한_그룹_이모티콘_리스트() throws Exception {

        //given
        //ORGReq013 request = ORGReq013.builder().angkorId("akykre1680848624").build();

        //when
        ResultActions result = mvc.perform(get("/ankochat/api/chatting/getChildEmoticonList/akICt71686138631")
                .header("Uuid", "95aef0c9-d1f5-4800-b3f8-8d042a595ae6")
                .queryParam("EmotiSeq","1009")
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("{class-name}/{method-name}", preprocessResponse(prettyPrint()),
                                responseFields(
                                        List.of(
                                                fieldWithPath("data[].emoti_ord").type(JsonFieldType.NUMBER).description("선택한 그룹 emoticon의 하위 리스트 순번").optional(),
                                                fieldWithPath("data[].emoti_url").type(JsonFieldType.STRING).description("선택한 그룹 emoticon의 하위 리스트 이미지").optional(),
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
    void 유저_공용키_조회() throws Exception {

        //given
        //ORGReq013 request = ORGReq013.builder().angkorId("akykre1680848624").build();

        //when
        ResultActions result = mvc.perform(get("/ankochat/api/chatting/getPublicKeyList/akICt71686138631")
                .header("Uuid", "95aef0c9-d1f5-4800-b3f8-8d042a595ae6")
                .queryParam("userList", "akfVSu1686136623,akOXTY1685969755")
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("{class-name}/{method-name}", preprocessResponse(prettyPrint()),
                                responseFields(
                                        List.of(
                                                fieldWithPath("data[].profileUrl").type(JsonFieldType.STRING).description("각 유저의 프로필 URL").optional(),
                                                fieldWithPath("data[].angkorId").type(JsonFieldType.STRING).description("각 유저의 앙코르챗 ID"),
                                                fieldWithPath("data[].nickname").type(JsonFieldType.STRING).description("각 유저의 닉네임"),
                                                fieldWithPath("data[].publicKey").type(JsonFieldType.STRING).description("각 유저의 공용키"),
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
    void 비밀채팅_그룹키_생성() throws Exception {

        //given
        ORGReq029 request = ORGReq029.builder().angkorId("akICt71686138631").userList(new String[]{"akfVSu1686136623"}).build();

        //when
        ResultActions result = mvc.perform(post("/ankochat/api/chatting/setSecertKey")
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
                                                fieldWithPath("data.groupPrivateKey[].privateKey").type(JsonFieldType.STRING).description("그룹의 각 개인별 개인키"),
                                                fieldWithPath("data.groupPrivateKey[].angkorId").type(JsonFieldType.STRING).description("그룹의 각 개인별 앙코르챗 ID"),
                                                fieldWithPath("data.groupPublicKey").type(JsonFieldType.STRING).description("그룹 공용키"),
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
    void 그룹초대_가능_여부() throws Exception {

        //given
        ORGReq039 request = ORGReq039.builder().angkorId("ak8rhg1689335044").userList(new String[]{"akB2ho1689336875", "akuG9R1689323141"}).build();

        //when
        ResultActions result = mvc.perform(post("/ankochat/api/chatting/getGroupList/ak8rhg1689335044")
                .header("Uuid", "ca548187-145d-4a5b-9f52-216a53435029")
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
                                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("결과 데이터").optional(),
                                                fieldWithPath("data[].friendAngkorId").type(JsonFieldType.STRING).description("angkorId"),
                                                fieldWithPath("data[].friend_group").type(JsonFieldType.NUMBER).description("나를 그룹채팅방에 초대 할 수 있는 범위 설정 [1:everybody,2:My friends,3:Nobody]"),
                                                fieldWithPath("data[].friend_yn").type(JsonFieldType.STRING).description("상대방이 나를 친구로 등록했는지의 여부"),
                                                fieldWithPath("data[].friend_block_yn").type(JsonFieldType.STRING).description("상대방이 나를 차단했는지의 여부"),
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

}