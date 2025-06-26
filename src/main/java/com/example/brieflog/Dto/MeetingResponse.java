package com.example.brieflog.Dto;

import com.example.brieflog.Entity.Meeting;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeetingResponse {
    private Long id;
    private int sessionNumber; // 차시
    private String meetingDate;
    private String meetingTime;
    private String content;

    public static MeetingResponse fromEntity(Meeting m) {
        MeetingResponse dto = new MeetingResponse();
        dto.id = m.getId();
        dto.sessionNumber = m.getSessionNumber();
        dto.meetingDate = m.getMeetingDate().toString();
        dto.meetingTime = m.getMeetingTime().toString();
        dto.content = m.getContent();
        return dto;
    }
}

