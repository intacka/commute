package com.assignment.commute.data.repository;

import com.assignment.commute.data.dto.CommuteResponseDto;
import com.assignment.commute.data.entity.Commute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CommuteRepository extends JpaRepository<Commute, Long> {

    Optional<Commute> findByMemberId(Long memberInherentId);
    Optional<Commute> findByMemberIdAndCommuteDate(Long memberInherentId, LocalDate localDate);
    Optional<Commute> findByMemberIdAndCommuteDateOrderByCreatedAtDesc(Long memberInherentId, LocalDate localDate);

    List<Commute> findAllByCommuteDateOrderByCreatedAtDesc(LocalDate inputLocalDate);

    List<Commute> findAllByMemberIdOrderByCreatedAtDesc(Long memberInherentId);

    Page<Commute> findAllByMemberIdOrderByCreatedAtDesc(Pageable pageable, Long memberInherentId);

}
