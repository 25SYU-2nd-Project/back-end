package com.example.brieflog.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamResponse {
    private Long id;
    private String teamName;

    public TeamResponse(Long id, String teamName) {
        this.id = id;
        this.teamName = teamName;
    }
}
