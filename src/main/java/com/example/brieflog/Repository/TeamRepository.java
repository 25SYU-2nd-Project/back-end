package com.example.brieflog.Repository;

import com.example.brieflog.Entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByTeamName(String teamName);

    // ★ 부분 검색용 (대소문자 구분없이 일부만 포함되어도 검색)
    List<Team> findByTeamNameContaining(String keyword);
}