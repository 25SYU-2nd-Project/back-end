package com.example.brieflog.Dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class UserJoinRequest {
    private String userId;
    private String userPw;
    private String userName;
}
