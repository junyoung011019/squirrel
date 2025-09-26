CREATE TABLE users (
    -- [고유 숫자] 우리 시스템 내부에서 사용할 기본 키 (Primary Key)
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    
    -- [로그인 아이디] 사용자가 로그인 시 실제로 입력하는 아이디
    username VARCHAR(50) UNIQUE NOT NULL,
    
    -- [Cognito ID] Cognito가 발급한 고유 사용자 식별자 (sub 값)
    cognito_sub VARCHAR(255) UNIQUE NOT NULL,
    
    -- 우리 서비스에서 사용할 사용자의 별명
    nickname VARCHAR(50) UNIQUE NOT NULL,
    
    -- 성별. 'MALE' 또는 'FEMALE' 값만 저장 가능
    gender ENUM('MALE', 'FEMALE'),
    
    -- 사용자 이메일 주소
    email VARCHAR(255) UNIQUE,
    
    -- 사용자 전화번호
    phone VARCHAR(20) UNIQUE NOT NULL,
    
    -- 약관에 동의한 시각
    terms_agreed_at DATETIME NOT NULL,
    
    -- 레코드가 생성된 시각 (자동 생성)
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- 레코드가 마지막으로 수정된 시각 (자동 업데이트)
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);