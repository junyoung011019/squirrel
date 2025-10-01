package com.zzm.word_service.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

/**
 * DynamoDB TempProcessing 테이블용 엔티티
 * - 파티션 키: scenarioId
 * - TTL: ttl 필드(epoch 초 단위)
 */
@Data
@NoArgsConstructor       // DynamoDbEnhancedClient에서 반드시 필요
@AllArgsConstructor
@Builder
@DynamoDbBean
public class TempProcessing {

    private String scenarioId;       // 파티션 키
    private Long userId;
    private String imageS3Key;
    private String recognizedWord;
    private String translatedWord;
    @Builder.Default
    private String status = "PENDING";

    private Long ttl;                // epoch 초 단위로 TTL 설정

    @DynamoDbPartitionKey
    public String getScenarioId() {
        return scenarioId;
    }

}
