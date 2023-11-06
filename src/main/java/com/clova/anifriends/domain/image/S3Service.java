package com.clova.anifriends.domain.image;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;
    private static final String FOLDER = "images";

    public List<String> uploadImages(List<MultipartFile> multipartFileList) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        List<String> list = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFileList) {
            String fileName = createFileName(multipartFile.getOriginalFilename());
            objectMetadata.setContentLength(multipartFile.getSize());
            objectMetadata.setContentType(multipartFile.getContentType());

            try (InputStream inputStream = multipartFile.getInputStream()) {
                amazonS3.putObject(
                    new PutObjectRequest(bucket + "/" + FOLDER + "/image", fileName, inputStream,
                        objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
                list.add(amazonS3.getUrl(bucket + "/" + FOLDER + "/image", fileName).toString());
            } catch (IOException e) {
                throw new S3BadRequestException("S3에 이미지를 업로드하는데 실패했습니다.");
            }
        }
        return list;
    }

    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    private String getFileExtension(String fileName) {
        if (fileName.length() == 0) {
            throw new S3BadRequestException("잘못된 파일입니다.");
        }
        ArrayList<String> fileValidate = new ArrayList<>();
        fileValidate.add(".jpg");
        fileValidate.add(".jpeg");
        fileValidate.add(".png");
        fileValidate.add(".JPG");
        fileValidate.add(".JPEG");
        fileValidate.add(".PNG");
        String idxFileName = fileName.substring(fileName.lastIndexOf("."));
        if (!fileValidate.contains(idxFileName)) {
            throw new S3BadRequestException("잘못된 파일 형식입니다.");
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
