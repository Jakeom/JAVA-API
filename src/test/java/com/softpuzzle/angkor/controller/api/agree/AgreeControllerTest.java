package com.softpuzzle.angkor.controller.api.agree;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:/application-test.properties")
public class AgreeControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void 약관_동의() throws Exception {

        //given


        //when
        ResultActions result = mvc.perform(get("/ankochat/api/agree/agree")
                .header("Language", "en")
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("{class-name}/{method-name}", preprocessResponse(prettyPrint()),
                                responseFields(
                                        List.of(
                                                fieldWithPath("data[].ord").type(JsonFieldType.NUMBER).description("약관 순서"),
                                                fieldWithPath("data[].language").type(JsonFieldType.STRING).description("약관 언어(English:영어, Khmer:크메르어)"),
                                                fieldWithPath("data[].agree_term").type(JsonFieldType.STRING).description("약관 내용"),
                                                fieldWithPath("data[].agree_type").type(JsonFieldType.NUMBER).description("약관 필수(1) / 선택(2)"),
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