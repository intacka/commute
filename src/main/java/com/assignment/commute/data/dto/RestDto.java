package com.assignment.commute.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestDto {

    private LocalTime restStartTime;                    //  휴식시작시간
    private LocalTime restStopTime;                    //  휴식종료시간
    private String restReason;                    //  휴식조건
    private LocalDate restDate;                       // 휴식날짜
    private Long memberId;              // 회원 테이블과 다대일 매핑


}
