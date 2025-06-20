package com.example.brieflog.Dto;

import com.example.brieflog.Entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String userId;
    private String userName;

    // 아래 생성자 추가
    public UserResponse(User user) {
        this.id = user.getId();
        this.userId = user.getUserId();
        this.userName = user.getUserName();
    }
}
