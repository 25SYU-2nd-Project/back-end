package com.example.brieflog.Repository;

import com.example.brieflog.Entity.Meeting;
import com.example.brieflog.Entity.MeetingAttendee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MeetingAttendeeRepository extends JpaRepository<MeetingAttendee, Long> {
    List<MeetingAttendee> findByMeeting(Meeting meeting);

    Optional<MeetingAttendee> findByMeetingIdAndUserId(Long meetingId, Long userId);

    void deleteByMeetingId(Long meetingId);
}
