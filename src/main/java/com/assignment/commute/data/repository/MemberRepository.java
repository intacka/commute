package com.assignment.commute.data.repository;

import com.assignment.commute.data.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberId(String memberId);

    List<Member> findAllByTeam(String team);




    @Query("SELECT m.team, COUNT(m.team) FROM Member AS m GROUP BY m.team ORDER BY m.team DESC")
    List<Object[]> findTeamGroupBy();

}
