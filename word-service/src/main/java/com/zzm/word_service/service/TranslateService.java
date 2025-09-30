package com.zzm.word_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.rekognition.model.Label;
import software.amazon.awssdk.services.translate.TranslateClient;
import software.amazon.awssdk.services.translate.model.TranslateTextRequest;
import software.amazon.awssdk.services.translate.model.TranslateTextResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TranslateService {

    private final TranslateClient translateClient;

    // Rekognition으로 인식한 객체 번역
    public String translateWord(List<Label> engWord) {

//        Label label = engWord.getFirst();
//
//        TranslateTextRequest request = TranslateTextRequest
//                .builder()
//                .text(label.name())
//                .sourceLanguageCode("en") // 원본 언어
//                .targetLanguageCode("ko") // 타겟 언어
//                .build();
//
//
//        TranslateTextResponse response = translateClient.translateText(request);
//
//        String korWord = response.translatedText();

        // 임시 객체 번역
        String korWord = "강아지";
        return korWord;
    }

}
