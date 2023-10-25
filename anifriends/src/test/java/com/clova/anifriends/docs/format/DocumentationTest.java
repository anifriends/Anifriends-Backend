package com.clova.anifriends.docs.format;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clova.anifriends.base.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("문서화 테스트")
class DocumentationTest extends BaseControllerTest {


    @Test
    @DisplayName("쿼리 필수값 테스트")
    void formatOptionalParam() throws Exception {
        //given
        //when
        ResultActions resultActions = mockMvc.perform(get("/test/docs/format/optional-param")
            .param("required", "필수값"));

        //then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
            queryParameters(
                parameterWithName("required").description("필수값"),
                parameterWithName("optional").description("옵셔널").optional()
            )
        ));
    }

    @Test
    @DisplayName("바디 필수값 테스트")
    void formatOptionalBody() throws Exception {
        //given
        TestRequest request = new TestRequest("필수값", null);

        //when
        ResultActions resultActions = mockMvc.perform(get("/test/docs/format/optional-body")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));

        //then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
            requestFields(
                fieldWithPath("required").type(STRING).description("필수값"),
                fieldWithPath("optional").type(STRING).description("옵셔널").optional()
            )
        ));
    }

    @Test
    @DisplayName("쿼리 제약 필드 테스트")
    void formatConstraintsParam() throws Exception {
        //given
        //when
        ResultActions resultActions = mockMvc.perform(get("/test/docs/format/optional-param")
            .param("required", "필수값")
            .param("optional", "옵셔널"));

        //then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
            queryParameters(
                parameterWithName("required").description("필수값")
                    .attributes(DocumentationFormatGenerator.getConstraint("1자 이상, 20자 이하")),
                parameterWithName("optional").description("옵셔널").optional()
            )
        ));
    }

    @Test
    @DisplayName("바디 제약 필드 테스트")
    void formatConstraintsBody() throws Exception {
        //given
        TestRequest request = new TestRequest("필수값", "옵셔널");

        //when
        ResultActions resultActions = mockMvc.perform(get("/test/docs/format/optional-body")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));

        //then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
            requestFields(
                fieldWithPath("required").type(STRING).description("필수값")
                    .attributes(DocumentationFormatGenerator.getConstraint("1자 이상, 20자 이하")),
                fieldWithPath("optional").type(STRING).description("옵셔널").optional()
            )
        ));
    }

    @Test
    @DisplayName("날짜 제약 필드 테스트")
    void formatConstraintsDate() throws Exception {
        //given
        //when
        ResultActions resultActions = mockMvc.perform(get("/test/docs/format/date")
            .param("date", "2023-11-01"));

        //then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
            queryParameters(
                parameterWithName("date").description("날짜")
                    .attributes(DocumentationFormatGenerator.getDateConstraint())
            )
        ));
    }

    @Test
    @DisplayName("날짜 시각 제약 필드 테스트")
    void formatConstraintsDatetime() throws Exception {
        //given
        //when
        ResultActions resultActions = mockMvc.perform(get("/test/docs/format/datetime")
            .param("datetime", "2023-11-01T11:11:11"));

        //then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
            queryParameters(
                parameterWithName("datetime").description("날짜 시각")
                    .attributes(DocumentationFormatGenerator.getDatetimeConstraint())
            )
        ));
    }
}
