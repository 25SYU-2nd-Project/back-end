package com.example.brieflog.Controller;

import com.example.brieflog.Dto.JwtResponse;
import com.example.brieflog.Dto.UserJoinRequest;
import com.example.brieflog.Dto.UserLoginRequest;
import com.example.brieflog.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/join")
    public String join(@RequestBody UserJoinRequest request) {
        userService.registerUser(request);
        return "회원가입 성공!";
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody UserLoginRequest req) {
        JwtResponse token = userService.login(req);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/me")
    public ResponseEntity<String> getMyInfo(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        return ResponseEntity.ok("현재 로그인한 사용자: " + userId);
    }
}