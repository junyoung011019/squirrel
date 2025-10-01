package com.zzm.word_service.service;

import com.zzm.word_service.entity.TempProcessing;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Service
public class DynamoService {
    private final DynamoDbEnhancedClient enhancedClient;
    private final DynamoDbTable<TempProcessing> table;

    // 생성자에서 테이블 객체 생성
    public DynamoService(DynamoDbEnhancedClient enhancedClient) {
        this.enhancedClient = enhancedClient;
        this.table = enhancedClient.table("temp_processing", TableSchema.fromBean(TempProcessing.class));
    }

    public void createTempData(String scenarioId, String fileKey) {
        
        long ttl = System.currentTimeMillis() / 1000 + 3600; // 1시간 후 삭제
        TempProcessing temp = TempProcessing.builder()
                .scenarioId(scenarioId)
                .userId(1L)
                .ttl(ttl)
                .imageS3Key(fileKey)
                .build();
        table.putItem(temp);
    }

    public TempProcessing getTempData(String scenarioId) {
        return table.getItem(r -> r.key(k -> k.partitionValue(scenarioId)));
    }
}
