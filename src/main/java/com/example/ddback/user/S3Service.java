package com.example.ddback.user;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class S3Service {
    @Autowired
    private ImageRepository imageRepository;

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public Image upload(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(multipartFile.getContentType());
        metadata.setContentLength(multipartFile.getSize()); // 파일의 크기를 명시적으로 지정
        InputStream inputStream = null;
        try {
            inputStream = multipartFile.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        PutObjectRequest request = new PutObjectRequest(bucket, fileName, inputStream, metadata);
        PutObjectResult result = amazonS3.putObject(request);
        String fileUrl = amazonS3.getUrl(bucket, fileName).toString();

        Image image = new Image(null, fileUrl);
        Image saved = imageRepository.save(image);

        return saved;
    }
}
