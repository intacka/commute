package com.assignment.commute.controller;

import com.assignment.commute.data.dto.*;
import com.assignment.commute.data.entity.Commute;
import com.assignment.commute.service.CommuteService;
import com.assignment.commute.service.MemberService;
import com.assignment.commute.service.RestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@Controller
public class CreateController {

    private final MemberService memberService;

    @Autowired
    public CreateController(MemberService memberService) {
        this.memberService = memberService;
    }



    @RequestMapping(value = "/create")
    public String createMember() {
        return "create";
    }

    @PostMapping(value = "/real-create")
    public String realCreateMember(@RequestParam("id") String id,
                                   @RequestParam("pw") String pw,
                                   @RequestParam("name") String name,
                                   @RequestParam("team") String team) {

        memberService.createMember(id, pw, name, team);
        return "login";
    }

}
