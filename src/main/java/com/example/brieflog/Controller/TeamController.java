package com.example.brieflog.Controller;

import com.example.brieflog.Dto.TeamCreateRequest;
import com.example.brieflog.Dto.TeamResponse;
import com.example.brieflog.Dto.UserResponse;
import com.example.brieflog.Dto.UserTeamPendingResponse;
import com.example.brieflog.Entity.Team;
import com.example.brieflog.Entity.UserTeam;
import com.example.brieflog.Service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createTeam(@RequestBody TeamCreateRequest request, Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        teamService.createTeam(userId, request);
        return ResponseEntity.ok("팀 생성 성공!");
    }

    @PostMapping("/{teamId}/request")
    public ResponseEntity<String> requestJoinTeam(
            @PathVariable Long teamId,
            Authentication authentication
    ) {
        String userId = (String) authentication.getPrincipal();
        teamService.requestJoinTeam(teamId, userId);
        return ResponseEntity.ok("가입 신청 완료! 대기 중입니다.");
    }

    @PostMapping("/{teamId}/accept/{userTeamId}")
    public ResponseEntity<String> acceptMember(
            @PathVariable Long teamId,
            @PathVariable Long userTeamId,
            Authentication authentication
    ) {
        // 팀장 권한 확인
        String leaderUserId = (String) authentication.getPrincipal();
        teamService.acceptUser(teamId, userTeamId, leaderUserId);
        return ResponseEntity.ok("팀원 수락 완료!");
    }

    @PostMapping("/{teamId}/reject/{userTeamId}")
    public ResponseEntity<String> rejectMember(
            @PathVariable Long teamId,
            @PathVariable Long userTeamId,
            Authentication authentication
    ) {
        // 팀장 권한 확인
        String leaderUserId = (String) authentication.getPrincipal();
        teamService.rejectUser(teamId, userTeamId, leaderUserId);
        return ResponseEntity.ok("팀원 거절 완료!");
    }

    @GetMapping("/my-teams")
    public ResponseEntity<List<TeamResponse>> getMyTeams(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        List<TeamResponse> teams = teamService.getUserTeams(userId);
        return ResponseEntity.ok(teams);
    }

    @GetMapping("/{teamId}/pending-members")
    public ResponseEntity<List<UserTeamPendingResponse>> getPendingMembers(
            @PathVariable Long teamId,
            Authentication authentication
    ) {
        String leaderUserId = (String) authentication.getPrincipal();
        List<UserTeam> pendings = teamService.getPendingMembers(teamId, leaderUserId);

        // DTO 변환 (순환참조 방지)
        List<UserTeamPendingResponse> resp = pendings.stream()
                .map(ut -> new UserTeamPendingResponse(
                        ut.getId(),
                        ut.getUser().getId(),
                        ut.getUser().getUserId(),
                        ut.getUser().getUserName()
                ))
                .toList();
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TeamResponse>> searchTeamsByName(@RequestParam String name) {
        List<TeamResponse> result = teamService.searchTeamsByName(name);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{teamId}/members")
    public ResponseEntity<List<UserResponse>> getTeamMembers(@PathVariable Long teamId) {
        List<UserResponse> members = teamService.getApprovedMembers(teamId);
        return ResponseEntity.ok(members);
    }
}