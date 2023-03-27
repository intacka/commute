package com.assignment.commute.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResearchDto {


    private String selectBox;             // 찾는 팀선택 이름
    private String calenderData;             // 찾는 달력날짜


}
