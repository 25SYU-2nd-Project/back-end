package com.example.brieflog.Repository;

import com.example.brieflog.Entity.UserTeam;
import com.example.brieflog.Entity.User;
import com.example.brieflog.Entity.Team;
import com.example.brieflog.Entity.UserTeamStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserTeamRepository extends JpaRepository<UserTeam, Long> {
    // 중복 신청 방지용
    boolean existsByUserAndTeamAndStatusIn(User user, Team team, List<UserTeamStatus> statusList);

    // 팀 가입 대기자 목록 (팀장용)
    List<UserTeam> findByTeamAndStatus(Team team, UserTeamStatus status);

    // 특정 사용자의 팀(승인된 것만)
    List<UserTeam> findByUserAndStatus(User user, UserTeamStatus status);

    // 팀별 승인된 유저
    List<UserTeam> findByTeamAndStatusIn(Team team, List<UserTeamStatus> statuses);

    // 기타 필요한 쿼리들은 추가하면서 확장하면 됩니다.
}