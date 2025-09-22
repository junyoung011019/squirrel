package com.lambdasquirrel.user_service.DTO;

public class ConfirmEmailSignUpDTO {

    private String email;
    private String confirmationCode;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }
}
