package com.zzm.word_service.service;

import com.zzm.word_service.DTO.PresignedUrlResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.rekognition.model.S3Object;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Presigner s3Presigner;
    private final S3Client s3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 클라이언트가 S3에 직접 파일을 업로드할 수 있는, 시간제한이 있는 Pre-signed URL을 생성
    public PresignedUrlResponseDto getUploadPresignedUrl(String extension) {

        String scenarioId = UUID.randomUUID().toString();

        //1. 파일 키 생성 (폴더 구조 + scenarioId + extension)
        String fileKey = "temp" + "/" + scenarioId + extension;

        //2. S3에 파일을 올리는(PutObject) 요청을 미리 준비
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileKey)
                .build();

        //3. Pre-signed URL을 생성하기 위한 요청
        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10)) // URL 유효 시간 10분
                .putObjectRequest(putObjectRequest)
                .build();

        //4. S3 Presigner를 사용하여 최종적으로 Pre-signed URL을 생성
        String presignedUrl = s3Presigner.presignPutObject(presignRequest).url().toString();

        //5. 클라이언트에게 업로드할 URL과 업로드 후 서버에 알려줄 파일 키를 함께 반환
        return new PresignedUrlResponseDto(presignedUrl, fileKey, scenarioId);
    }

    public String getDownloadPresignedUrl(String fileKey) {
        // filekey 없는 경우
        if (fileKey == null || fileKey.isBlank()) {
            return null;
        }
        //1. S3에 파일을 다운로드할(GetObject) 요청을 미리 준비
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(fileKey)
                .build();
        //2. S3 Presigner를 사용하여 최종적으로 Pre-signed URL을 생성
        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(60))
                .getObjectRequest(getObjectRequest)
                .build();

        String presignedUrl = s3Presigner.presignGetObject(getObjectPresignRequest).url().toString();

        //3. url 반환
        return presignedUrl;
    }

}
