package com.example.brieflog.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserTeamPendingResponse {
    private Long userTeamId;
    private Long userId;
    private String userLoginId;
    private String userName;

    public UserTeamPendingResponse(Long userTeamId, Long userId, String userLoginId, String userName) {
        this.userTeamId = userTeamId;
        this.userId = userId;
        this.userLoginId = userLoginId;
        this.userName = userName;
    }
}
