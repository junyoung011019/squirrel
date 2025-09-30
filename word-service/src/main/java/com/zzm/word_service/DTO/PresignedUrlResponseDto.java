package com.zzm.word_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PresignedUrlResponseDto {
    private String presignedUrl; // S3에 업로드할 수 있는 임시 URL
    private String fileKey;
    private String scenarioId;
}
