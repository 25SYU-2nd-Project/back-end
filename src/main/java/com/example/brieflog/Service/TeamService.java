package com.example.brieflog.Service;

import com.example.brieflog.Dto.TeamCreateRequest;
import com.example.brieflog.Dto.TeamResponse;
import com.example.brieflog.Dto.UserResponse;
import com.example.brieflog.Entity.Team;
import com.example.brieflog.Entity.User;
import com.example.brieflog.Entity.UserTeam;
import com.example.brieflog.Entity.UserTeamStatus;
import com.example.brieflog.Repository.TeamRepository;
import com.example.brieflog.Repository.UserRepository;
import com.example.brieflog.Repository.UserTeamRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final UserTeamRepository userTeamRepository;

    public TeamService(TeamRepository teamRepository, UserRepository userRepository, UserTeamRepository userTeamRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.userTeamRepository = userTeamRepository;
    }

    @Transactional
    public void createTeam(String userId, TeamCreateRequest request) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        // 팀 중복 검사
        if (teamRepository.findByTeamName(request.getTeamName()).isPresent()) {
            throw new RuntimeException("이미 존재하는 팀 이름입니다.");
        }

        // 팀 생성
        Team team = new Team();
        team.setTeamName(request.getTeamName());
        team.setLeader(user);
        teamRepository.save(team);

        UserTeam userTeam = new UserTeam();
        userTeam.setUser(user);
        userTeam.setTeam(team);
        userTeam.setStatus(UserTeamStatus.APPROVED);
        userTeamRepository.save(userTeam);
    }

//    @Transactional
//    public void joinTeam(Long teamId, String userId) {
//        User user = userRepository.findByUserId(userId)
//                .orElseThrow(() -> new RuntimeException("사용자 없음"));
//        Team team = teamRepository.findById(teamId)
//                .orElseThrow(() -> new RuntimeException("팀 없음"));
//
//        // 이미 가입되어 있으면 예외처리(옵션)
//        if (user.getTeams().contains(team)) {
//            throw new RuntimeException("이미 가입된 팀입니다.");
//        }
//
//        user.getTeams().add(team);
//        team.getUsers().add(user);
//        // 양방향 관계면 둘 다 추가
//
//        // teamRepository.save(team); // save는 사실 JPA가 트랜잭션 안에서 dirty checking 하므로 명시적 save 생략 가능
//    }

    public List<TeamResponse> getUserTeams(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));
        // APPROVED 상태의 팀만 조회
        List<UserTeam> userTeams = userTeamRepository.findByUserAndStatus(user, UserTeamStatus.APPROVED);
        return userTeams.stream()
                .map(userTeam -> {
                    Team team = userTeam.getTeam();
                    return new TeamResponse(team.getId(), team.getTeamName());
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void requestJoinTeam(Long teamId, String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("팀 없음"));

        // PENDING(대기) or APPROVED(팀원) 둘 중 하나라도 있으면 예외 발생
        boolean alreadyRequestedOrMember = userTeamRepository.existsByUserAndTeamAndStatusIn(
                user, team, List.of(UserTeamStatus.PENDING, UserTeamStatus.APPROVED)
        );
        if (alreadyRequestedOrMember) {
            throw new RuntimeException("이미 가입 신청 중이거나 팀원입니다.");
        }

        UserTeam userTeam = new UserTeam();
        userTeam.setUser(user);
        userTeam.setTeam(team);
        userTeam.setStatus(UserTeamStatus.PENDING);

        userTeamRepository.save(userTeam);
    }

    @Transactional
    public void acceptUser(Long teamId, Long userTeamId, String leaderUserId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("팀 없음"));

        // 팀장 권한 체크
        if (!team.getLeader().getUserId().equals(leaderUserId)) {
            throw new RuntimeException("팀장만 수락할 수 있습니다.");
        }

        UserTeam userTeam = userTeamRepository.findById(userTeamId)
                .orElseThrow(() -> new RuntimeException("가입 신청 내역 없음"));

        if (!userTeam.getTeam().equals(team)) {
            throw new RuntimeException("팀 불일치");
        }

        userTeam.setStatus(UserTeamStatus.APPROVED);
        userTeamRepository.save(userTeam);
    }

    @Transactional
    public void rejectUser(Long teamId, Long userTeamId, String leaderUserId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("팀 없음"));

        // 팀장 권한 체크
        if (!team.getLeader().getUserId().equals(leaderUserId)) {
            throw new RuntimeException("팀장만 거절할 수 있습니다.");
        }

        UserTeam userTeam = userTeamRepository.findById(userTeamId)
                .orElseThrow(() -> new RuntimeException("가입 신청 내역 없음"));

        if (!userTeam.getTeam().equals(team)) {
            throw new RuntimeException("팀 불일치");
        }

        userTeam.setStatus(UserTeamStatus.REJECTED);
        userTeamRepository.save(userTeam);
    }

    public List<UserTeam> getPendingMembers(Long teamId, String leaderUserId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("팀 없음"));
        if (!team.getLeader().getUserId().equals(leaderUserId)) {
            throw new RuntimeException("팀장만 조회할 수 있습니다.");
        }
        return userTeamRepository.findByTeamAndStatus(team, UserTeamStatus.PENDING);
    }

    public List<TeamResponse> searchTeamsByName(String name) {
        List<Team> teams = teamRepository.findByTeamNameContaining(name);
        return teams.stream()
                .map(team -> new TeamResponse(team.getId(), team.getTeamName()))
                .collect(Collectors.toList());
    }

    public List<UserResponse> getApprovedMembers(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("팀 없음"));

        // UserTeam 엔티티에서 status가 APPROVED인 사용자만 추출
        return userTeamRepository.findByTeamAndStatus(team, UserTeamStatus.APPROVED)
                .stream()
                .map(ut -> new UserResponse(ut.getUser()))
                .collect(Collectors.toList());
    }

    public String getTeamLeaderId(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("팀 없음"));
        return team.getLeader().getUserId();
    }
}