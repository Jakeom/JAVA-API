package com.softpuzzle.angkor.controller.api.helpcenter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

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
public class HelpCenterControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 공지사항() throws Exception {

        //when
        ResultActions result = mvc.perform(get("/ankochat/api/helpcenter/notice/akfNRi1687419321")
                .header("Uuid", "8481aa30-7c72-469d-82f8-37f49635b398")
                .header("Language", "en")
                .queryParam("paging.page","1")
                .queryParam("paging.pagingSize","20")
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("{class-name}/{method-name}", preprocessResponse(prettyPrint()),
                                responseFields(
                                        List.of(
                                                fieldWithPath("data[].seq").type(JsonFieldType.NUMBER).description("공지사항 순번"),
                                                fieldWithPath("data[].title").type(JsonFieldType.STRING).description("공지사항 타이틀"),
                                                fieldWithPath("data[].content").type(JsonFieldType.STRING).description("공지사항 컨텐츠"),
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
    void FAQ_카테고리_리스트() throws Exception {

        //when
        ResultActions result = mvc.perform(get("/ankochat/api/helpcenter/faq/category/akfNRi1687419321")
                .header("Uuid", "8481aa30-7c72-469d-82f8-37f49635b398")
                .header("Language", "en")
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("{class-name}/{method-name}", preprocessResponse(prettyPrint()),
                                responseFields(
                                        List.of(
                                                fieldWithPath("data.fCategory[].seq").type(JsonFieldType.NUMBER).description("FAQ 첫번째 카테고리 순번"),
                                                fieldWithPath("data.fCategory[].name").type(JsonFieldType.STRING).description("FAQ 첫번째 카테고리명"),
                                                fieldWithPath("data.sCategory[].seq").type(JsonFieldType.NUMBER).description("FAQ 두번째 카테고리 순번"),
                                                fieldWithPath("data.sCategory[].name").type(JsonFieldType.STRING).description("FAQ 두번째 카테고리명"),
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
    void FAQ_리스트() throws Exception {

        //when
        ResultActions result = mvc.perform(get("/ankochat/api/helpcenter/faq/akfNRi1687419321")
                .header("Uuid", "8481aa30-7c72-469d-82f8-37f49635b398")
                .header("Language", "khr")
                .queryParam("fCategory","common")
                .queryParam("keyword","new")
                .queryParam("paging.page","1")
                .queryParam("paging.pagingSize","20")
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("{class-name}/{method-name}", preprocessResponse(prettyPrint()),
                                responseFields(
                                        List.of(
                                                fieldWithPath("data[].seq").type(JsonFieldType.NUMBER).description("FAQ 순번"),
                                                fieldWithPath("data[].fCategory").type(JsonFieldType.STRING).description("FAQ 첫번째 카테고리").optional(),
                                                fieldWithPath("data[].sCategory").type(JsonFieldType.STRING).description("FAQ 두번째 카테고리").optional(),
                                                fieldWithPath("data[].title").type(JsonFieldType.STRING).description("FAQ 타이틀"),
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
    void FAQ_상세() throws Exception {

        //when
        ResultActions result = mvc.perform(get("/ankochat/api/helpcenter/faq/detail/akfNRi1687419321")
                .header("Uuid", "8481aa30-7c72-469d-82f8-37f49635b398")
                .header("Language", "khr")
                .queryParam("seq","5")
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("{class-name}/{method-name}", preprocessResponse(prettyPrint()),
                                responseFields(
                                        List.of(
                                                fieldWithPath("data.fCategory").type(JsonFieldType.STRING).description("FAQ 첫번째 카테고리"),
                                                fieldWithPath("data.sCategory").type(JsonFieldType.STRING).description("FAQ 두번째 카테고리"),
                                                fieldWithPath("data.title").type(JsonFieldType.STRING).description("FAQ 타이틀"),
                                                fieldWithPath("data.content").type(JsonFieldType.STRING).description("FAQ 컨텐츠"),
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
    void QUESTION_리스트() throws Exception {

        //when
        ResultActions result = mvc.perform(get("/ankochat/api/helpcenter/quesion/akfNRi1687419321")
                .header("Uuid", "8481aa30-7c72-469d-82f8-37f49635b398")
                .queryParam("paging.page", "1")
                .queryParam("paging.pagingSize", "20")
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("{class-name}/{method-name}", preprocessResponse(prettyPrint()),
                                responseFields(
                                        List.of(
                                                fieldWithPath("data[].seq").type(JsonFieldType.NUMBER).description("문의 순번"),
                                                fieldWithPath("data[].status").type(JsonFieldType.STRING).description("문의 답변여부"),
                                                fieldWithPath("data[].title").type(JsonFieldType.STRING).description("문의 타이틀"),
                                                fieldWithPath("data[].regDt").type(JsonFieldType.STRING).description("문의 등록날짜"),
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
    void QUESTION_상세() throws Exception {

        //when
        ResultActions result = mvc.perform(get("/ankochat/api/helpcenter/quesion/detail/akfNRi1687419321")
                .header("Uuid", "8481aa30-7c72-469d-82f8-37f49635b398")
                .queryParam("seq","9")
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("{class-name}/{method-name}", preprocessResponse(prettyPrint()),
                                responseFields(
                                        List.of(
                                                fieldWithPath("data.question.seq").type(JsonFieldType.NUMBER).description("문의 순번"),
                                                fieldWithPath("data.question.regDt").type(JsonFieldType.STRING).description("문의 날짜"),
                                                fieldWithPath("data.question.title").type(JsonFieldType.STRING).description("문의 제목"),
                                                fieldWithPath("data.question.content").type(JsonFieldType.STRING).description("문의 내용"),
                                                fieldWithPath("data.question.fileList").type(JsonFieldType.ARRAY).description("문의 첨부파일"),
                                                fieldWithPath("data.question.status").type(JsonFieldType.STRING).description("문의 답변여부"),
                                                fieldWithPath("data.answer.title").type(JsonFieldType.STRING).description("답변 제목"),
                                                fieldWithPath("data.answer.content").type(JsonFieldType.STRING).description("답변 내용"),
                                                fieldWithPath("data.answer.regDt").type(JsonFieldType.STRING).description("답변 날짜"),
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