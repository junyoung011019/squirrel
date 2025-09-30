package com.zzm.word_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.rekognition.model.S3Object;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Presigner s3Presigner;
    private final S3Client s3Client;
    private static final String bucket = "temp";

    /* ----------------------------------테스트 용-------------------------------------*/
    // 버킷 생성
    public void createBucket() {
        try {
            ListBucketsResponse listBucketsResponse = s3Client.listBuckets();
            boolean exists = listBucketsResponse.buckets().stream()
                    .anyMatch(b -> b.name().equals(bucket));
            if (!exists) {
                s3Client.createBucket(CreateBucketRequest.builder().bucket(bucket).build());
                System.out.println("Bucket 생성 완료");
            } else {
                System.out.println("Bucket 이미 존재, 생성하지 않음");
            }
        } catch (Exception e) {
            System.out.println("버킷 생성 중 예외 발생: " + e.getMessage());
        }
    }
    // 이미지 업로드
    public String uploadImageFile(MultipartFile multipartFile) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        String fileKey = UUID.randomUUID() + "-" + fileName;

        // S3에 업로드
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(fileKey)
                        .contentType(multipartFile.getContentType()) // MIME 타입 지정(파일 형식)
                        .build(),
                RequestBody.fromBytes(multipartFile.getBytes()) // 바이트 단위 업로드
        );
        System.out.println("S3 업로드 완료: " + fileKey);
        return fileKey; // LocalStack 테스트용 URL
    }
    // 이미지 업로드 시 람다 트리거 대체
    public S3Object triggerS3(String fileKey) {

        S3Object s3Object = S3Object.builder()
                .bucket(bucket)
                .name(fileKey)
                .build();

        return s3Object;

    }

    /* ------------------------------------------------------------------------------*/

}
