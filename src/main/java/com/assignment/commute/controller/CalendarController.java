package com.assignment.commute.controller;

import com.assignment.commute.data.LunarCalendar;
import com.assignment.commute.data.dto.MemberCalendarDto;
import com.assignment.commute.data.repository.MemberCalendarRepository;
import com.assignment.commute.service.MemberService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
public class CalendarController {

    private final MemberService memberService;
    private final MemberCalendarRepository memberCalendarRepository;
    private final JPAQueryFactory queryFactory;


    @Autowired
    public CalendarController(MemberService memberService, MemberCalendarRepository memberCalendarRepository, JPAQueryFactory queryFactory) {
        this.memberService = memberService;
        this.memberCalendarRepository = memberCalendarRepository;
        this.queryFactory = queryFactory;
    }

    @PostMapping(value = "/calendar")
    public List<MemberCalendarDto> monthPlan() {
        List<MemberCalendarDto> listAll = memberService.findTest();

        return listAll;
    }

    @PostMapping(value = "/test2") // 공휴일 반환 클래스 테스트 API
    public Set<String> test2() {


        LunarCalendar lunarCalendar = new LunarCalendar();
        Set<String> stringSet = lunarCalendar.holidayArray("2023");


        return stringSet;
    }

    @PostMapping(value = "/calendar-reload")
    public List<MemberCalendarDto> reload() {
        List<MemberCalendarDto> listAll = memberService.findMemberCalendarList();

        return listAll;
    }


    @PostMapping(value = "/delete-calendar")
    public void delete() {
        memberService.deletefindMemberCalendarList();
    }

}
