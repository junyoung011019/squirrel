package com.lambdasquirrel.user_service.Controller;

import com.lambdasquirrel.user_service.DTO.ConfirmEmailSignUpDTO;
import com.lambdasquirrel.user_service.DTO.SignUpRequestDTO;
import com.lambdasquirrel.user_service.Service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/hello")
    public String test() {
        return "hello";
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequestDTO requestDto) {
        authService.signUp(requestDto);
        return ResponseEntity.ok("회원가입 요청이 접수되었습니다. 이메일에서 인증을 완료해주세요.");
    }

    @PostMapping("/confirm")
    public ResponseEntity<String> confirmEmail(@RequestBody ConfirmEmailSignUpDTO requestDto) {
        authService.confirmEmail(requestDto);
        return ResponseEntity.ok("인증이 완료 되었습니다.");
    }
}
