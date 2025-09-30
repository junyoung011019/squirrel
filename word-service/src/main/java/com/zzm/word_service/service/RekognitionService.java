package com.zzm.word_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RekognitionService {
    private final RekognitionClient rekognitionClient;

    // Rekognition 응답 임시 데이터 사용
    public List<Label> objectRecognition(S3Object s3Object) {
        System.out.println("Rekognition 호출");

        Label label1 = Label.builder()
                .name("dog")
                .confidence(99.0f)
                .build();

//        Label label2 = Label.builder()
//                .name("ExampleLabel")
//                .confidence(88.5f)
//                .build();

        return List.of(label1);
//        return List.of(label1, label2);



//        Image image = Image.builder()
//                .s3Object(s3Object)
//                .build();
//
//
//        DetectLabelsRequest detectLabelsRequest =
//                DetectLabelsRequest.builder()
//                .image(image)
//                .maxLabels(10) //최대 레이블 수
//                .minConfidence(70F) // 최소 정확도?
//                .build();
//
//        DetectLabelsResponse response  = rekognitionClient.detectLabels(detectLabelsRequest);
//        List<Label> recognizedObjects =  response .labels();

//        return recognizedObjects;
    }
}
