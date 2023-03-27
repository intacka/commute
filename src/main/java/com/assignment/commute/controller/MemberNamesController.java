package com.assignment.commute.controller;

import com.assignment.commute.data.dto.DinnerVoteDto;
import com.assignment.commute.data.repository.MemberCalendarRepository;
import com.assignment.commute.service.DinnerVoteService;
import com.assignment.commute.service.MemberService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class MemberNamesController {

    private final DinnerVoteService dinnerVoteService;


    @Autowired
    public MemberNamesController(DinnerVoteService dinnerVoteService) {
        this.dinnerVoteService = dinnerVoteService;
    }


    @PostMapping(value = "/get_membernames")
    @ResponseBody
    public String vote(DinnerVoteDto dinnerVoteDto) {
        System.out.println("dinnerVoteId : " + dinnerVoteDto.getDinnerVoteId());

        Integer dinnerVoteId = dinnerVoteDto.getDinnerVoteId();
        String memberNames = dinnerVoteService.getMemberNames(dinnerVoteId);


        return memberNames;

    }








}
