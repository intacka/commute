package com.assignment.commute.controller;

import com.assignment.commute.data.dto.DinnerVoteDto;
import com.assignment.commute.data.repository.MemberCalendarRepository;
import com.assignment.commute.service.DinnerVoteService;
import com.assignment.commute.service.MemberService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class DinnerVoteController {

    private final MemberService memberService;
    private final MemberCalendarRepository memberCalendarRepository;
    private final JPAQueryFactory queryFactory;
    private final DinnerVoteService dinnerVoteService;


    @Autowired
    public DinnerVoteController(MemberService memberService,
                                MemberCalendarRepository memberCalendarRepository,
                                JPAQueryFactory queryFactory,
                                DinnerVoteService dinnerVoteService) {
        this.memberService = memberService;
        this.memberCalendarRepository = memberCalendarRepository;
        this.queryFactory = queryFactory;
        this.dinnerVoteService = dinnerVoteService;
    }


    @RequestMapping(value = "/dinnervote")
    public String dinnerVote(Model model, HttpSession session) {
        // 오늘 날짜로 된 밥투표DB를 찾아서 보여준다.
        Long id = 0l; // 회원고유 id
        // 세션에 값이 존재하면
        if (session.getAttribute("id")!=null) {
            id = (Long) session.getAttribute("id");

            boolean voteResult = false; // 투표이력

            // 오늘날짜로 추가된 DinnerVote 찾아서 반환해주자.
            List<DinnerVoteDto> dinnerVoteDtoList = dinnerVoteService.getDinnerVoteList();
            // getId() 가 null일 경우 -> 바로 넘어가도록
            if (!dinnerVoteDtoList.isEmpty()) {
                for (int i = 0; i < dinnerVoteDtoList.size(); i++) {
                    DinnerVoteDto searchedDinnerVoteDto = dinnerVoteDtoList.get(i);

                    String members = searchedDinnerVoteDto.getMembers();
                    if (!members.equals("")) { // 투표한 member 들이 존재한다면??
                        String[] arrayMembers = members.split(",");
                        for (int j = 0; j < arrayMembers.length; j++) { // 밥메뉴중에서 내가 투표한 이력이 있다면? -> voteResult = true;
                            if (id== Long.parseLong(arrayMembers[j])) {
                                voteResult = true;
                            }
                        }
                    }

                }

            }

            // 오늘날짜로 추가된 DinnerVote 찾아서 반환해주자.
            model.addAttribute("dinnerVoteDtoList", dinnerVoteDtoList);
            model.addAttribute("voteResult", voteResult);

            return "dinnervote";
        } else { // 세션에 값이 없다면 -> 로그인페이지로 이동.
            return "redirect:/login"; // 로그인 실패로 redirect
        }

    }

    @PostMapping(value = "/dinnervote/input")
    public String dinnerVoteInput(Model model, HttpSession session, DinnerVoteDto dinnerVoteDto) {


        // 오늘 날짜로 된 밥투표DB를 찾아서 보여준다.
        Long id = 0l; // 회원고유 id
        // 세션에 값이 존재하면
        if (session.getAttribute("id")!=null) {
            id = (Long) session.getAttribute("id");


            String menuTime = dinnerVoteDto.getMenuTime();
            dinnerVoteService.saveDinnerVote(menuTime, id);


            boolean voteResult = false; // 투표이력

            // 오늘날짜로 추가된 DinnerVote 찾아서 반환해주자.
            List<DinnerVoteDto> dinnerVoteDtoList = dinnerVoteService.getDinnerVoteList();
            // getId() 가 null일 경우 -> 바로 넘어가도록
            if (!dinnerVoteDtoList.isEmpty()) {
                for (int i = 0; i < dinnerVoteDtoList.size(); i++) {
                    DinnerVoteDto searchedDinnerVoteDto = dinnerVoteDtoList.get(i);

                    String members = searchedDinnerVoteDto.getMembers();
                    if (!members.equals("")) {
                        String[] arrayMembers = members.split(",");
                        for (int j = 0; j < arrayMembers.length; j++) { // 밥메뉴중에서 내가 투표한 이력이 있다면? -> voteResult = true;
                            if (id== Long.parseLong(arrayMembers[j])) {
                                voteResult = true;
                            }
                        }
                    }
                }

            }

            model.addAttribute("dinnerVoteDtoList", dinnerVoteDtoList);
            model.addAttribute("voteResult",voteResult);

            return "redirect:/dinnervote";
        } else { // 세션에 값이 없다면 -> 로그인페이지로 이동.
            return "redirect:/login"; // 로그인 실패로 redirect
        }
    }

    @PostMapping(value = "/dinnervote/vote")
    public String vote(Model model, HttpSession session, DinnerVoteDto dinnerVoteDto) {


        // 오늘 날짜로 된 밥투표DB를 찾아서 보여준다.
        Long id = 0l; // 회원고유 id
        // 세션에 값이 존재하면
        if (session.getAttribute("id")!=null) {
            id = (Long) session.getAttribute("id");

            int dinnerVoteId = dinnerVoteDto.getDinnerVoteId();
            dinnerVoteService.addMember(id, dinnerVoteId);

            boolean voteResult = false; // 투표이력


            // 오늘날짜로 추가된 DinnerVote 찾아서 반환해주자.
            List<DinnerVoteDto> dinnerVoteDtoList = dinnerVoteService.getDinnerVoteList();
            // getId() 가 null일 경우 -> 바로 넘어가도록
            if (!dinnerVoteDtoList.isEmpty()) {
                for (int i = 0; i < dinnerVoteDtoList.size(); i++) {
                    DinnerVoteDto searchedDinnerVoteDto = dinnerVoteDtoList.get(i);

                    String members = searchedDinnerVoteDto.getMembers();
                    if (!members.equals("")) {
                        String[] arrayMembers = members.split(",");
                        for (int j = 0; j < arrayMembers.length; j++) { // 밥메뉴중에서 내가 투표한 이력이 있다면? -> voteResult = true;
                            if (id== Long.parseLong(arrayMembers[j])) {
                                voteResult = true;
                            }
                        }
                    }
                }

            }


            model.addAttribute("dinnerVoteDtoList", dinnerVoteDtoList);
            model.addAttribute("voteResult",voteResult);

            return "redirect:/dinnervote";
        } else { // 세션에 값이 없다면 -> 로그인페이지로 이동.
            return "redirect:/login"; // 로그인 실패로 redirect
        }
    }









}
