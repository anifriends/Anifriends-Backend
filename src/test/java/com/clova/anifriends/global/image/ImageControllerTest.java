package com.clova.anifriends.global.image;

import static java.sql.JDBCType.ARRAY;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clova.anifriends.base.BaseControllerTest;
import com.clova.anifriends.docs.format.DocumentationFormatGenerator;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

class ImageControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("이미지 업로드 API 호출 시")
    void uploadImages() throws Exception {
        // given
        List<MultipartFile> imageFiles = List.of(
            new MockMultipartFile("test1", "test1.PNG", MediaType.IMAGE_PNG_VALUE, "test1".getBytes()),
            new MockMultipartFile("test2", "test2.PNG", MediaType.IMAGE_PNG_VALUE, "test2".getBytes())
        );

        // when
        ResultActions resultActions = mockMvc.perform(
            multipart("/api/images")
                .file("images", imageFiles.get(0).getBytes())
                .file("images", imageFiles.get(1).getBytes())
                .with(requestPostProcessor -> {
                    requestPostProcessor.setMethod("POST");
                    return requestPostProcessor;
                })
                .contentType(MediaType.MULTIPART_FORM_DATA));

        // then
        resultActions.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestParts(
                    partWithName("images").description("이미지 파일")
                        .attributes(DocumentationFormatGenerator.getConstraint("이미지 파일은 1 이상 5이하"))
                ),
                responseFields(
                    fieldWithPath("imageUrls").type(ARRAY).description("이미지 URL 목록")
                )
            ));
    }
}
