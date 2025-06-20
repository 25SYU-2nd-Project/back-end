package com.example.brieflog.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequest {
    private String userId;
    private String userPw;
}