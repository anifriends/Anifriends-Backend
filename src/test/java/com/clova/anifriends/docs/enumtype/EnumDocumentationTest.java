package com.clova.anifriends.docs.enumtype;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clova.anifriends.domain.common.EnumType;
import com.clova.anifriends.base.BaseControllerTest;
import java.util.Arrays;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadSubsectionExtractor;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("enum 문서화")
public class EnumDocumentationTest extends BaseControllerTest {

    private static final String TITLE = "title";  // enum의 문서화에 사용되는 키 값

    @Test
    @DisplayName("정상 작동 테스트")
    void test() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/test/docs/enum/test")
            .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                enumResponseFields("enum-response",  // 스니펫 파일의 이름
                    attributes(key(TITLE).value("enum 문서화 테스트")),  // 문서화한 enum의 타이틀
                    null,  // API 응답을 감싸서 전달하는 경우 beneathPath("data").withSubsectionId("providers") 추가할 것
                    enumConvertFieldDescriptor(TestEnum.values()))));
    }

    private FieldDescriptor[] enumConvertFieldDescriptor(EnumType[] enumTypes) {
        return Arrays.stream(enumTypes)
            .map(enumType -> fieldWithPath(enumType.getName()).description(enumType.getValue()))
            .toArray(FieldDescriptor[]::new);
    }

    public static EnumResponseFieldsSnippet enumResponseFields(String type,
        Map<String, Object> attributes,
        PayloadSubsectionExtractor<?> subsectionExtractor,
        FieldDescriptor... descriptors) {
        return new EnumResponseFieldsSnippet(type, Arrays.asList(descriptors), attributes,
            true, subsectionExtractor);
    }
}
