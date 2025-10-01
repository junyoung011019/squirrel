package com.zzm.word_service.controller;

import com.zzm.word_service.DTO.PresignedUrlRequestDto;
import com.zzm.word_service.DTO.PresignedUrlResponseDto;
import com.zzm.word_service.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.rekognition.model.Label;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WordController {

    private final WordService wordService;
    private final DynamoService dynamoService;


    @GetMapping("/word-service/presigned-url")
    public ResponseEntity<?> getPreSignedUrl(@RequestBody PresignedUrlRequestDto request) {

        PresignedUrlResponseDto dto = wordService.getUploadPresignedUrl(request.getExtension());


        // TODO: Dynamo DB(tempProcessing)에 해당 scenarioId로 데이터 추가(status = PENDING)
        dynamoService.createTempData(dto.getScenarioId(), dto.getFileKey());
        return ResponseEntity.ok().body(dto);
    }

}

