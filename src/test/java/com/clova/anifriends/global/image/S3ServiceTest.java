package com.clova.anifriends.global.image;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.amazonaws.services.s3.AmazonS3;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {

    @InjectMocks
    private S3Service s3Service;

    @Mock
    private AmazonS3 amazonS3;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(s3Service, "bucket", "bucket-name");
    }

    @Nested
    @DisplayName("uploadImages 메서드 실행 시")
    class UploadImagesTest {

        @Test
        @DisplayName("성공")
        void testUploadImages() {
            // given
            MockMultipartFile file1 = new MockMultipartFile("file1", "test1.jpg", "image/jpeg",
                "file content".getBytes());
            MockMultipartFile file2 = new MockMultipartFile("file2", "test2.jpg", "image/jpeg",
                "file content".getBytes());
            List<String> expectedUrls = Arrays.asList(
                "https://example.com/bucket-name/images/random-uuid.jpg",
                "https://example.com/bucket-name/images/random-uuid.jpg"
            );

            when(amazonS3.putObject(any())).thenReturn(null);
            when(amazonS3.getUrl(any(), any()))
                .thenAnswer(invocation -> {
                    String bucketName = invocation.getArgument(0);
                    String fileName = invocation.getArgument(1);
                    return new java.net.URL("https", "example.com",
                        "/" + bucketName + "/" + fileName);
                });

            // when
            List<String> uploadedUrls = s3Service.uploadImages(Arrays.asList(file1, file2));

            // then
            assertThat(uploadedUrls.size()).isEqualTo(expectedUrls.size());
        }

        @Test
        @DisplayName("예외(S3BadRequestException): 파일이 확장자가 잘못된 경우")
        void throwExceptionWhenFileExtensionIsWrong() {
            // given
            MockMultipartFile file1 = new MockMultipartFile("file1", "test1.abcd", "image/jpeg",
                "file content".getBytes());
            MockMultipartFile file2 = new MockMultipartFile("file2", "test2.sdf", "image/jpeg",
                "file content".getBytes());

            // when
            Exception exception = catchException(
                () -> s3Service.uploadImages(Arrays.asList(file1, file2))
            );

            // then
            assertThat(exception).isInstanceOf(S3BadRequestException.class);
        }

        @Test
        @DisplayName("예외(S3BadRequestException): 파일 이름 길이가 0인 경우")
        void throwExceptionWhenFileNameLengthIsZero() {
            // given
            MockMultipartFile file1 = new MockMultipartFile("file1", "", "image/jpeg",
                "file content".getBytes());
            MockMultipartFile file2 = new MockMultipartFile("file2", "", "image/jpeg",
                "file content".getBytes());

            // when
            Exception exception = catchException(
                () -> s3Service.uploadImages(Arrays.asList(file1, file2))
            );

            // then
            assertThat(exception).isInstanceOf(S3BadRequestException.class);
        }
    }

    @Nested
    @DisplayName("deleteImages 메서드 실행 시")
    class DeleteImagesTest {

        @Test
        @DisplayName("성공")
        void testDeleteImages() {
            // given
            List<String> imageUrlsToDelete = List.of(
                "https://example.com/bucket-name/images/random-uuid.jpg",
                "https://example.com/bucket-name/images/random-uuid.jpg"
            );

            // when
            s3Service.deleteImages(imageUrlsToDelete);

            // then
            verify(amazonS3, times(imageUrlsToDelete.size())).deleteObject(any(), any());
        }
    }
}
