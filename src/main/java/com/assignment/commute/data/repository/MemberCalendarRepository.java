package com.assignment.commute.data.repository;

import com.assignment.commute.data.entity.Member;
import com.assignment.commute.data.entity.MemberCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberCalendarRepository extends JpaRepository<MemberCalendar, Integer> {


    @Query("SELECT mc.start, COUNT(mc.start) FROM MemberCalendar AS mc GROUP BY mc.start ORDER BY mc.start DESC")
    List<Object[]> countGruopByStart();

    List<MemberCalendar> findAllByStart(String today);
}
