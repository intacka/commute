package com.assignment.commute.controller;

import com.assignment.commute.data.dto.*;
import com.assignment.commute.data.entity.Commute;
import com.assignment.commute.service.CommuteService;
import com.assignment.commute.service.MemberService;
import com.assignment.commute.service.RestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

@Controller
public class HomeController {

    private final MemberService memberService;
    private final CommuteService commuteService;
    private final RestService restService;

    @Autowired
    public HomeController(MemberService memberService,
                            CommuteService commuteService,
                            RestService restService) {
        this.memberService = memberService;
        this.commuteService = commuteService;
        this.restService = restService;
    }


    @PostMapping
    public List<CommuteResponseDto> test(final Pageable pageable) {

        List<CommuteResponseDto> commuteResponseDtoList = commuteService.test();

        return commuteResponseDtoList;
    }

    @RequestMapping(value = "/statistics")
    public String statistics(HttpSession session, Model model,
                                ResearchDto researchDto,
//                             @RequestParam(value = "selectBox", required = false) String selectBox,
//                             @RequestParam(value = "calenderData", required = false) String calenderData,
                             @PageableDefault Pageable pageable) {

        String selectBox = researchDto.getSelectBox();
        String calenderData = researchDto.getCalenderData();

        // 세션에 값이 존재하면
        if (session.getAttribute("id")!=null) {

            Long id = (Long) session.getAttribute("id");
            boolean result = memberService.searchMember(id);

            // 세션 id로 회원이 존재가 되면~~ 기능을 수행하자.
            if (result) {


                Page<StatisticsResponseDto> allStatisticsResponseDtoList = commuteService.viewSelectionCommuteList2(selectBox, calenderData, pageable);
                model.addAttribute("list", allStatisticsResponseDtoList);                            //////
                model.addAttribute("allCommuteResponseDtoList", allStatisticsResponseDtoList);
                model.addAttribute("selectBox",selectBox);
                model.addAttribute("calenderData", calenderData);
                model.addAttribute("personCnt", allStatisticsResponseDtoList.getTotalElements());
                System.out.println("selectBox : " + selectBox);
                return "statistics";

            } else { // 세션id가 DB에 없는 id라면
                return "login";
            }

        } else {
            return "login"; // 세션에 값이없다면 login으로 보내기
        }
    }

    /// 수정된부분
    @RequestMapping(value = "/statistics_test")
    public String statistics_test(HttpSession session, Model model,
                                  ResearchDto researchDto,
//                             @RequestParam(value = "selectBox", required = false) String selectBox,
//                             @RequestParam(value = "calenderData", required = false) String calenderData,
                             @PageableDefault Pageable pageable) {
        String selectBox = researchDto.getSelectBox();
        String calenderData = researchDto.getCalenderData();
        // 세션에 값이 존재하면
        if (session.getAttribute("id")!=null) {

            Long id = (Long) session.getAttribute("id");
            boolean result = memberService.searchMember(id);

            // 세션 id로 회원이 존재가 되면~~ 기능을 수행하자.
            if (result) {


                Page<StatisticsResponseDto> allStatisticsResponseDtoList = commuteService.viewSelectionCommuteList2(selectBox, calenderData, pageable); //
                model.addAttribute("list", allStatisticsResponseDtoList);
                model.addAttribute("allCommuteResponseDtoList", allStatisticsResponseDtoList);
                model.addAttribute("selectBox",selectBox);
                model.addAttribute("calenderData", calenderData);
                // 모델에다가 출근인원 몇명인지 쏴주자
                model.addAttribute("personCnt", allStatisticsResponseDtoList.getTotalElements()); //
                return "statistics :: #commute_list";

            } else { // 세션id가 DB에 없는 id라면
                return "login";
            }

        } else {
            return "login"; // 세션에 값이없다면 login으로 보내기
        }
    }

    @RequestMapping(value = "/cnt")
    public String cnd(HttpSession session, Model model,
                      ResearchDto researchDto,
//                             @RequestParam(value = "selectBox", required = false) String selectBox,
//                             @RequestParam(value = "calenderData", required = false) String calenderData,
                                  @PageableDefault Pageable pageable) {
        String selectBox = researchDto.getSelectBox();
        String calenderData = researchDto.getCalenderData();
        // 세션에 값이 존재하면
        if (session.getAttribute("id")!=null) {

            Long id = (Long) session.getAttribute("id");
            boolean result = memberService.searchMember(id);

            // 세션 id로 회원이 존재가 되면~~ 기능을 수행하자.
            if (result) {


                Page<StatisticsResponseDto> allStatisticsResponseDtoList = commuteService.viewSelectionCommuteList2(selectBox, calenderData, pageable); //
//                model.addAttribute("list", allStatisticsResponseDtoList);
//                model.addAttribute("allCommuteResponseDtoList", allStatisticsResponseDtoList);
//                model.addAttribute("selectBox",selectBox);
//                model.addAttribute("calenderData", calenderData);
                // 모델에다가 출근인원 몇명인지 쏴주자
                model.addAttribute("personCnt", allStatisticsResponseDtoList.getTotalElements()); //
                return "statistics :: #commute_cnt";

            } else { // 세션id가 DB에 없는 id라면
                return "login";
            }

        } else {
            return "login"; // 세션에 값이없다면 login으로 보내기
        }
    }




    @GetMapping(value = "/login")
    public String login(HttpSession session, Model model,  @PageableDefault Pageable pageable) {
        // 세션에 값이 존재하면
        if (session.getAttribute("id")!=null) {
            // 출퇴근기록 Model에 갖다주기
            Long id = (Long) session.getAttribute("id");
            boolean restStopPossibile = restService.restStopPossibile(id);
            List<CommuteResponseDto> commuteResponseDtoList = commuteService.viewCommuteList(id);
            CommuteResponseDto todayCommuteResponseDto = commuteService.viewTodayCommute(id);
            String restStartTime = restService.viewTodayRest(id);

            Page<Commute> lastCommuteResponseDtoList = commuteService.searchInfoList(pageable, id);  //////
            model.addAttribute("list", lastCommuteResponseDtoList);


            model.addAttribute("commuteResponseDtoList", commuteResponseDtoList);
            model.addAttribute("todayCommuteResponseDto", todayCommuteResponseDto);
            model.addAttribute("restStopPossibile", restStopPossibile);
            model.addAttribute("restStartTime", restStartTime);
            model.addAttribute("personCnt", lastCommuteResponseDtoList.getTotalElements());

            return "main";
        } else { // 세션에 값이 존재 안한다면 login View 보여주기

            return "login"; // 세션에 값이없다면 login View 보여주기
        }

    }


    @RequestMapping(value = "/main") // 회원id로 조회한 출퇴근기록에서 출근이 안찍혀있다면 출근버튼만 보여야함.
    public String main(HttpSession session, Principal principal, Model model, @PageableDefault Pageable pageable) {

        Long id = 0l; // 회원고유 id
        // 세션에 값이 존재하면
        if (session.getAttribute("id")!=null) {
            // 출퇴근기록 Model에 갖다주기
            id = (Long) session.getAttribute("id");


        } else { // 세션에 값이 없다면 -> 로그인페이지로 이동.
//            boolean loginResult = memberService.login(memberDto.getMemberId(), memberDto.getMemberPw());
//            if (loginResult) { // 로그인 성공시



            try {
                // 이름 Return
                String name = memberService.searchName(principal.getName());

                // 세션에 MemberId저장
                session.setAttribute("id",memberService.searchId(principal.getName())); // 고유id가 와야한다.
                session.setAttribute("name", name);

                // 출퇴근기록 Model에 갖다주기
                id = memberService.searchId(principal.getName());
                // main.html로
            } catch (Exception e) {
                return "redirect:/login"; // 로그인 실패로 redirect
            }


//            } else { // 로그인 실패시
//            }
        }
        // boolean restStopPossibile : 휴식DB가 있는지
        boolean restStopPossibile = restService.restStopPossibile(id);
        List<CommuteResponseDto> commuteResponseDtoList = commuteService.viewCommuteList(id);
        CommuteResponseDto todayCommuteResponseDto = commuteService.viewTodayCommute(id);
        String restStartTime = restService.viewTodayRest(id);

        Page<Commute> lastCommuteResponseDtoList = commuteService.searchInfoList(pageable, id);  //////
        model.addAttribute("list", lastCommuteResponseDtoList);


        model.addAttribute("commuteResponseDtoList", commuteResponseDtoList);
        model.addAttribute("todayCommuteResponseDto", todayCommuteResponseDto);
        model.addAttribute("restStopPossibile", restStopPossibile);
        model.addAttribute("restStartTime", restStartTime);
        model.addAttribute("personCnt", lastCommuteResponseDtoList.getTotalElements());
        return "main"; //
    }

//   메인 API 백업
//    @RequestMapping(value = "/main") // 회원id로 조회한 출퇴근기록에서 출근이 안찍혀있다면 출근버튼만 보여야함.
//    public String main(HttpSession session, MemberDto memberDto, Model model,  @PageableDefault Pageable pageable) {
//
//        Long id = 0l; // 회원고유 id
//        // 세션에 값이 존재하면
//        if (session.getAttribute("id")!=null) {
//            // 출퇴근기록 Model에 갖다주기
//            id = (Long) session.getAttribute("id");
//
//
//        } else { // 세션에 값이 없다면 -> 로그인페이지로 이동. (근데 로그인정보가 일치한다면 main Reload)
//            boolean loginResult = memberService.login(memberDto.getMemberId(), memberDto.getMemberPw());
//            if (loginResult) { // 로그인 성공시
//
//                // 만약 idCheck에 체크가 되어있다면,
//                // 그리고 쿠키값이 비어있다면,
//                // ----->>>> 쿠키저장
//
//
//
//                // 이름 Return
//                String name = memberService.searchName(memberDto.getMemberId());
//
//                // 세션에 MemberId저장
//                session.setAttribute("id",memberService.searchId(memberDto.getMemberId())); // 고유id가 와야한다.
//                session.setAttribute("name", name);
//
//                // 출퇴근기록 Model에 갖다주기
//                id = memberService.searchId(memberDto.getMemberId());
//
//                // main.html로
//            } else { // 로그인 실패시
//                return "redirect:/login"; // 로그인 실패로 redirect
//            }
//        }
//        // boolean restStopPossibile : 휴식DB가 있는지
//        boolean restStopPossibile = restService.restStopPossibile(id);
//        List<CommuteResponseDto> commuteResponseDtoList = commuteService.viewCommuteList(id);
//        CommuteResponseDto todayCommuteResponseDto = commuteService.viewTodayCommute(id);
//        String restStartTime = restService.viewTodayRest(id);
//
//        Page<Commute> lastCommuteResponseDtoList = commuteService.searchInfoList(pageable, id);  //////
//        model.addAttribute("list", lastCommuteResponseDtoList);
//
//
//        model.addAttribute("commuteResponseDtoList", commuteResponseDtoList);
//        model.addAttribute("todayCommuteResponseDto", todayCommuteResponseDto);
//        model.addAttribute("restStopPossibile", restStopPossibile);
//        model.addAttribute("restStartTime", restStartTime);
//        model.addAttribute("personCnt", lastCommuteResponseDtoList.getTotalElements());
//        return "main"; //
//    }


    @GetMapping("/clickStartTime")
    public String clickStartTime(HttpSession session,
                                 MemberDto memberDto,
                                 Model model,
//                                 @RequestParam(value = "message", required = false) String message,
                                 MessageDto messageDto,
                                 @PageableDefault Pageable pageable) {
        String message = messageDto.getMessage();

        // 출퇴근기록 Model에 갖다주기
        Long id = (Long) session.getAttribute("id");

        if (message==null||message.equals("")) {
            System.out.println(message);
            commuteService.saveStartTime(id);
        } else {
            System.out.println(message);
            commuteService.saveStartTimeAndMessage(id, message);
        }

        List<CommuteResponseDto> commuteResponseDtoList = commuteService.viewCommuteList(id);
        CommuteResponseDto todayCommuteResponseDto = commuteService.viewTodayCommute(id);

        Page<Commute> lastCommuteResponseDtoList = commuteService.searchInfoList(pageable, id);  //////
        model.addAttribute("list", lastCommuteResponseDtoList);


        model.addAttribute("commuteResponseDtoList", commuteResponseDtoList);
        model.addAttribute("todayCommuteResponseDto", todayCommuteResponseDto);
        return "main"; //
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("id");
        session.removeAttribute("name");

        return "redirect:/login";
    }


    @PostMapping("/rest-start")
    public String restStart(HttpSession session,
                            Model model,
//                            @RequestParam(value="restReason", required = false) String restReason,
                            RestDto restDto
    ) {
        String restReason = restDto.getRestReason();
        // 출퇴근기록 Model에 갖다주기
        Long id = (Long) session.getAttribute("id");

        restService.startRestTime2(id, restReason);

        List<CommuteResponseDto> commuteResponseDtoList = commuteService.viewCommuteList(id);
        CommuteResponseDto todayCommuteResponseDto = commuteService.viewTodayCommute(id);
        model.addAttribute("commuteResponseDtoList", commuteResponseDtoList);
        model.addAttribute("todayCommuteResponseDto", todayCommuteResponseDto);
        return "redirect:/main";
    }

    @PostMapping("/rest-stop")
    public String restStop(HttpSession session,
                           Model model
                           ) {
        // 출퇴근기록 Model에 갖다주기
        Long id = (Long) session.getAttribute("id"); // 회원고유id Key값

        // 휴식종료해주기
        restService.stopRestTime2(id);

        List<CommuteResponseDto> commuteResponseDtoList = commuteService.viewCommuteList(id);
        CommuteResponseDto todayCommuteResponseDto = commuteService.viewTodayCommute(id);
        model.addAttribute("commuteResponseDtoList", commuteResponseDtoList);
        model.addAttribute("todayCommuteResponseDto", todayCommuteResponseDto);
        return "redirect:/main";
    }

    // 퇴근버튼눌렀을때
    @PostMapping("/leave")
    public String leave(HttpSession session,
                        CommuteDto commuteDto
    ) {

        String leaveMessage = commuteDto.getLeaveNote();
        Long id = (Long) session.getAttribute("id"); // 회원고유id Key값

        commuteService.saveLeaveTime2(id, leaveMessage);


        return "redirect:/main";
    }


    @RequestMapping(value = "/chart")
    public String chart(HttpSession session, MemberDto memberDto, Model model, @PageableDefault Pageable pageable) {




        Long id = 0l; // 회원고유 id
        // 세션에 값이 존재하면
        if (session.getAttribute("id")!=null) {
            // 출퇴근기록 Model에 갖다주기
            id = (Long) session.getAttribute("id");


        } else { // 세션에 값이 없다면 -> 로그인페이지로 이동.
            boolean loginResult = memberService.login(memberDto.getMemberId(), memberDto.getMemberPw());
            if (loginResult) { // 로그인 성공시
                // 이름 Return
                String name = memberService.searchName(memberDto.getMemberId());

                // 세션에 MemberId저장
                session.setAttribute("id",memberService.searchId(memberDto.getMemberId())); // 고유id가 와야한다.
                session.setAttribute("name", name);

                // 출퇴근기록 Model에 갖다주기
                id = memberService.searchId(memberDto.getMemberId());

                // main.html로
            } else { // 로그인 실패시
                return "redirect:/login"; // 로그인 실패로 redirect
            }
        }
        // boolean restStopPossibile : 휴식DB가 있는지
        boolean restStopPossibile = restService.restStopPossibile(id);
        CommuteResponseDto todayCommuteResponseDto = commuteService.viewTodayCommute(id);
        String restStartTime = restService.viewTodayRest(id);

        List<CommuteResponseDto> commuteResponseDtoList = commuteService.viewCommuteList(id);           //////
        model.addAttribute("commuteResponseDtoList", commuteResponseDtoList);               //////

        model.addAttribute("todayCommuteResponseDto", todayCommuteResponseDto);
        model.addAttribute("restStopPossibile", restStopPossibile);
        model.addAttribute("restStartTime", restStartTime);


        Page<Commute> lastCommuteResponseDtoList = commuteService.searchInfoList(pageable, id);  //////
        model.addAttribute("list", lastCommuteResponseDtoList);                            //////



        return "main"; //
//        return "/chart";


    }

    @GetMapping(value = "/friendship")
    public String login(HttpSession session, Model model, MemberDto memberDto) {



        Long id = 0l; // 회원고유 id
        // 세션에 값이 존재하면
        if (session.getAttribute("id")!=null) {
            // 출퇴근기록 Model에 갖다주기
            id = (Long) session.getAttribute("id");


        } else { // 세션에 값이 없다면 -> 로그인페이지로 이동.
            boolean loginResult = memberService.login(memberDto.getMemberId(), memberDto.getMemberPw());
            if (loginResult) { // 로그인 성공시
                // 이름 Return
                String name = memberService.searchName(memberDto.getMemberId());

                // 세션에 MemberId저장
                session.setAttribute("id",memberService.searchId(memberDto.getMemberId())); // 고유id가 와야한다.
                session.setAttribute("name", name);

                // 출퇴근기록 Model에 갖다주기
                id = memberService.searchId(memberDto.getMemberId());

                // main.html로
            } else { // 로그인 실패시
                return "redirect:/login"; // 로그인 실패로 redirect
            }
        }



        return "friendship"; //
//        return "/chart";









    }


//    @PostMapping(value = "/calendar")
//    public JSONArray monthPlan() {
//        List<MemberCalendarDto> listAll = memberService.findTest();
//
//        JSONObject jsonObj = new JSONObject();
//        JSONArray jsonArr = new JSONArray();
//
//        HashMap<String, Object> hash = new HashMap<>();
//
//        for (int i = 0; i < listAll.size(); i++) {
//            hash.put("title", listAll.get(i).getTitle());
//            hash.put("start", listAll.get(i).getStart());
//
//            jsonObj = new JSONObject(hash);
//            jsonArr.put(jsonObj);
//        }
//
//        return jsonArr;
//    }




}
