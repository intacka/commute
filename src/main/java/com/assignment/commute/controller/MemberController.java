package com.assignment.commute.controller;

import com.assignment.commute.data.container.StatisticsResult;
import com.assignment.commute.data.dto.CommuteResponseDto;
import com.assignment.commute.data.dto.MemberDto;
import com.assignment.commute.data.dto.MemberResponseDto;
import com.assignment.commute.data.dto.RestResponseDto;
import com.assignment.commute.data.entity.Commute;
import com.assignment.commute.service.CommuteService;
import com.assignment.commute.service.MemberService;
import com.assignment.commute.service.RestService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/member")
public class MemberController {


    private final MemberService memberService;
    private final CommuteService commuteService;
    private final RestService restService;

    @Autowired
    public MemberController(MemberService memberService,
                            CommuteService commuteService,
                            RestService restService) {
        this.memberService = memberService;
        this.commuteService = commuteService;
        this.restService = restService;
    }

    @ApiOperation(value = "로그인 API")
    @PostMapping("/login")
    public boolean login(@RequestParam String id, @RequestParam(name = "password") String pwd) { // 암호화 해야함
        boolean result = false;
        result = memberService.login(id, pwd);
        return result;
    }

    @ApiOperation(value = "회원가입 API")
    @PostMapping("/save-member")
    public MemberResponseDto saveMember(@RequestBody MemberDto memberDto) {
        MemberResponseDto memberResponseDto = memberService.saveMember(memberDto);
        return memberResponseDto;
    }

//    @ApiOperation(value = "출근 API")
//    @PostMapping("/go-to-work")
//    public CommuteResponseDto saveGoToWorkTime(@RequestParam Long MemberInherentId,
//                                               @RequestParam(required = false) String tardyReason) {
//        CommuteResponseDto commuteResponseDto = commuteService.saveGoToWorkTime(MemberInherentId, tardyReason);
//        return commuteResponseDto;
//    }
//
//    @ApiOperation(value = "퇴근 API") // 최종휴식시간 추가해야함
//    @PostMapping("/leave")
//    public CommuteResponseDto saveLeaveTime(@RequestParam Long MemberInherentId,
//                                            @RequestParam(required = false) String leaveNote) {
//        CommuteResponseDto commuteResponseDto = commuteService.saveLeaveTime(MemberInherentId, leaveNote);
//        return commuteResponseDto;
//    }
//
//    @ApiOperation(value = "휴식시작") // 시작은 현재시간.
//    @PostMapping("/rest-start")
//    public RestResponseDto startRestTime(@RequestParam Long memberInherentId,
//                                         @RequestParam String restReason) {
//        RestResponseDto restResponseDto = restService.startRestTime(memberInherentId, restReason);
//
//        return restResponseDto;
//    }
//
//
//    @ApiOperation(value = "휴식종료")
//    @PostMapping("/rest-stop")
//    public RestResponseDto stopRestTime(@RequestParam Long memberInherentId) {
//        RestResponseDto restResponseDto = restService.stopRestTime(memberInherentId);
//        return restResponseDto;
//    }



//    @ApiOperation(value = "통계현황조회")                 // return : List<CommuteResponseDto>, goToWorkNumber
//    @PostMapping("/statistics")
//    public StatisticsResult viewStatistics(@RequestParam(required = false) String team,
//                                           @RequestParam String date) {
//        StatisticsResult statisticsResult = commuteService.viewStatistics(team, date);
//        return statisticsResult;
//    }

    @RequestMapping(value = "/idCheck")
    public int idCheck(@RequestParam("id") String id) {

        int cnt = memberService.idCheck(id);
        return cnt;

    }

}
























