package com.clova.anifriends.docs.enumtype;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clova.anifriends.domain.animal.vo.AnimalActive;
import com.clova.anifriends.domain.animal.vo.AnimalGender;
import com.clova.anifriends.domain.animal.vo.AnimalType;
import com.clova.anifriends.domain.applicant.vo.ApplicantStatus;
import com.clova.anifriends.domain.common.EnumType;
import com.clova.anifriends.base.BaseControllerTest;
import com.clova.anifriends.domain.volunteer.vo.VolunteerGender;
import com.clova.anifriends.global.exception.ErrorCode;
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

    @Test
    @DisplayName("AnimalActive 문서화")
    void animalActive() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/test/docs/enum/animal/active")
            .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                enumResponseFields("enum-response",  // 스니펫 파일의 이름
                    attributes(key(TITLE).value("보호 동물 성격")),  // 문서화한 enum의 타이틀
                    null,  // API 응답을 감싸서 전달하는 경우 beneathPath("data").withSubsectionId("providers") 추가할 것
                    enumConvertFieldDescriptor(AnimalActive.values()))));
    }

    @Test
    @DisplayName("AnimalGender 문서화")
    void animalGender() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/test/docs/enum/animal/gender")
            .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                enumResponseFields("enum-response",  // 스니펫 파일의 이름
                    attributes(key(TITLE).value("보호 동물 성별")),  // 문서화한 enum의 타이틀
                    null,  // API 응답을 감싸서 전달하는 경우 beneathPath("data").withSubsectionId("providers") 추가할 것
                    enumConvertFieldDescriptor(AnimalGender.values()))));
    }

    @Test
    @DisplayName("AnimalType 문서화")
    void animalType() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/test/docs/enum/animal/type")
            .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                enumResponseFields("enum-response",  // 스니펫 파일의 이름
                    attributes(key(TITLE).value("보호 동물 종류")),  // 문서화한 enum의 타이틀
                    null,  // API 응답을 감싸서 전달하는 경우 beneathPath("data").withSubsectionId("providers") 추가할 것
                    enumConvertFieldDescriptor(AnimalType.values()))));
    }

    @Test
    @DisplayName("ApplicantStatus 문서화")
    void applicantStatus() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/test/docs/enum/applicant/status")
            .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                enumResponseFields("enum-response",  // 스니펫 파일의 이름
                    attributes(key(TITLE).value("봉사 신청자 상태")),  // 문서화한 enum의 타이틀
                    null,  // API 응답을 감싸서 전달하는 경우 beneathPath("data").withSubsectionId("providers") 추가할 것
                    enumConvertFieldDescriptor(ApplicantStatus.values()))));
    }

    @Test
    @DisplayName("VolunteerGender 문서화")
    void volunteerGender() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/test/docs/enum/volunteer/gender")
            .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                enumResponseFields("enum-response",  // 스니펫 파일의 이름
                    attributes(key(TITLE).value("봉사자 성별")),  // 문서화한 enum의 타이틀
                    null,  // API 응답을 감싸서 전달하는 경우 beneathPath("data").withSubsectionId("providers") 추가할 것
                    enumConvertFieldDescriptor(VolunteerGender.values()))));
    }

    private FieldDescriptor[] enumConvertFieldDescriptor(EnumType[] enumTypes) {
        return Arrays.stream(enumTypes)
            .map(enumType -> fieldWithPath(enumType.getName()).description(enumType.getName()))
            .toArray(FieldDescriptor[]::new);
    }

    private FieldDescriptor[] errorCodeConvertFieldDescriptor(ErrorCode[] errorCodes) {
        return Arrays.stream(errorCodes)
            .map(errorCode -> fieldWithPath(errorCode.getName()).description(errorCode.getValue()))
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
