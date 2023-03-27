package com.assignment.commute.data.dto;

import com.assignment.commute.data.entity.Member;
import lombok.*;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MemberCalendarDto {

    private Integer id;
    private String title;
    private String start;

    private Long memberId;




}
