package com.example.brieflog.Repository;

import com.example.brieflog.Entity.Meeting;
import com.example.brieflog.Entity.MeetingAttendee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    List<Meeting> findByTeamIdOrderBySessionNumberAsc(Long teamId);
}

