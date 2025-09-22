package com.lambdasquirrel.user_service.Service;

import com.lambdasquirrel.user_service.DTO.ConfirmEmailSignUpDTO;
import com.lambdasquirrel.user_service.DTO.SignUpRequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmSignUpRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpRequest;

@Service
public class AuthService {

    private final CognitoIdentityProviderClient cognitoClient;
    private final String clientId;
    private final String userPoolId;

    public AuthService(CognitoIdentityProviderClient cognitoClient,
                       @Value("${aws.cognito.client-id}") String clientId,
                       @Value("${aws.cognito.user-pool-id}") String userPoolId) {
        this.cognitoClient = cognitoClient;
        this.clientId = clientId;
        this.userPoolId = userPoolId;
    }

    public void signUp(SignUpRequestDTO requestDTO){
        try {
            // Cognito에 전달할 사용자 속성을 만듭니다.
            AttributeType emailAttr = AttributeType.builder().name("email").value(requestDTO.getEmail()).build();
            AttributeType nicknameAttr = AttributeType.builder().name("nickname").value(requestDTO.getNickname()).build();
            AttributeType genderAttr = AttributeType.builder().name("gender").value(requestDTO.getGENDER().name()).build();

            // Cognito에 회원가입을 요청합니다.
            SignUpRequest signUpRequest = SignUpRequest.builder()
                    .clientId(this.clientId)
                    .username(requestDTO.getEmail())
                    .password(requestDTO.getPassword())
                    .userAttributes(emailAttr, nicknameAttr, genderAttr)
                    .build();

            cognitoClient.signUp(signUpRequest);

        } catch (CognitoIdentityProviderException e) {
            // 에러 처리 로직 (예: 이미 존재하는 이메일)
            throw new RuntimeException(e.awsErrorDetails().errorMessage());
        }
    }

    public void confirmEmail(ConfirmEmailSignUpDTO requestDTO){
        try {
            ConfirmSignUpRequest cognitoRequest = ConfirmSignUpRequest.builder()
                    .clientId(this.clientId)
                    .username(requestDTO.getEmail())
                    .confirmationCode(requestDTO.getConfirmationCode())
                    .build();
            cognitoClient.confirmSignUp(cognitoRequest);

        } catch (CognitoIdentityProviderException e) {

            throw new RuntimeException(e.awsErrorDetails().errorMessage());
        }
    }

}
