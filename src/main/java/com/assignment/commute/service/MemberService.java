package com.assignment.commute.service;

import com.assignment.commute.data.dto.MemberCalendarDto;
import com.assignment.commute.data.dto.MemberDto;
import com.assignment.commute.data.dto.MemberResponseDto;

import java.util.List;

public interface MemberService {
    boolean login(String id, String pwd);

    MemberResponseDto saveMember(MemberDto memberDto);

    String test();

    String searchName(Long id);

    String searchName(String memberId);

    Long searchId(String memberId);

    boolean searchMember(Long id);

    List<MemberCalendarDto> findTest();

    List<MemberCalendarDto> findMemberCalendarList();

    void deletefindMemberCalendarList();
}
