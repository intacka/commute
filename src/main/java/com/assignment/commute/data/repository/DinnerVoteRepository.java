package com.assignment.commute.data.repository;

import com.assignment.commute.data.entity.Commute;
import com.assignment.commute.data.entity.DinnerVote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DinnerVoteRepository extends JpaRepository<DinnerVote, Integer> {
    List<DinnerVote> findAllByDinnerDateOrderByCreatedAt(LocalDate today);

    Optional<DinnerVote> findByMembersContaining(String id);
}
