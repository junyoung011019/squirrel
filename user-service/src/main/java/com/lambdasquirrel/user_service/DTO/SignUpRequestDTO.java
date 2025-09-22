package com.lambdasquirrel.user_service.DTO;

import com.lambdasquirrel.user_service.Gender;

public class SignUpRequestDTO {
    private String email;
    private String nickname;
    private String password;
    private Gender GENDER;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Enum getGENDER() {
        return GENDER;
    }

    public void setGENDER(Gender GENDER) {
        this.GENDER = GENDER;
    }
}
