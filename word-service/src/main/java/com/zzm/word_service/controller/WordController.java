package com.zzm.word_service.controller;

import com.zzm.word_service.DTO.PresignedUrlRequestDto;
import com.zzm.word_service.DTO.PresignedUrlResponseDto;
import com.zzm.word_service.service.LambdaService;
import com.zzm.word_service.service.S3Service;
import com.zzm.word_service.service.TranslateService;
import com.zzm.word_service.service.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.rekognition.model.Label;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WordController {

    private final S3Service s3Service;
    private final LambdaService lambdaService;
    private final TranslateService translateService;
    private final WordService wordService;

    @PostMapping("/test")
    public ResponseEntity<?> testLocalStack(@RequestParam("file") MultipartFile multipartFile) {
        try {
            // 1. S3 버킷 생성 (이미 존재하면 무시)
            s3Service.createBucket();

            // 2. 이미지 업로드
            String fileKey = s3Service.uploadImageFile(multipartFile);

            // 3. Lambda 호출 및 Rekognition 결과 받기
            List<Label> objects = lambdaService.invokeLambda(fileKey);

            // 4. 레이블 출력
            for (Label label : objects) {
                System.out.println(label.name() + " : " + label.confidence());
            }

            String korWord = translateService.translateWord(objects);

            System.out.println("번역된 단어 : " + korWord);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("실패: " + e.getMessage());
        }

        return ResponseEntity.ok("성공!");
    }

    @GetMapping("/word-service/presigned-url")
    public ResponseEntity<?> getPreSignedUrl(@RequestBody PresignedUrlRequestDto request) {

        PresignedUrlResponseDto dto = wordService.getUploadPresignedUrl(request.getExtension());
        dto.getScenarioId();

        // TODO: Dynamo DB(tempProcessing)에 해당 scenarioId로 데이터 추가(status = Pending)

        return ResponseEntity.ok().body(dto);
    }

}

