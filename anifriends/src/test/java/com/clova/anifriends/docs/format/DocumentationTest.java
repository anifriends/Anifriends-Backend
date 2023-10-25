package com.clova.anifriends.docs.format;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;

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
        resultActions.andDo(restDocs.document(
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
        ResultActions resultActions = mockMvc.perform(get("/test/docs/format/optional-param")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));

        //then
        resultActions.andDo(restDocs.document(
            requestFields(
                fieldWithPath("required").type(STRING).description("필수값"),
                fieldWithPath("optional").type(STRING).description("옵셔널").optional()
            )
        ));
    }
}
