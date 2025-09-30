package com.zzm.word_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.polly.PollyClient;

@Service
@RequiredArgsConstructor
public class PollyService {
    private final PollyClient pollyClient;

    public void createTTS() {

    }
}
