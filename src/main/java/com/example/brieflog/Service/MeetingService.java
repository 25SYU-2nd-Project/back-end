package com.example.brieflog.Service;

import com.example.brieflog.Dto.MeetingCreateRequest;
import com.example.brieflog.Entity.*;
import com.example.brieflog.Repository.MeetingAttendeeRepository;
import com.example.brieflog.Repository.MeetingRepository;
import com.example.brieflog.Repository.TeamRepository;
import com.example.brieflog.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeetingService {
    private final TeamRepository teamRepository;
    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final MeetingAttendeeRepository attendeeRepository;

    @Transactional
    public Long createMeeting(MeetingCreateRequest req) {
        Team team = teamRepository.findById(req.getTeamId())
                .orElseThrow(() -> new RuntimeException("팀 없음"));

        Meeting meeting = new Meeting();
        meeting.setTeam(team);
        meeting.setMeetingDate(LocalDate.parse(req.getDate()));
        meeting.setMeetingTime(LocalTime.parse(req.getTime()));
        meeting.setContent(req.getContent());
        meeting.setSessionNumber(req.getSessionNumber());
        meetingRepository.save(meeting);

        for (Long userId : req.getAttendeeUserIds()) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("유저 없음"));
            MeetingAttendee attendee = new MeetingAttendee();
            attendee.setMeeting(meeting);
            attendee.setUser(user);
            attendee.setStatus(AttendanceStatus.PRESENT); // 기본값 출석, 변경 가능
            attendeeRepository.save(attendee);
        }
        return meeting.getId();
    }


    public List<Meeting> getMeetingsByTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("팀 없음"));
        return meetingRepository.findAll().stream()
                .filter(m -> m.getTeam().equals(team))
                .collect(Collectors.toList());
    }

    public List<MeetingAttendee> getAttendees(Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("회의 없음"));
        return attendeeRepository.findByMeeting(meeting);
    }

    public Long getLeaderIdByMeetingId(Long meetingId) {
        return teamRepository.findLeaderIdByMeetingId(meetingId);
    }

    // 출석 상태 변경 (출석/결석 등)
    @Transactional
    public void updateAttendance(Long meetingId, Long userId, AttendanceStatus status) {
        MeetingAttendee attendee = attendeeRepository.findByMeetingIdAndUserId(meetingId, userId)
                .orElseThrow(() -> new RuntimeException("해당 참석자가 없습니다."));
        attendee.setStatus(status);
        attendeeRepository.save(attendee);
    }
}
