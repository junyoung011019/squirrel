package com.zzm.word_service.service;

import com.zzm.word_service.DTO.PresignedUrlResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WordService {
    private final S3Service s3Service;

    // 클라이언트가 S3에 직접 파일을 업로드할 수 있는, 시간제한이 있는 Pre-signed URL을 생성
    public PresignedUrlResponseDto getUploadPresignedUrl(String extension) {

        return s3Service.getUploadPresignedUrl(extension);
    }



}
