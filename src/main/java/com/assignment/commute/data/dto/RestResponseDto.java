package com.assignment.commute.data.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class RestResponseDto {

    private Long id;
    private LocalTime restStartTime;                    //  휴식시작시간
    private LocalTime restStopTime;                    //  휴식종료시간
    private String restReason;                    //  휴식조건
    private LocalDate restDate;                       // 휴식날짜
    private Long memberId;                       // 회원 테이블과 다대일 매핑
    private LocalDateTime createdAt;              // 생성날짜
    private LocalDateTime updatedAt;              // 업데이트날짜

}
