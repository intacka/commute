package com.assignment.commute.data.repository;

import com.assignment.commute.data.entity.Commute;
import com.assignment.commute.data.entity.Rest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RestRepository extends JpaRepository<Rest, Long> {
    List<Rest> findByMemberId(Long memberInherentId);

    List<Rest> findByRestDate(LocalDate now);

    List<Rest> findByRestDateAndMemberId(LocalDate now, Long memberId);



    List<Rest> findAllByMemberIdAndRestDateOrderByCreatedAt(Long id, LocalDate localDate);
}
