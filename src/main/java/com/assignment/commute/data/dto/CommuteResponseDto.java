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
public class CommuteResponseDto {

    private Long id;
    private LocalDate commuteDate;             // 날짜 LocalDate
    private LocalTime startTime;        // 출근시간 LocalTime
    private LocalTime leaveTime;        // 퇴근시간 LocalTime
    private LocalTime lastRestTime;         // 최종휴식시간
    private LocalTime workingTime;      // 근무시간 : 퇴근시간-출근시간
    private LocalTime tardyTime;        // 지각시간 : 현재시간이 10:00 이후일경우, 현재시간-10:00
    private String tardyReason;         // 지각사유
    private String leaveNote;           // 퇴근비고
    private Long memberId;              // 회원 테이블과 다대일 매핑
    private LocalDateTime createdAt;            // 생성날짜
    private LocalDateTime updatedAt;            // 업데이트날짜

}
