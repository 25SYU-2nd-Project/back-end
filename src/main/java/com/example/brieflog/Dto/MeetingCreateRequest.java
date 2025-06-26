package com.example.brieflog.Dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class MeetingCreateRequest {
    private Long teamId;
    private String date; // "2024-06-23"
    private String time; // "14:30"
    private String content;
    private Integer sessionNumber;
    private List<Long> attendeeUserIds;
}
