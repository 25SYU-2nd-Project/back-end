package com.example.brieflog.Dto;

import com.example.brieflog.Entity.AttendanceStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceStatusUpdateRequest {
    private AttendanceStatus status;
}
