package com.zzm.word_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.*;
import software.amazon.awssdk.services.rekognition.model.Label;
import software.amazon.awssdk.services.rekognition.model.S3Object;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LambdaService {
    private final LambdaClient lambdaClient;
    private final S3Service s3Service;
    private final RekognitionService rekognitionService;

    // 람다 생성
    public void createLambda() throws Exception {
        try {
            CreateFunctionRequest request = CreateFunctionRequest.builder()
                    .functionName("myLambda")
                    .runtime("python3.9") // 문자열로 지정
                    .role("arn:aws:iam::000000000000:role/lambda-role")
                    .handler("lambda_function.lambda_handler")
                    .code(FunctionCode.builder()
                            .zipFile(SdkBytes.fromByteArray(
                                    Files.readAllBytes(Paths.get("function.zip"))
                            ))
                            .build())
                    .build();

            lambdaClient.createFunction(request);
            System.out.println("Lambda 생성 완료");

        } catch (ResourceConflictException e) {
            System.out.println("Lambda 이미 존재, 무시");
        }
    }

    // 람다 호출 (S3이미지 -> Rekognition)
    public List<Label> invokeLambda(String filKey) {
        System.out.println("Lambda 호출");
        S3Object s3Object = s3Service.triggerS3(filKey);
        List<Label> objects = rekognitionService.objectRecognition(s3Object);


        return objects;
    }
}