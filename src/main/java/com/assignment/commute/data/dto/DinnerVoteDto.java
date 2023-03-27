package com.assignment.commute.data.dto;

import com.assignment.commute.data.entity.DinnerVote;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class DinnerVoteDto {

    private Integer dinnerVoteId;                   //  고유id

    private LocalDate dinnerDate;                //  날짜

    private String menuTime;                     // 메뉴,시간

    private String members;                     // 회원 이름 List

    private Integer cntMembers;                 // 회원 수

}
