package com.example.brieflog.Dto;

import com.example.brieflog.Entity.MeetingAttendee;
import com.example.brieflog.Entity.AttendanceStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceResponse {
    private Long id;
    private Long userId;
    private String userName;
    private AttendanceStatus status;
    private boolean leader;

    public static AttendanceResponse fromEntity(MeetingAttendee a, Long leaderId) {
        AttendanceResponse dto = new AttendanceResponse();
        dto.setId(a.getId());
        dto.setUserId(a.getUser().getId());
        dto.setUserName(a.getUser().getUserName());
        dto.setStatus(a.getStatus());
        // 리더 여부 판별
        dto.setLeader(a.getUser().getId().equals(leaderId));
        return dto;
    }
}
