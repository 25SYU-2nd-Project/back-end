package com.example.brieflog.Controller;

import com.example.brieflog.Dto.AttendanceResponse;
import com.example.brieflog.Dto.AttendanceStatusUpdateRequest;
import com.example.brieflog.Dto.MeetingCreateRequest;
import com.example.brieflog.Dto.MeetingResponse;
import com.example.brieflog.Entity.AttendanceStatus;
import com.example.brieflog.Entity.Meeting;
import com.example.brieflog.Entity.MeetingAttendee;
import com.example.brieflog.Service.MeetingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/meetings")
public class MeetingController {

    private final MeetingService meetingService;

    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @PostMapping("/{teamId}/create")
    public ResponseEntity<String> createMeeting(
            @PathVariable Long teamId,
            @RequestBody MeetingCreateRequest request
    ) {
        request.setTeamId(teamId);
        meetingService.createMeeting(request);
        return ResponseEntity.ok("회의 생성 성공!");
    }

    @GetMapping("/list/{teamId}")
    public ResponseEntity<List<MeetingResponse>> getMeetings(@PathVariable Long teamId) {
        List<Meeting> meetings = meetingService.getMeetingsByTeam(teamId);
        List<MeetingResponse> result = meetings.stream()
                .map(MeetingResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{meetingId}/attendees")
    public ResponseEntity<List<AttendanceResponse>> getAttendees(@PathVariable Long meetingId) {
        Long leaderId = meetingService.getLeaderIdByMeetingId(meetingId);

        List<AttendanceResponse> result = meetingService.getAttendees(meetingId)
                .stream()
                .map(a -> AttendanceResponse.fromEntity(a, leaderId))
                .toList();

        return ResponseEntity.ok(result);
    }

    @PutMapping("/{meetingId}/attendees/{userId}/status")
    public ResponseEntity<Void> updateAttendance(
            @PathVariable Long meetingId,
            @PathVariable Long userId,
            @RequestBody AttendanceStatusUpdateRequest request
    ) {
        meetingService.updateAttendance(meetingId, userId, request.getStatus());
        return ResponseEntity.ok().build();
    }
}